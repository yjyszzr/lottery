package com.dl.shop.lottery.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.NetWorkUtil;
import com.dl.lottery.dto.DLBetMatchCellDTO;
import com.dl.lottery.dto.DLZQBetInfoDTO;
import com.dl.lottery.dto.DlJcZqMatchCellDTO;
import com.dl.lottery.dto.DlJcZqMatchDTO;
import com.dl.lottery.dto.DlJcZqMatchListDTO;
import com.dl.lottery.dto.DlJcZqMatchPlayDTO;
import com.dl.lottery.dto.LotteryMatchDTO;
import com.dl.lottery.dto.LotteryPrintDTO;
import com.dl.lottery.dto.MatchBetCellDTO;
import com.dl.base.enums.MatchPlayTypeEnum;
import com.dl.lottery.param.DlJcZqMatchBetParam;
import com.dl.lottery.param.DlJcZqMatchListParam;
import com.dl.shop.lottery.core.LocalWeekDate;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryMatchMapper;
import com.dl.shop.lottery.dao.LotteryMatchPlayMapper;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.model.LotteryMatchPlay;
import com.dl.shop.lottery.utils.PlayTypeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LotteryMatchService extends AbstractService<LotteryMatch> {
    
	@Resource
    private LotteryMatchMapper lotteryMatchMapper;
	
	@Resource
	private LotteryMatchPlayMapper lotteryMatchPlayMapper;
	
	@Value("${match.url}")
	private String matchUrl;

    /**
     * 获取赛事列表
     * @param param
     * @return
     */
	public DlJcZqMatchListDTO getMatchList(DlJcZqMatchListParam param) {
		DlJcZqMatchListDTO dlJcZqMatchListDTO = new DlJcZqMatchListDTO();
		List<DlJcZqMatchPlayDTO> matchPlayDTOList = lotteryMatchMapper.getMatchList(param.getPlayType(), param.getLeagueId());
		Map<String, DlJcZqMatchDTO> map = new HashMap<String, DlJcZqMatchDTO>();
		matchPlayDTOList.forEach(dto->{
			//页面展示日期
			int matchTime = dto.getMatchTime();
			int betEndTime = matchTime - ProjectConstant.BET_PRESET_TIME;
			dto.setBetEndTime(betEndTime);
			LocalDate localDate = LocalDateTime.ofEpochSecond(matchTime, 0, ZoneOffset.UTC).toLocalDate();
			String matchDate = 	localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
			DayOfWeek dayOfWeek = localDate.getDayOfWeek();
			int value = dayOfWeek.getValue();
			String name = LocalWeekDate.getName(value);
			String matchDay = name + matchDate;
			if(LocalDate.now().isEqual(localDate)) {
				matchDay = "今日 " + matchDate;
			}
			dto.setMatchDay(matchDay);
			//赛事编码
			String changci = dto.getChangci();
			String changcinew = LocalWeekDate.getCode(changci.substring(0, 2))+changci.substring(2);
			String playCode = localDate.format(DateTimeFormatter.BASIC_ISO_DATE) + changcinew;
			dto.setPlayCode(playCode);
			//
			DlJcZqMatchDTO dlJcZqMatchDTO = map.get(matchDay);
			if(null == dlJcZqMatchDTO) {
				dlJcZqMatchDTO = new DlJcZqMatchDTO();
				dlJcZqMatchDTO.setMatchDay(matchDay);
				map.put(matchDay, dlJcZqMatchDTO);
			}
			//初始化投注选项
			initDlJcZqMatchCell(dto);
			dto.setPlayContent(null);
			if(dto.getIsHot() == 1) {
				dlJcZqMatchListDTO.getHotPlayList().add(dto);
			} else {
				dlJcZqMatchDTO.getPlayList().add(dto);
			}
		});
		map.forEach((key, value) ->{
			dlJcZqMatchListDTO.getPlayList().add(value);
		});
		dlJcZqMatchListDTO.setAllMatchCount(matchPlayDTOList.size()+"");
	    return dlJcZqMatchListDTO;
	}
	
	/**
	 * 初始化球赛类型投注选项
	 * @param dto
	 */
	private void initDlJcZqMatchCell(DlJcZqMatchPlayDTO dto) {
		Integer playType = dto.getPlayType();
		switch(playType) {
			case 1:
				initDlJcZqMatchCell1(dto);
				break;
			case 2:
				initDlJcZqMatchCell2(dto);
				break;
			case 3:
				initDlJcZqMatchCell3(dto);
				break;
			case 4:
				initDlJcZqMatchCell4(dto);
				break;
			case 5:
				initDlJcZqMatchCell5(dto);
				break;
			case 6:
				initDlJcZqMatchCell6(dto);
				break;
			case 7:
				initDlJcZqMatchCell7(dto);
				break;
		}
	}
	/**
	 * 让球胜平负
	 * {"p_status":"Selling","a":"2.35","d_trend":"0","fixedodds":"+1","d":"3.20","h":"2.56","vbt":"0",
		"int":"1","a_trend":"0","goalline":"","single":"0","o_type":"F","p_code":"HHAD","cbt":"1",
		"allup":"1","h_trend":"0","p_id":"471462","l_trend":"0"}
	 * @param dto
	 */
	private void initDlJcZqMatchCell1(DlJcZqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		String hOdds = jsonObj.getString("h");
		String dOdds = jsonObj.getString("d");
		String aOdds = jsonObj.getString("a");
		String fixedOdds = jsonObj.getString("fixedodds");
		dto.setFixedOdds(fixedOdds);
		Integer single = jsonObj.getInteger("single");
		dto.setSingle(single);
		dto.setHomeCell(new DlJcZqMatchCellDTO("3", "主胜", hOdds));
		dto.setFlatCell(new DlJcZqMatchCellDTO("1", "平局", dOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO("0", "客胜", aOdds));
	}
	/**
	 * 胜平负
	 * {"p_status":"Selling","a":"1.39","d_trend":"0","fixedodds":"","d":"3.90","h":"6.50","vbt":"0",
		"int":"1","a_trend":"0","goalline":"","single":"0","o_type":"F","p_code":"HAD","cbt":"1",
		"allup":"1","h_trend":"0","p_id":"471461","l_trend":"0"}
	 * @param dto
	 */
	private void initDlJcZqMatchCell2(DlJcZqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		String hOdds = jsonObj.getString("h");
		String dOdds = jsonObj.getString("d");
		String aOdds = jsonObj.getString("a");
		Integer single = jsonObj.getInteger("single");
		dto.setSingle(single);
		dto.setHomeCell(new DlJcZqMatchCellDTO("3", "主胜", hOdds));
		dto.setFlatCell(new DlJcZqMatchCellDTO("1", "平局", dOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO("0", "客胜", aOdds));
	}
	/**
	 * 比分
	 * {"fixedodds":"","vbt":"0","a_trend":"0","0105":"60.00","0204":"60.00","0303":"80.00",
	 * "0402":"250.0","0501":"700.0","goalline":"","0205":"120.0","0502":"700.0","o_type":"F",
	 * "0004":"19.00","0103":"11.00","0202":"17.00","0301":"60.00","0400":"300.0","0005":"50.00",
	 * "0104":"25.00","0203":"26.00","0302":"60.00","0401":"250.0","0500":"900.0","0002":"6.00",
	 * "0101":"7.00","0200":"30.00","p_code":"CRS","0003":"9.50","0102":"7.00","0201":"16.00",
	 * "0300":"80.00","0000":"9.50","0001":"5.80","0100":"13.00","l_trend":"0","p_status":"Selling",
	 * "d_trend":"0","-1-h":"250.0","-1-a":"40.00","-1-d":"500.0","int":"1","single":"1","cbt":"1",
	 * "allup":"1","h_trend":"0","p_id":"471464"}
	 * @param dto
	 */
	private void initDlJcZqMatchCell3(DlJcZqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		Integer single = jsonObj.getInteger("single");
		dto.setSingle(single);
		Set<String> keySet = jsonObj.keySet();
		DlJcZqMatchCellDTO homeCell = new DlJcZqMatchCellDTO("3", "主胜", null);
		homeCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(10));
		dto.setHomeCell(homeCell);
		DlJcZqMatchCellDTO flatCell = new DlJcZqMatchCellDTO("1", "平局", null);
		flatCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(10));
		dto.setFlatCell(flatCell);
		DlJcZqMatchCellDTO visitingCell = new DlJcZqMatchCellDTO("0", "客胜", null);
		visitingCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(10));
		dto.setVisitingCell(visitingCell);
		//List<DlJcZqMatchCellDTO> matchCells = new ArrayList<DlJcZqMatchCellDTO>();
		String regex = "^0\\d{3}$";
		for(String key: keySet) {
			if(Pattern.matches(regex, key)) {
				String code = String.valueOf(new char[] {key.charAt(1),key.charAt(3)});
				String odds = jsonObj.getString(key);
				String name = "";
				if("90".equals(code)) {
					name = "胜其它";
				} else if("99".equals(code)) {
					name = "平其它";
				} else if("09".equals(code)) {
					name = "负其它";
				}else {
					name = String.valueOf(new char[] {key.charAt(1),':',key.charAt(3)});
				}
				if(key.charAt(1) > key.charAt(3)) {
					homeCell.getCellSons().add(new DlJcZqMatchCellDTO(code, name, odds));
				} else if(key.charAt(1) < key.charAt(3)) {
					visitingCell.getCellSons().add(new DlJcZqMatchCellDTO(code, name, odds));
				}else {
					flatCell.getCellSons().add(new DlJcZqMatchCellDTO(code, name, odds));
				}
				//matchCells.add(new DlJcZqMatchCellDTO(code, name, odds));
			}
		}
		//dto.setMatchCells(matchCells);
	}
	/**
	 * 总进球数
	 * {"s3":"3.55","p_status":"Selling","s4":"5.60","d_trend":"0","s5":"10.50","fixedodds":"",
		"s6":"18.00","s7":"29.00","vbt":"0","int":"1","a_trend":"0","single":"1","goalline":"",
		"o_type":"F","p_code":"TTG","cbt":"1","allup":"1","h_trend":"0","s0":"9.50","s1":"4.10",
		"p_id":"471465","s2":"3.15","l_trend":"0"}
	 * @param dto
	 */
	private void initDlJcZqMatchCell4(DlJcZqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		Integer single = jsonObj.getInteger("single");
		dto.setSingle(single);
		Set<String> keySet = jsonObj.keySet();
		List<DlJcZqMatchCellDTO> matchCells = new ArrayList<DlJcZqMatchCellDTO>();
		String regex = "^s\\d$";
		for(String key: keySet) {
			if(Pattern.matches(regex, key)) {
				String code = String.valueOf(key.charAt(1));
				String odds = jsonObj.getString(key);
				String name = String.valueOf(new char[] {key.charAt(1),'球'});
				if("7".equals(code)) {
					name += "或更多";
				} 
				matchCells.add(new DlJcZqMatchCellDTO(code, name, odds));
			}
		}
		dto.setMatchCells(matchCells);
	}
	
	/**
	 * 半全场
	 * {"aa":"1.92","dd":"5.60","hh":"10.50","p_status":"Selling","d_trend":"0","fixedodds":"",
		"ad":"16.00","dh":"12.00","ah":"50.00","vbt":"0","int":"1","a_trend":"0","single":"1",
		"goalline":"","o_type":"F","p_code":"HAFU","cbt":"1","allup":"1","ha":"25.00","h_trend":"0",
		"hd":"16.00","da":"4.00","p_id":"471463","l_trend":"0"}
	 * @param dto
	 */
	private void initDlJcZqMatchCell5(DlJcZqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		List<DlJcZqMatchCellDTO> matchCells = new ArrayList<DlJcZqMatchCellDTO>();
		JSONObject jsonObj = JSON.parseObject(playContent);
		Integer single = jsonObj.getInteger("single");
		dto.setSingle(single);
		String hhOdds = jsonObj.getString("hh");
		matchCells.add(new DlJcZqMatchCellDTO("33", "胜-胜", hhOdds));
		String hdOdds = jsonObj.getString("hd");
		matchCells.add(new DlJcZqMatchCellDTO("31", "胜-平", hdOdds));
		String haOdds = jsonObj.getString("ha");
		matchCells.add(new DlJcZqMatchCellDTO("30", "胜-负", haOdds));
		String ddOdds = jsonObj.getString("dd");
		matchCells.add(new DlJcZqMatchCellDTO("11", "平-平", ddOdds));
		String daOdds = jsonObj.getString("da");
		matchCells.add(new DlJcZqMatchCellDTO("10", "平-负", daOdds));
		String dhOdds = jsonObj.getString("dh");
		matchCells.add(new DlJcZqMatchCellDTO("13", "平-胜", dhOdds));
		String aaOdds = jsonObj.getString("aa");
		matchCells.add(new DlJcZqMatchCellDTO("00", "负-负", aaOdds));
		String adOdds = jsonObj.getString("ad");
		matchCells.add(new DlJcZqMatchCellDTO("01", "负-平", adOdds));
		String ahOdds = jsonObj.getString("ah");
		matchCells.add(new DlJcZqMatchCellDTO("03", "负-胜", ahOdds));
		dto.setMatchCells(matchCells);
	}
	private void initDlJcZqMatchCell6(DlJcZqMatchPlayDTO dto) {
		
	}
	private void initDlJcZqMatchCell7(DlJcZqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		String hOdds = jsonObj.getString("zbb");
		String aOdds = jsonObj.getString("zb");
		dto.setHomeCell(new DlJcZqMatchCellDTO("3", "主胜", hOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO("0", "客胜", aOdds));
	}
	/**
	 * 转换页面展示用的比赛时间
	 * @param matchTime
	 * @return
	 */
	private String date2Show(Date matchTime) {
		LocalDate localDate = LocalDateTime.ofInstant(matchTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
		DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		int value = dayOfWeek.getValue();
		String name = LocalWeekDate.getName(value);
		String matchDate = 	localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
		return name + matchDate;
	}
	/**
	 * 抓取赛事列表并保存
	 */
	@Transactional
	public void saveMatchList() {
		//赛事、玩法汇总列表
		Map<String, Object> map = new HashMap<String, Object>();
		//赛事列表
		Map<String, JSONObject> matchs = new HashMap<String, JSONObject>();
		//各赛事的玩法列表
		List<Map<String, Object>> matchPlays = new LinkedList<Map<String, Object>>();
		map.put("matchs", matchs);
		map.put("matchPlays", matchPlays);
		//抓取胜平负数据
		map = getCollectMatchData(map, MatchPlayTypeEnum.PLAY_TYPE_HAD.getMsg());
		//抓取让球胜平负数据
		map = getCollectMatchData(map, MatchPlayTypeEnum.PLAY_TYPE_HHAD.getMsg());
		//抓取半全场数据
		map = getCollectMatchData(map, MatchPlayTypeEnum.PLAY_TYPE_HAFU.getMsg());
		//抓取总进球数据
		map = getCollectMatchData(map, MatchPlayTypeEnum.PLAY_TYPE_TTG.getMsg());
		//抓取比分数据
		map = getCollectMatchData(map, MatchPlayTypeEnum.PLAY_TYPE_CRS.getMsg());
		//2选1不是标准玩法，需要数据转换获取
		map = getTwoSelOneMatchData(map, MatchPlayTypeEnum.PLAY_TYPE_TSO.getMsg());
		
		List<LotteryMatch> lotteryMatchs = getLotteryMatchData(matchs);
		log.info(lotteryMatchs.toString());
		
		//保存赛事数据
		saveMatchData(lotteryMatchs, matchPlays);
	}
	
	/**
	 * 逐个玩法组装数据
	 * @param map
	 * @param playType
	 * @return
	 */
	private Map<String, Object> getCollectMatchData(Map<String, Object> map, String playType) {
		@SuppressWarnings("unchecked")
		Map<String, JSONObject> matchs = (Map<String, JSONObject>) map.get("matchs");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> matchPlays = (List<Map<String, Object>>) map.get("matchPlays");
		Map<String, Object> backDataMap = getBackMatchData(playType);
		if(null != backDataMap && backDataMap.size() > 0) {
			Map<String, Object> matchPlay = new HashMap<String, Object>();
	    	for(Map.Entry<String, Object> entry : backDataMap.entrySet()) {
	    		JSONObject jo = (JSONObject) entry.getValue();
	    		Set<String> keys = matchs.keySet();
	    		if(!keys.contains(jo.getString("id"))) {
	    			matchs.put(jo.getString("id"), jo);
	    		}
	    		matchPlay.put(jo.getString("id"), jo);
	    		matchPlay.put("playType", playType);
	    	}
	    	matchPlays.add(matchPlay);
	    }
		map.put("matchs", matchs);
		map.put("matchPlays", matchPlays);
		return map;
	}
	
	/**
	 * 转换2选1数据
	 * @param map
	 * @param playType
	 * @return
	 */
	private Map<String, Object> getTwoSelOneMatchData(Map<String, Object> map, String playType) {
		@SuppressWarnings("unchecked")
		Map<String, JSONObject> matchs = (Map<String, JSONObject>) map.get("matchs");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> matchPlays = (List<Map<String, Object>>) map.get("matchPlays");
		Map<String, Object> hadMatchPlay = new HashMap<String, Object>();
		Map<String, Object> hhadMatchPlay = new HashMap<String, Object>();
		Map<String, Object> tsoMatchPlay = new HashMap<String, Object>();
		for(Map<String, Object> matchPlay : matchPlays) {
			if(MatchPlayTypeEnum.PLAY_TYPE_HAD.getMsg().equals(matchPlay.get("playType").toString())) {
				hadMatchPlay = matchPlay;
			}
			if(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getMsg().equals(matchPlay.get("playType").toString())) {
				for(Map.Entry<String, Object> entry : matchPlay.entrySet()) {
					if(!"playType".equals(entry.getKey())) {
						JSONObject jo = (JSONObject) entry.getValue();
						JSONObject hhadJo = jo.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getMsg());
						String fixedodds = hhadJo.getString("fixedodds");
						if(fixedodds.equals("+1") || fixedodds.equals("-1")) {
							hhadMatchPlay.put(entry.getKey(), entry.getValue());
						}
					}
				}
			}
		}
		if(hhadMatchPlay.size() > 0) {
			for(Map.Entry<String, Object> entry : hhadMatchPlay.entrySet()) {
				JSONObject jo = (JSONObject) entry.getValue();
				JSONObject hhadJo = jo.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getMsg());
				String fixedodds = hhadJo.getString("fixedodds");
				JSONObject tsoJo = new JSONObject();
				boolean flag = false;
				JSONObject hadJo = null;
				for(Map.Entry<String, Object> hadEntry : hadMatchPlay.entrySet()) {
					if(!"playType".equals(entry.getKey())) {
						if(entry.getKey().equals(hadEntry.getKey())) {
							JSONObject jsonObject = (JSONObject) hadEntry.getValue();
							hadJo = jsonObject.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_HAD.getMsg());
						}
					}
				}
				if(fixedodds.equals("+1")) {
					tsoJo.put("zbb", "主不败 " + hhadJo.getString("h"));
					tsoJo.put("zb", "主败 " + hadJo.getString("a"));
					flag = true;
				} else if(fixedodds.equals("-1")) {
					tsoJo.put("zbs", "主不胜 " + hhadJo.getString("a"));
					tsoJo.put("zs", "主胜 " + hadJo.getString("h"));
					flag = true;
				}
				if(flag) {
					jo.put("tso", tsoJo);
					tsoMatchPlay.put(entry.getKey(), jo);
					tsoMatchPlay.put("playType", playType);
				}
			}
			matchPlays.add(tsoMatchPlay);
		}
		map.put("matchs", matchs);
		map.put("matchPlays", matchPlays);
		return map;
	}
	
	/**
	 * 获取返回的赛事数据
	 * @return
	 */
	private Map<String, Object> getBackMatchData(String playType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("poolcode[]", playType);
		String json = NetWorkUtil.doGet(matchUrl, map, "UTF-8");
	    if (json.contains("error")) {
	        throw new ServiceException(RespStatusEnum.FAIL.getCode(), playType + "赛事查询失败");
	    }
	    JSONObject jsonObject = JSONObject.parseObject(json);
	    JSONObject jo = jsonObject.getJSONObject("data");
	    map = jo;
	    return map;
	}
	
	/**
	 * 组装赛事列表数据
	 * @param matchs
	 * @return
	 */
	private List<LotteryMatch> getLotteryMatchData(Map<String, JSONObject> matchs){
		List<LotteryMatch> lotteryMatchs = new LinkedList<LotteryMatch>();
		if(null != matchs && matchs.size() > 0) {
			for (Map.Entry<String, JSONObject> entry : matchs.entrySet()) {
				LotteryMatch lotteryMatch = new LotteryMatch();
				JSONObject jo = entry.getValue();
				lotteryMatch.setLeagueId(Integer.parseInt(jo.getString("l_id")));
				lotteryMatch.setLeagueName(jo.getString("l_cn"));
				lotteryMatch.setLeagueAddr(jo.getString("l_cn_abbr"));
				lotteryMatch.setChangciId(Integer.parseInt(jo.getString("id")));
				lotteryMatch.setChangci(jo.getString("num"));
				lotteryMatch.setHomeTeamId(Integer.parseInt(jo.getString("h_id")));
				lotteryMatch.setHomeTeamName(jo.getString("h_cn"));
				lotteryMatch.setHomeTeamRank(jo.getString("h_order"));
				lotteryMatch.setHomeTeamAbbr(jo.getString("h_cn_abbr"));
				lotteryMatch.setVisitingTeamId(Integer.parseInt(jo.getString("a_id")));
				lotteryMatch.setVisitingTeamName(jo.getString("a_cn"));
				lotteryMatch.setVisitingTeamRank(jo.getString("a_order"));
				lotteryMatch.setVisitingTeamAbbr(jo.getString("a_cn_abbr"));
				try {
					String machtimeStr = jo.getString("date") + " " + jo.getString("time");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date machTime = sdf.parse(machtimeStr);
					lotteryMatch.setMatchTime(machTime);
					sdf.applyPattern("yyyy-MM-dd");
					Date showTime = sdf.parse(jo.getString("b_date"));
					lotteryMatch.setShowTime(showTime);
					lotteryMatch.setMatchSn(this.commonCreateIssue(jo.getString("date"), lotteryMatch.getChangci()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				lotteryMatch.setCreateTime(DateUtil.getCurrentTimeLong());
				lotteryMatch.setIsShow(ProjectConstant.IS_SHOW);
				lotteryMatch.setIsDel(ProjectConstant.IS_NOT_DEL);
				lotteryMatchs.add(lotteryMatch);
			}   
		}
		return lotteryMatchs;
	}
	
	/**
	 * 构造场次的公共方法
	 */
	public String commonCreateIssue(String dateStr,String changci ) {
		String dateItem = dateStr.replaceAll("-", "");
		int weekDay = LocalWeekDate.getCode(changci.substring(0, 2));
		return dateItem + weekDay + changci.substring(2);
	}
	
	/**
	 * 组装赛事玩法数据
	 * @param matchPlays
	 * @return
	 */
	private LotteryMatchPlay getLotteryMatchPlayData(JSONObject matchPlay, Integer matchId, String playType){
		if(null == matchPlay) return null;
		LotteryMatchPlay lotteryMatchPlay = new LotteryMatchPlay();
		lotteryMatchPlay.setMatchId(matchId);
		lotteryMatchPlay.setPlayContent(matchPlay.getString(playType));
		lotteryMatchPlay.setPlayType(PlayTypeUtil.getPlayTypeCode(playType));
		lotteryMatchPlay.setStatus(ProjectConstant.MATCH_PLAY_STATUS_SELLING);
		lotteryMatchPlay.setIsHot(ProjectConstant.MATCH_PLAY_NOT_HOT);
		lotteryMatchPlay.setIsDel(ProjectConstant.IS_NOT_DEL);
		lotteryMatchPlay.setCreateTime(DateUtil.getCurrentTimeLong());
		lotteryMatchPlay.setUpdateTime(DateUtil.getCurrentTimeLong());
		return lotteryMatchPlay;
	}
	
	/**
	 * 保存赛事数据
	 * @param lotteryMatchs
	 * @param matchPlays
	 */
	private void saveMatchData(List<LotteryMatch> lotteryMatchs, List<Map<String, Object>> matchPlays) {
		if(CollectionUtils.isNotEmpty(lotteryMatchs)) {
			for(LotteryMatch lotteryMatch : lotteryMatchs) {
				List<LotteryMatchPlay> lotteryMatchPlays = new LinkedList<LotteryMatchPlay>();
				lotteryMatchMapper.insertMatch(lotteryMatch);
				if(CollectionUtils.isNotEmpty(matchPlays)) {
					for(Map<String, Object> map : matchPlays) {
						for(Map.Entry<String, Object> entry : map.entrySet()) {
							if(!"playType".equals(entry.getKey()) && lotteryMatch.getChangciId().toString().equals(entry.getKey())) {
								LotteryMatchPlay lotteryMatchPlay = getLotteryMatchPlayData((JSONObject)map.get(lotteryMatch.getChangciId().toString()), lotteryMatch.getMatchId(), map.get("playType").toString());
								if(null != lotteryMatchPlay) {
									lotteryMatchPlays.add(lotteryMatchPlay);
								}
								break;
							}
						}
					}
					if(CollectionUtils.isNotEmpty(lotteryMatchPlays)) {
						lotteryMatchPlayMapper.insertList(lotteryMatchPlays);
					}
				}
			}
		}
	}
	
	/**
	 * 计算组合
	 * @param str
	 * @param num
	 * @param list
	 * @param betList
	 */
	private void betNum(DLBetMatchCellDTO str, int num, List<MatchBetCellDTO> list, List<DLBetMatchCellDTO> betList) {
		LinkedList<MatchBetCellDTO> link = new LinkedList<MatchBetCellDTO>(list);
		while(link.size() > 0) {
			MatchBetCellDTO remove = link.remove(0);
			String changci = remove.getChangci();
			List<DlJcZqMatchCellDTO> betCells = remove.getBetCells();
			for(DlJcZqMatchCellDTO betCell: betCells) {
				DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
				dto.setPlayType(remove.getPlayType());
				Double amount = str.getAmount()*Double.valueOf(betCell.getCellOdds());
				dto.setAmount(Double.valueOf(String.format("%.2f", amount)));
				String betContent = str.getBetContent() + changci + "(" + betCell.getCellName() + "@" + betCell.getCellOdds() +")X";
				if(num == 1) {
					betContent += (str.getTimes() + "倍");
				}
				dto.setBetContent(betContent);
				dto.setBetType(str.getBetType());
				dto.setTimes(str.getTimes());
				if(num == 1) {
					betList.add(dto);
				}else {
					betNum(dto,num-1,link, betList);
				}
			}
		}
	}
	/**
	 * 计算组合
	 * @param str
	 * @param num
	 * @param list
	 * @param betList
	 */
	private void betNum1(String str, int num, List<String> list, List<String> betList) {
		LinkedList<String> link = new LinkedList<String>(list);
		while(link.size() > 0) {
			String remove = link.remove(0);
			String item = str+remove+",";
			if(num == 1) {
				betList.add(item.substring(0,item.length()-1));
			} else {
				betNum1(item,num-1,link, betList);
			}
		}
	}
	
	
	/**
	 * 投注信息获取
	 * @param param
	 * @return
	 */
	public DLZQBetInfoDTO getBetInfo(DlJcZqMatchBetParam param) {
		List<MatchBetCellDTO> matchBellCellList = param.getMatchBetCells();
		//读取设胆的索引
		List<String> indexList = new ArrayList<String>(matchBellCellList.size());
		List<String> danIndexList = new ArrayList<String>(3);
		for(int i=0; i< matchBellCellList.size(); i++) {
			indexList.add(i+"");
			int isDan = matchBellCellList.get(i).getIsDan();
			if(isDan != 0) {
				danIndexList.add(i+"");
			}
		}
		List<LotteryPrintDTO> lotteryPrints = new ArrayList<LotteryPrintDTO>();
		List<List<MatchBetCellDTO>>  matchBetList = new ArrayList<List<MatchBetCellDTO>>();
		List<DLBetMatchCellDTO> betCellList = new ArrayList<DLBetMatchCellDTO>();
		List<DLBetMatchCellDTO> maxBetCellList = new ArrayList<DLBetMatchCellDTO>();
		List<DLBetMatchCellDTO> minBetCellList = new ArrayList<DLBetMatchCellDTO>();
		String betTypes = param.getBetType();
		String[] split = betTypes.split(",");
		for(String betType: split) {
			char[] charArray = betType.toCharArray();
			if(charArray.length == 2 && charArray[1] == '1') {
				int num = Integer.valueOf(String.valueOf(charArray[0]));
				//
				List<String> betIndexList = new ArrayList<String>();
				betNum1("", num, indexList, betIndexList);
				if(danIndexList.size() > 0) {
					betIndexList = betIndexList.stream().filter(item->{
						for(String str: danIndexList) {
							if(item.contains(str)) {
								return true;
							}
						}
						return false;
					}).collect(Collectors.toList());
				}
				//
				for(String str: betIndexList) {
					String[] strArr = str.split(",");
					List<MatchBetCellDTO> maxList = new ArrayList<MatchBetCellDTO>(strArr.length);
					List<MatchBetCellDTO> minList = new ArrayList<MatchBetCellDTO>(strArr.length);
					List<MatchBetCellDTO> subList = new ArrayList<MatchBetCellDTO>(strArr.length);
					Arrays.asList(strArr).stream().forEach(item->{
						MatchBetCellDTO matchBetCell = matchBellCellList.get(Integer.valueOf(item));
						subList.add(matchBetCell);
						MatchBetCellDTO maxBetCell = maxOrMinOddsCell(matchBetCell, true);
						MatchBetCellDTO minBetCell = maxOrMinOddsCell(matchBetCell, false);
						maxList.add(maxBetCell);
						minList.add(minBetCell);
					});
					DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
					dto.setBetType(param.getBetType());
					dto.setTimes(param.getTimes());
					dto.setBetContent("");
					dto.setAmount(2.0*param.getTimes());
					List<DLBetMatchCellDTO> betCellList1 = new ArrayList<DLBetMatchCellDTO>();
					List<DLBetMatchCellDTO> maxBetCellList1 = new ArrayList<DLBetMatchCellDTO>();
					List<DLBetMatchCellDTO> minBetCellList1 = new ArrayList<DLBetMatchCellDTO>();
					betNum(dto, num, subList, betCellList1);
					betNum(dto, num, maxList, maxBetCellList1);
					betNum(dto, num, minList, minBetCellList1);
					matchBetList.add(subList);
					betCellList.addAll(betCellList1);
					maxBetCellList.addAll(maxBetCellList1);
					minBetCellList.addAll(minBetCellList1);
					String stakes = subList.stream().map(cdto->{
						String playCode = cdto.getPlayCode();
						String playType = cdto.getPlayType();
						String cellCodes = cdto.getBetCells().stream().map(cell->{
							return cell.getCellCode();
						}).collect(Collectors.joining(","));
						return playType + "|" + playCode + "|" + cellCodes;
					}).collect(Collectors.joining(";"));
					String issue = subList.get(0).getPlayCode();
					if(subList.size() > 1) {
						issue = subList.stream().max((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode())).get().getPlayCode();
					}
					int times = param.getTimes();
					Double money = betCellList1.size()*times*2.0;
					LotteryPrintDTO lotteryPrintDTO = new LotteryPrintDTO();
					lotteryPrintDTO.setBetType(betType);
					lotteryPrintDTO.setIssue(issue);
					lotteryPrintDTO.setMoney(money);
					lotteryPrintDTO.setPlayType(param.getPlayType());
					lotteryPrintDTO.setStakes(stakes);
					lotteryPrintDTO.setTimes(times);
					lotteryPrints.add(lotteryPrintDTO);
				}
			}
		}
		Double maxBonus = maxBetCellList.stream().map(item->{
			return item.getAmount();
		}).reduce(0.0, Double::sum);
		Double minBonus = minBetCellList.stream().map(item->{
			return item.getAmount();
		}).reduce(0.0, Double::sum);
		//页面返回信息对象
		DLZQBetInfoDTO betInfoDTO = new DLZQBetInfoDTO();
		betInfoDTO.setMaxBonus(Double.valueOf(String.format("%.2f", maxBonus)));
		betInfoDTO.setMinBonus(Double.valueOf(String.format("%.2f", minBonus)));
		betInfoDTO.setTimes(param.getTimes());
		betInfoDTO.setBetNum(betCellList.size());
		Double money = betCellList.size()*param.getTimes()*2.0;
		betInfoDTO.setMoney(Double.valueOf(String.format("%.2f", money)));
		betInfoDTO.setBetType(param.getBetType());
		betInfoDTO.setPlayType(param.getPlayType());
		betInfoDTO.setBetCells(betCellList);//投注方案
		betInfoDTO.setLotteryPrints(lotteryPrints);
		/*String stakes = matchBellCellList.stream().map(item->{
			String cellCodes = item.getBetCells().stream().map(cell->cell.getCellCode()).collect(Collectors.joining(","));
			return item.getPlayType() +"|" + item.getPlayCode() + "|" + cellCodes;
		}).collect(Collectors.joining(";"));
		betInfoDTO.setStakes(stakes);*/
		//所有选项的最后一个场次编码
		/*String issue = matchBellCellList.stream().max((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode())).get().getPlayCode();
		betInfoDTO.setIssue(issue);*/
//		betInfoDTO.setMatchBetList(matchBetList);
		return betInfoDTO;
	}
	/**
	 * 获取最大最小赔率的对象
	 * @param matchBetCell
	 * @param maxFlag
	 * @return
	 */
	private MatchBetCellDTO maxOrMinOddsCell(MatchBetCellDTO matchBetCell, boolean maxFlag) {
		List<DlJcZqMatchCellDTO> betCells = matchBetCell.getBetCells();
		DlJcZqMatchCellDTO maxOptional = null;
		if(betCells.size() > 1) {
			if(maxFlag) {
				maxOptional = betCells.stream().max((item1, item2)->{return Double.valueOf(item1.getCellOdds()).compareTo(Double.valueOf(item2.getCellOdds()));}).get();
			}else {
				maxOptional = betCells.stream().min((item1, item2)->{return Double.valueOf(item1.getCellOdds()).compareTo(Double.valueOf(item2.getCellOdds()));}).get();
			}
		}else {
			maxOptional = betCells.get(0);
		}
		List<DlJcZqMatchCellDTO> subList = new ArrayList<DlJcZqMatchCellDTO>(3);
		String maxOdds = maxOptional.getCellOdds();
		for(DlJcZqMatchCellDTO dto: betCells) {
			if(dto.getCellOdds().equals(maxOdds)) {
				subList.add(dto);
			}
		}
		MatchBetCellDTO maxBetCell = new MatchBetCellDTO();
		maxBetCell.setChangci(matchBetCell.getChangci());
		maxBetCell.setIsDan(matchBetCell.getIsDan());
		maxBetCell.setPlayCode(matchBetCell.getPlayCode());
		maxBetCell.setBetCells(subList);
		maxBetCell.setPlayType(matchBetCell.getPlayType());
		return maxBetCell;
	}
	
	/**
	 * 抓取当天和前两天的中国体育彩票足球竞猜网 开赛结果 并更新赛事结果  -- 定时任务
	 * @return
	 */
	public int pullMatchResult() {
		List<LotteryMatch> matchList = lotteryMatchMapper.getMatchListUnknowScoreToday();
		if(CollectionUtils.isEmpty(matchList)) {
			return -1;
		}
		
        Document doc = null;
        List<LotteryMatch> matchResult = new ArrayList<LotteryMatch>();
        try {
            doc = Jsoup.connect("http://info.sporttery.cn/football/match_result.php").get();
            Elements elements =  doc.getElementsByClass("m-tab");
            Elements trs = elements.select("tbody tr");
            for (int i = 0; i < trs.size(); i++) {
            	Elements tds = trs.get(i).select("td");
            	LotteryMatch lotteryMatch = new  LotteryMatch();
            	for(int j = 0,tdsLen = tds.size();j < tdsLen;j++) {
            		if(j <= 1) {
            			String str = getMatchSnStr(tds);
            			if(StringUtils.isEmpty(str)) {
            				break;
            			}
            			lotteryMatch.setMatchSn(str);
            		}
            		lotteryMatch.setFirstHalf(String.valueOf(tds.get(4).text()));
            		lotteryMatch.setWhole(String.valueOf(tds.get(5).text()));
            		lotteryMatch.setStatus(ProjectConstant.MATCH_FINISH);
            		matchResult.add(lotteryMatch);
            		break;
            	}
            }
        } catch (Exception e) {
        	log.error(e.getMessage());
        }
        
        int rst = lotteryMatchMapper.updateMatchBatch(matchResult);
        
		return rst;
	}
	
	/** 
	 * 对抓取的数据构造赛事编号
	 * @param tds
	 * @return
	 */
	public String getMatchSnStr(Elements tds) {
		String now = DateUtil.getCurrentDate(DateUtil.date_sdf);
		String str1 = tds.get(0).text();
		if(!now.equals(str1)) {
			return "";
		}
		Boolean goOn = str1.contains("-");
		if(goOn == false) {
			return "";
		}
		str1 = str1.replaceAll("-", "");
		String str2 = tds.get(1).text();
		String str3 = str2.substring(str2.length() - 3);
		str2 = str2.substring(0,str2.length() - 3);
		str2 = String.valueOf(LocalWeekDate.getCode(str2));
		
		return  str1+str2+str3;
	}
	
	/**
	 * 根据日期查询比赛结果
	 * @param dateStr
	 * @return
	 */
	public List<LotteryMatchDTO> queryMatchResult(String dateStr){
		List<LotteryMatch> lotteryMatchList = lotteryMatchMapper.queryMatchByDate(dateStr);
		List<LotteryMatchDTO> lotteryMatchDTOList = new ArrayList<LotteryMatchDTO>();
//		if(CollectionUtils.isEmpty(lotteryMatchList)) {
//			return lotteryMatchDTOList;
//		}
//		
//		lotteryMatchList.forEach(s->{
//			LotteryMatchDTO  lotteryMatchDTO = new LotteryMatchDTO();
//			BeanUtils.copyProperties(s, lotteryMatchDTO);
//			lotteryMatchDTO.setMatchTime(DateUtil.getYMD(s.getMatchTime()));
//			lotteryMatchDTOList.add(lotteryMatchDTO);
//		});
		
		return lotteryMatchDTOList;
	}
	
	
}

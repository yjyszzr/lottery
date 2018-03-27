package com.dl.shop.lottery.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.sql.visitor.functions.Substring;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.NetWorkUtil;
import com.dl.dto.DlJcZqMatchCellDTO;
import com.dl.dto.DlJcZqMatchDTO;
import com.dl.dto.DlJcZqMatchListDTO;
import com.dl.dto.DlJcZqMatchPlayDTO;
import com.dl.enums.MatchPlayTypeEnum;
import com.dl.param.DlJcZqMatchListParam;
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
		List<DlJcZqMatchPlayDTO> matchPlayDTOList = lotteryMatchMapper.getMatchList(param.getPlayType());
		Map<String, DlJcZqMatchDTO> map = new HashMap<String, DlJcZqMatchDTO>();
		matchPlayDTOList.forEach(dto->{
			//页面展示日期
			Date matchTime = dto.getMatchTime();
			LocalDate localDate = LocalDateTime.ofInstant(matchTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
			DayOfWeek dayOfWeek = localDate.getDayOfWeek();
			int value = dayOfWeek.getValue();
			String name = LocalWeekDate.getName(value);
			String matchDate = 	localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
			String matchDay = name + matchDate;
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
			dlJcZqMatchDTO.getPlayList().add(dto);
			if(dto.getIsHot() == 1) {
				dlJcZqMatchListDTO.getHotPlayList().add(dto);
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
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");
					Date machTime = sdf.parse(machtimeStr);
					lotteryMatch.setMatchTime(machTime);
					sdf.applyPattern("yyyy-mm-dd");
					Date showTime = sdf.parse(jo.getString("b_date"));
					lotteryMatch.setShowTime(showTime);
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
	
}

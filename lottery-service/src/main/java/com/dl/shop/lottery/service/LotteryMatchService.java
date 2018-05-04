package com.dl.shop.lottery.service;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.MatchPlayTypeEnum;
import com.dl.base.enums.MatchResultCrsEnum;
import com.dl.base.enums.MatchResultHadEnum;
import com.dl.base.enums.MatchResultHafuEnum;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.NetWorkUtil;
import com.dl.base.util.SNGenerator;
import com.dl.lottery.dto.DLBetMatchCellDTO;
import com.dl.lottery.dto.DLZQBetInfoDTO;
import com.dl.lottery.dto.DLZQOrderLotteryBetInfoDTO;
import com.dl.lottery.dto.DlJcZqDateMatchDTO;
import com.dl.lottery.dto.DlJcZqMatchCellDTO;
import com.dl.lottery.dto.DlJcZqMatchDTO;
import com.dl.lottery.dto.DlJcZqMatchListDTO;
import com.dl.lottery.dto.DlJcZqMatchPlayDTO;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.lottery.dto.LotteryMatchDTO;
import com.dl.lottery.dto.LotteryPrintDTO;
import com.dl.lottery.dto.MatchBetCellDTO;
import com.dl.lottery.dto.MatchBetPlayCellDTO;
import com.dl.lottery.dto.MatchBetPlayDTO;
import com.dl.lottery.dto.MatchInfoDTO;
import com.dl.lottery.dto.MatchInfoForTeamDTO;
import com.dl.lottery.dto.MatchTeamInfoDTO;
import com.dl.lottery.dto.MatchTeamInfosDTO;
import com.dl.lottery.enums.LotteryResultEnum;
import com.dl.lottery.param.DlJcZqMatchBetParam;
import com.dl.lottery.param.DlJcZqMatchListParam;
import com.dl.lottery.param.DlToAwardingParam;
import com.dl.lottery.param.QueryMatchParam;
import com.dl.order.api.IOrderDetailService;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.IssueDTO;
import com.dl.order.dto.OrderDetailDataDTO;
import com.dl.order.dto.OrderInfoAndDetailDTO;
import com.dl.order.dto.OrderInfoDTO;
import com.dl.order.param.DateStrParam;
import com.dl.order.param.LotteryPrintRewardParam;
import com.dl.shop.lottery.core.LocalWeekDate;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.DlLeagueTeamMapper;
import com.dl.shop.lottery.dao.LotteryMatchMapper;
import com.dl.shop.lottery.dao.LotteryMatchPlayMapper;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.model.DlLeagueTeam;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.model.LotteryMatchPlay;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.utils.PlayTypeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LotteryMatchService extends AbstractService<LotteryMatch> {
    
	private final static Logger logger = Logger.getLogger(LotteryMatchService.class);
	@Resource
    private LotteryMatchMapper lotteryMatchMapper;
	
	@Resource
	private LotteryMatchPlayMapper lotteryMatchPlayMapper;
	
	@Resource
	private LotteryPrintMapper lotteryPrintMapper;
	
	@Resource
	private DlLeagueInfoService leagueInfoService;
	
	@Resource
	private IOrderDetailService orderDetailService;
	
	@Resource
	private DlLeagueTeamMapper leagueTeamMapper;
	
	@Resource
	private LotteryRewardService lotteryRewardService;
	
	@Resource
	private DlLeagueMatchResultService matchResultService;
	
	@Resource
	private IOrderService orderService;
	
	
	@Value("${match.url}")
	private String matchUrl;
	
	private final static String MATCH_RESULT_OVER = "已完成";

    /**
     * 获取赛事列表
     * @param param
     * @return
     */
	public DlJcZqMatchListDTO getMatchList(DlJcZqMatchListParam param) {
		DlJcZqMatchListDTO dlJcZqMatchListDTO = new DlJcZqMatchListDTO();
		List<LotteryMatch> matchList = lotteryMatchMapper.getMatchList(param.getLeagueId());
		if(matchList == null || matchList.size() == 0) {
			return dlJcZqMatchListDTO;
		}
		List<Integer> matchIds = matchList.stream().map(match->match.getMatchId()).collect(Collectors.toList());
		String playType = param.getPlayType();
		List<LotteryMatchPlay> matchPlayList = lotteryMatchPlayMapper.matchPlayListByMatchIds(matchIds.toArray(new Integer[matchIds.size()]), "6".equals(playType)?"":playType);
		Map<Integer, List<DlJcZqMatchPlayDTO>> matchPlayMap = new HashMap<Integer, List<DlJcZqMatchPlayDTO>>();
		for(LotteryMatchPlay matchPlay: matchPlayList) {
			Integer playType2 = matchPlay.getPlayType();
			if("6".equals(playType) && playType2 == 7) {
				continue;
			}
			Integer matchId = matchPlay.getMatchId();
			DlJcZqMatchPlayDTO matchPlayDto = new DlJcZqMatchPlayDTO();
			matchPlayDto.setPlayContent(matchPlay.getPlayContent());
			matchPlayDto.setPlayType(playType2);
			initDlJcZqMatchCell(matchPlayDto);
			matchPlayDto.setPlayContent(null);
			List<DlJcZqMatchPlayDTO> dlJcZqMatchPlayDTOs = matchPlayMap.get(matchId);
			if(dlJcZqMatchPlayDTOs == null){
				dlJcZqMatchPlayDTOs = new ArrayList<DlJcZqMatchPlayDTO>();
				matchPlayMap.put(matchId, dlJcZqMatchPlayDTOs);
			}
			dlJcZqMatchPlayDTOs.add(matchPlayDto);
		}
		Map<String, DlJcZqDateMatchDTO> map = new HashMap<String, DlJcZqDateMatchDTO>();
		Integer totalNum = 0;
		Map<Integer, LeagueInfoDTO> leagueInfoMap = new HashMap<Integer, LeagueInfoDTO>();
		for(LotteryMatch match: matchList) {
			DlJcZqMatchDTO matchDto = new DlJcZqMatchDTO();
			Date matchTimeDate = match.getMatchTime();
			Instant instant = matchTimeDate.toInstant();
			int matchTime = Long.valueOf(instant.getEpochSecond()).intValue();
//			LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
			int betEndTime = matchTime - ProjectConstant.BET_PRESET_TIME;
			LocalDateTime betendDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(betEndTime), ZoneId.systemDefault());
			if(betendDateTime.toLocalDate().isAfter(LocalDate.now())) {
				betEndTime = Long.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)).toInstant(ZoneOffset.UTC).getEpochSecond()).intValue();
			}
			matchDto.setBetEndTime(betEndTime);
			matchDto.setChangci(match.getChangci());
			matchDto.setChangciId(match.getChangciId().toString());
			matchDto.setHomeTeamAbbr(match.getHomeTeamAbbr());
			matchDto.setHomeTeamId(match.getHomeTeamId());
			matchDto.setHomeTeamName(match.getHomeTeamName());
			matchDto.setHomeTeamRank(match.getHomeTeamRank());
			matchDto.setIsHot(match.getIsHot());
			matchDto.setLeagueAddr(match.getLeagueAddr());
			matchDto.setLeagueId(match.getLeagueId().toString());
			matchDto.setLeagueName(match.getLeagueName());
			/*String matchDate = 	localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
			DayOfWeek dayOfWeek = localDate.getDayOfWeek();
			int value = dayOfWeek.getValue();
			String name = LocalWeekDate.getName(value);
			String matchDay = name + matchDate;
			if(LocalDate.now().isEqual(localDate)) {
				matchDay = "今日 " + matchDate;
			}*/
			String matchDay =LocalDateTime.ofInstant(match.getShowTime().toInstant(), ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
			matchDto.setMatchDay(matchDay);
			matchDto.setMatchId(match.getMatchId());
			matchDto.setMatchTime(matchTime);
			matchDto.setPlayCode(match.getMatchSn());
			matchDto.setPlayType(Integer.parseInt(playType));
			matchDto.setVisitingTeamAbbr(match.getVisitingTeamAbbr());
			matchDto.setVisitingTeamId(match.getVisitingTeamId().toString());
			matchDto.setVisitingTeamName(match.getVisitingTeamName());
			matchDto.setVisitingTeamRank(match.getVisitingTeamRank());
			List<DlJcZqMatchPlayDTO> matchPlays = matchPlayMap.get(match.getMatchId());
			if(matchPlays == null || matchPlays.size() == 0) {
				continue;
			}
			if(null == leagueInfoMap.get(match.getLeagueId())) {
				LeagueInfoDTO leagueInfo = new LeagueInfoDTO();
				leagueInfo.setLeagueAddr(match.getLeagueAddr());
				leagueInfo.setLeagueId(match.getLeagueId());
				leagueInfo.setLeagueName(match.getLeagueName());
				leagueInfoMap.put(match.getLeagueId(), leagueInfo);
			}
			
			if("6".equals(playType) && matchPlays.size() < 5) {
				List<Integer> collect = matchPlays.stream().map(dto->dto.getPlayType()).collect(Collectors.toList());
				for(int i=1; i< 6; i++) {
					if(!collect.contains(i)) {
						DlJcZqMatchPlayDTO dto = new DlJcZqMatchPlayDTO();
						dto.setPlayType(i);
						dto.setIsShow(0);
						matchPlays.add(dto);
					}
				}
			}
			matchPlays.sort((item1,item2)->item1.getPlayType().compareTo(item2.getPlayType()));
			matchDto.setMatchPlays(matchPlays);
			//
			DlJcZqDateMatchDTO dlJcZqMatchDTO = map.get(matchDay);
			if(null == dlJcZqMatchDTO) {
				dlJcZqMatchDTO = new DlJcZqDateMatchDTO();
				dlJcZqMatchDTO.setMatchDay(matchDay);
				map.put(matchDay, dlJcZqMatchDTO);
			}
			//初始化投注选项
			if(matchDto.getIsHot() == 1) {
				dlJcZqMatchListDTO.getHotPlayList().add(matchDto);
			} else {
				dlJcZqMatchDTO.getPlayList().add(matchDto);
			}
			totalNum++;
		}
		map.forEach((key, value) ->{
			dlJcZqMatchListDTO.getPlayList().add(value);
		});
		leagueInfoMap.forEach((key,value)->{
			dlJcZqMatchListDTO.getLeagueInfos().add(value);
		});
		dlJcZqMatchListDTO.getHotPlayList().sort((item1,item2)->(item1.getMatchTime() < item2.getMatchTime()) ? -1 : ((item1.getMatchTime() == item2.getMatchTime()) ? 0 : 1));
		dlJcZqMatchListDTO.getPlayList().sort((item1,item2)->item1.getMatchDay().compareTo(item2.getMatchDay()));
		dlJcZqMatchListDTO.setAllMatchCount(totalNum.toString());
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
		dto.setHomeCell(new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_H.getCode().toString(), MatchResultHadEnum.HAD_H.getMsg(), hOdds));
		dto.setFlatCell(new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_D.getCode().toString(), MatchResultHadEnum.HAD_D.getMsg(), dOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_A.getCode().toString(), MatchResultHadEnum.HAD_A.getMsg(), aOdds));
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
		dto.setHomeCell(new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_H.getCode().toString(), MatchResultHadEnum.HAD_H.getMsg(), hOdds));
		dto.setFlatCell(new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_D.getCode().toString(), MatchResultHadEnum.HAD_D.getMsg(), dOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_A.getCode().toString(), MatchResultHadEnum.HAD_A.getMsg(), aOdds));
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
		DlJcZqMatchCellDTO homeCell = new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_H.getCode().toString(), MatchResultHadEnum.HAD_H.getMsg(), null);
		homeCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(10));
		dto.setHomeCell(homeCell);
		DlJcZqMatchCellDTO flatCell = new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_D.getCode().toString(), MatchResultHadEnum.HAD_D.getMsg(), null);
		flatCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(10));
		dto.setFlatCell(flatCell);
		DlJcZqMatchCellDTO visitingCell = new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_A.getCode().toString(), MatchResultHadEnum.HAD_A.getMsg(), null);
		visitingCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(10));
		dto.setVisitingCell(visitingCell);
		//List<DlJcZqMatchCellDTO> matchCells = new ArrayList<DlJcZqMatchCellDTO>();
		String regex = "^0\\d{3}$";
		for(String key: keySet) {
			if(Pattern.matches(regex, key)) {
				String code = String.valueOf(new char[] {key.charAt(1),key.charAt(3)});
				String odds = jsonObj.getString(key);
				String name = MatchResultCrsEnum.getName(code);
				if(StringUtils.isBlank(name)) {
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
		homeCell.getCellSons().sort((cell1,cell2)->cell1.getCellCode().compareTo(cell2.getCellCode()));
		visitingCell.getCellSons().sort((cell1,cell2)->cell1.getCellCode().compareTo(cell2.getCellCode()));
		flatCell.getCellSons().sort((cell1,cell2)->cell1.getCellCode().compareTo(cell2.getCellCode()));
		String hOdds = jsonObj.getString("-1-h");
		homeCell.getCellSons().add(new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_90.getCode(), MatchResultCrsEnum.CRS_90.getMsg(), hOdds));
		String aOdds = jsonObj.getString("-1-a");
		visitingCell.getCellSons().add(new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_09.getCode(), MatchResultCrsEnum.CRS_09.getMsg(), aOdds));
		String dOdds = jsonObj.getString("-1-d");
		flatCell.getCellSons().add(new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_99.getCode(), MatchResultCrsEnum.CRS_99.getMsg(), dOdds));
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
				String name = String.valueOf(new char[] {key.charAt(1)});
				if("7".equals(code)) {
					name += "+";
				} 
				matchCells.add(new DlJcZqMatchCellDTO(code, name, odds));
			}
		}
		matchCells.sort((cell1,cell2)->cell1.getCellCode().compareTo(cell2.getCellCode()));
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
		matchCells.add(new DlJcZqMatchCellDTO(MatchResultHafuEnum.HAFU_HH.getCode(), MatchResultHafuEnum.HAFU_HH.getMsg(), hhOdds));
		String hdOdds = jsonObj.getString("hd");
		matchCells.add(new DlJcZqMatchCellDTO(MatchResultHafuEnum.HAFU_HD.getCode(), MatchResultHafuEnum.HAFU_HD.getMsg(), hdOdds));
		String haOdds = jsonObj.getString("ha");
		matchCells.add(new DlJcZqMatchCellDTO(MatchResultHafuEnum.HAFU_HA.getCode(), MatchResultHafuEnum.HAFU_HA.getMsg(), haOdds));
		String ddOdds = jsonObj.getString("dd");
		matchCells.add(new DlJcZqMatchCellDTO(MatchResultHafuEnum.HAFU_DD.getCode(), MatchResultHafuEnum.HAFU_DD.getMsg(), ddOdds));
		String daOdds = jsonObj.getString("da");
		matchCells.add(new DlJcZqMatchCellDTO(MatchResultHafuEnum.HAFU_DA.getCode(), MatchResultHafuEnum.HAFU_DA.getMsg(), daOdds));
		String dhOdds = jsonObj.getString("dh");
		matchCells.add(new DlJcZqMatchCellDTO(MatchResultHafuEnum.HAFU_DH.getCode(), MatchResultHafuEnum.HAFU_DH.getMsg(), dhOdds));
		String aaOdds = jsonObj.getString("aa");
		matchCells.add(new DlJcZqMatchCellDTO(MatchResultHafuEnum.HAFU_AA.getCode(), MatchResultHafuEnum.HAFU_AA.getMsg(), aaOdds));
		String adOdds = jsonObj.getString("ad");
		matchCells.add(new DlJcZqMatchCellDTO(MatchResultHafuEnum.HAFU_AD.getCode(), MatchResultHafuEnum.HAFU_AD.getMsg(), adOdds));
		String ahOdds = jsonObj.getString("ah");
		matchCells.add(new DlJcZqMatchCellDTO(MatchResultHafuEnum.HAFU_AH.getCode(), MatchResultHafuEnum.HAFU_AH.getMsg(), ahOdds));
		matchCells.sort((cell1,cell2)->cell1.getCellCode().compareTo(cell2.getCellCode()));
		dto.setMatchCells(matchCells);
	}
	private void initDlJcZqMatchCell6(DlJcZqMatchPlayDTO dto) {
		
	}
	private void initDlJcZqMatchCell7(DlJcZqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		String zbbOdds = jsonObj.getString("zbb");
		if(StringUtils.isNotBlank(zbbOdds)) {
			dto.setHomeCell(new DlJcZqMatchCellDTO("32", "主不败", zbbOdds));
		}
		String zbOdds = jsonObj.getString("zb");
		if(StringUtils.isNotBlank(zbOdds)) {
			dto.setVisitingCell(new DlJcZqMatchCellDTO("30", "主败", zbOdds));
		}
		String zsOdds = jsonObj.getString("zs");
		if(StringUtils.isNotBlank(zsOdds)) {
			dto.setHomeCell(new DlJcZqMatchCellDTO("31", "主胜", zsOdds));
		}
		String zbsOdds = jsonObj.getString("zbs");
		if(StringUtils.isNotBlank(zbsOdds)) {
			dto.setVisitingCell(new DlJcZqMatchCellDTO("33", "主不胜", zbsOdds));
		}
		dto.setSingle(0);
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
					tsoJo.put("zbb", hhadJo.getString("h"));
					tsoJo.put("zb", hadJo.getString("a"));
					flag = true;
				} else if(fixedodds.equals("-1")) {
					tsoJo.put("zbs", hhadJo.getString("a"));
					tsoJo.put("zs", hadJo.getString("h"));
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
					lotteryMatch.setMatchSn(this.commonCreateIssue(jo.getString("b_date"), lotteryMatch.getChangci()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				lotteryMatch.setCreateTime(DateUtil.getCurrentTimeLong());
				lotteryMatch.setIsShow(ProjectConstant.IS_SHOW);
				lotteryMatch.setIsDel(ProjectConstant.IS_NOT_DEL);
				lotteryMatch.setIsHot(jo.getInteger("hot"));
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
				boolean isInsert = this.saveLotteryMatch(lotteryMatch);
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
						this.saveLotteryMatchPlays(lotteryMatchPlays, isInsert);
					}
				}
			}
		}
	}

	private void saveLotteryMatchPlays(List<LotteryMatchPlay> lotteryMatchPlays, boolean isInsert) {
		if(isInsert) {
			lotteryMatchPlayMapper.insertList(lotteryMatchPlays);
		}else {
			for(LotteryMatchPlay play: lotteryMatchPlays) {
				lotteryMatchPlayMapper.updatePlayContent(play);
			}
		}
	}

	/**
	 * 保存赛事对象
	 * @param lotteryMatch
	 */
	private boolean saveLotteryMatch(LotteryMatch lotteryMatch) {
		LotteryMatch byChangciId = lotteryMatchMapper.getByChangciId(lotteryMatch.getChangciId());
		if(null == byChangciId) {
			lotteryMatchMapper.insertMatch(lotteryMatch);
			return true;
		}else {
			lotteryMatch.setMatchId(byChangciId.getMatchId());
			return false;
		}
	}
	
	/**
	 * 计算组合
	 * @param str
	 * @param num
	 * @param list
	 * @param betList
	 */
	private void betNum(DLBetMatchCellDTO str, int num, List<MatchBetPlayCellDTO> list, List<DLBetMatchCellDTO> betList) {
		LinkedList<MatchBetPlayCellDTO> link = new LinkedList<MatchBetPlayCellDTO>(list);
		while(link.size() > 0) {
			MatchBetPlayCellDTO remove = link.remove(0);
			String changci = remove.getChangci();
			String playCode = remove.getPlayCode();
			List<DlJcZqMatchCellDTO> betCells = remove.getBetCells();
			for(DlJcZqMatchCellDTO betCell: betCells) {
				String cellCode = betCell.getCellCode();
				DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
				String playType = remove.getPlayType();
				dto.setPlayType(playType);
				Double amount = str.getAmount()*Double.valueOf(betCell.getCellOdds());
				dto.setAmount(Double.valueOf(String.format("%.2f", amount)));
				String betContent = str.getBetContent() + changci + "(" + betCell.getCellName() + " " + betCell.getCellOdds() +")X";
				/*StringBuffer sbuf = new StringBuffer();
				if(Integer.valueOf(playType).equals(MatchPlayTypeEnum.PLAY_TYPE_TSO.getcode())) {
					if("30".equals(cellCode)) {
						sbuf.append("02|").append(playCode).append("|0").append(";");
					}else if("31".equals(cellCode)) {
						sbuf.append("02|").append(playCode).append("|3").append(";");
					}else if("32".equals(cellCode)) {
						sbuf.append("01|").append(playCode).append("|3,1").append(";");
					}else if("33".equals(cellCode)) {
						sbuf.append("01|").append(playCode).append("|0,1").append(";");
					}
				}else {
					sbuf.append("0").append(playType).append("|").append(playCode).append("|").append(cellCode);
				}
				String betStakes = str.getBetStakes() + sbuf.toString();*/
				 
				if(num == 1) {
					betContent = betContent.substring(0, betContent.length()-1);
//					betStakes = betStakes.substring(0, betStakes.length()-1);
				}
//				dto.setBetStakes(betStakes);
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
	 * 
	 * @param playCellMap
	 * @param list
	 * @param dtos
	 * @param result
	 */
	private void matchBetPlayCellsForLottery(int num, Map<String, List<MatchBetPlayCellDTO>> playCellMap, List<String> list, List<MatchBetPlayCellDTO> dtos, List<List<MatchBetPlayCellDTO>> result) {
		LinkedList<String> link = new LinkedList<String>(list);
		while(link.size() > 0) {
			String key = link.remove(0);
			List<MatchBetPlayCellDTO> playCellDTOs = playCellMap.get(key);
			for(MatchBetPlayCellDTO dto: playCellDTOs) {
				List<MatchBetPlayCellDTO> playCells = new ArrayList<MatchBetPlayCellDTO>();
				playCells.addAll(dtos);
				playCells.add(dto);
				if(num == 1) {
					playCells.sort((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode()));
					result.add(playCells);
				}else {
					matchBetPlayCellsForLottery(num-1, playCellMap, link, playCells, result);
				}
			}
		}
	}
	
	
	/**
	 * 投注信息获取
	 * @param param
	 * @return
	 */
	public DLZQBetInfoDTO getBetInfo(DlJcZqMatchBetParam param) {
		List<MatchBetPlayDTO> matchBellCellList = param.getMatchBetPlays();
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
		List<List<MatchBetPlayCellDTO>>  matchBetList = new ArrayList<List<MatchBetPlayCellDTO>>();
		List<DLZQOrderLotteryBetInfoDTO> orderLotteryBetInfos = new ArrayList<DLZQOrderLotteryBetInfoDTO>();
		List<DLBetMatchCellDTO> betCellList = new ArrayList<DLBetMatchCellDTO>();
		List<DLBetMatchCellDTO> maxBetCellList = new ArrayList<DLBetMatchCellDTO>();
//		List<DLBetMatchCellDTO> minBetCellList = new ArrayList<DLBetMatchCellDTO>();
		String betTypes = param.getBetType();
		String[] split = betTypes.split(",");
		Map<String, List<String>> indexMap = new HashMap<String, List<String>>();
//		int betNums = 0;
		for(String betType: split) {
			char[] charArray = betType.toCharArray();
			if(charArray.length == 2 && charArray[1] == '1') {
				int num = Integer.valueOf(String.valueOf(charArray[0]));
				//计算场次组合
				List<String> betIndexList = new ArrayList<String>();
				betNum1("", num, indexList, betIndexList);
				if(danIndexList.size() > 0) {
					String danIndexStr = danIndexList.stream().collect(Collectors.joining(","));
					betIndexList = betIndexList.stream().filter(item->{
						for(String danIndex: danIndexList) {
							if(!item.contains(danIndex)) {
								return false;
							}
						}
						return true;
					}).collect(Collectors.toList());
				}
				indexMap.put(betType, betIndexList);
//				betNums += betIndexList.size();
			}
		}
		//
		Map<String, List<List<MatchBetPlayCellDTO>>> betPlayCellMap = new HashMap<String, List<List<MatchBetPlayCellDTO>>>();
		for(String betType: indexMap.keySet()) {
			List<String> betIndexList = indexMap.get(betType);
			List<List<MatchBetPlayCellDTO>> result = new ArrayList<List<MatchBetPlayCellDTO>>(betIndexList.size());
			for(String str: betIndexList) {
				String[] strArr = str.split(",");
				List<String> playCodes = new ArrayList<String>(strArr.length);
				Map<String, List<MatchBetPlayCellDTO>> playCellMap = new HashMap<String, List<MatchBetPlayCellDTO>>();
//				List<MatchBetPlayDTO> subList = new ArrayList<MatchBetPlayDTO>(strArr.length);
				Arrays.asList(strArr).stream().forEach(item->{
					MatchBetPlayDTO betPlayDto = matchBellCellList.get(Integer.valueOf(item));
					String playCode = betPlayDto.getPlayCode();
					List<MatchBetCellDTO> matchBetCells = betPlayDto.getMatchBetCells();
					List<MatchBetPlayCellDTO> list = playCellMap.get(playCode);
					if(list == null) {
						list = new ArrayList<MatchBetPlayCellDTO>(matchBetCells.size());
						playCellMap.put(playCode, list);
						playCodes.add(playCode);
					}
					for(MatchBetCellDTO cell: matchBetCells) {
						MatchBetPlayCellDTO playCellDto = new MatchBetPlayCellDTO(betPlayDto);
						playCellDto.setPlayType(cell.getPlayType());
						playCellDto.setBetCells(cell.getBetCells());
						list.add(playCellDto);
					}
				});
				List<MatchBetPlayCellDTO> dtos = new ArrayList<MatchBetPlayCellDTO>(0);
				matchBetPlayCellsForLottery(playCodes.size(), playCellMap, playCodes, dtos, result);
			}
			betPlayCellMap.put(betType, result);
		}
		for(String betType: betPlayCellMap.keySet()) {
			char[] charArray = betType.toCharArray();
			int num = Integer.valueOf(String.valueOf(charArray[0]));
			List<List<MatchBetPlayCellDTO>> betIndexList = betPlayCellMap.get(betType);
			for(List<MatchBetPlayCellDTO> subList: betIndexList) {
				List<MatchBetPlayCellDTO> maxList = new ArrayList<MatchBetPlayCellDTO>(subList.size());
//				List<MatchBetPlayCellDTO> minList = new ArrayList<MatchBetPlayCellDTO>(subList.size());
				subList.stream().forEach(matchBetCell->{
					MatchBetPlayCellDTO maxBetCell = maxOrMinOddsCell(matchBetCell, true);
//					MatchBetPlayCellDTO minBetCell = maxOrMinOddsCell(matchBetCell, false);
					maxList.add(maxBetCell);
//					minList.add(minBetCell);
				});
				DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
				dto.setBetType(betType);
				dto.setTimes(param.getTimes());
				dto.setBetContent("");
				dto.setBetStakes("");
				dto.setAmount(2.0*param.getTimes());
				List<DLBetMatchCellDTO> betCellList1 = new ArrayList<DLBetMatchCellDTO>();
				List<DLBetMatchCellDTO> maxBetCellList1 = new ArrayList<DLBetMatchCellDTO>();
//				List<DLBetMatchCellDTO> minBetCellList1 = new ArrayList<DLBetMatchCellDTO>();
				betNum(dto, num, subList, betCellList1);
				betNum(dto, num, maxList, maxBetCellList1);
//				betNum(dto, num, minList, minBetCellList1);
				matchBetList.add(subList);
				betCellList.addAll(betCellList1);
				maxBetCellList.addAll(maxBetCellList1);
//				minBetCellList.addAll(minBetCellList1);
				String playType1 = subList.get(0).getPlayType();
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
				String playType = param.getPlayType();
				LotteryPrintDTO lotteryPrintDTO = new LotteryPrintDTO();
				lotteryPrintDTO.setBetType(betType);
				lotteryPrintDTO.setIssue(issue);
				lotteryPrintDTO.setMoney(money);
				lotteryPrintDTO.setPlayType(playType);
				lotteryPrintDTO.setStakes(stakes);
				String ticketId = SNGenerator.nextSN(SNBusinessCodeEnum.TICKET_SN.getCode());
				lotteryPrintDTO.setTicketId(ticketId);
				orderLotteryBetInfos.add(new DLZQOrderLotteryBetInfoDTO(stakes, betCellList1));
				lotteryPrintDTO.setTimes(times);
				lotteryPrints.add(lotteryPrintDTO);
			}
		}
		Double maxBonus = maxBetCellList.stream().map(item->{
			return item.getAmount();
		}).reduce(0.0, Double::sum);
		/*Double minBonus = minBetCellList.stream().map(item->{
			return item.getAmount();
		}).reduce(0.0, Double::sum);*/
		Double minBonus  = betCellList.get(0).getAmount();
		if(betCellList.size() > 1) {
			minBonus  = betCellList.stream().min((item1,item2)->item1.getAmount().compareTo(item2.getAmount())).get().getAmount();
		}
		//页面返回信息对象
		DLZQBetInfoDTO betInfoDTO = new DLZQBetInfoDTO();
		betInfoDTO.setMaxBonus(Double.valueOf(String.format("%.2f", maxBonus)));
		betInfoDTO.setMinBonus(Double.valueOf(String.format("%.2f", minBonus)));
		betInfoDTO.setTimes(param.getTimes());
		betInfoDTO.setBetNum(betCellList.size());
		betInfoDTO.setTicketNum(lotteryPrints.size());
		Double money = betCellList.size()*param.getTimes()*2.0;
		betInfoDTO.setMoney(Double.valueOf(String.format("%.2f", money)));
		betInfoDTO.setBetType(param.getBetType());
		betInfoDTO.setPlayType(param.getPlayType());
		betInfoDTO.setBetCells(orderLotteryBetInfos);//投注方案
		betInfoDTO.setLotteryPrints(lotteryPrints);
		/*String stakes = matchBellCellList.stream().map(item->{
			String cellCodes = item.getBetCells().stream().map(cell->cell.getCellCode()).collect(Collectors.joining(","));
			return item.getPlayType() +"|" + item.getPlayCode() + "|" + cellCodes;
		}).collect(Collectors.joining(";"));
		betInfoDTO.setStakes(stakes);*/
		//所有选项的最后一个场次编码
		String issue = matchBellCellList.stream().max((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode())).get().getPlayCode();
		betInfoDTO.setIssue(issue);
//		betInfoDTO.setMatchBetList(matchBetList);
		return betInfoDTO;
	}
	/**
	 * 获取最大最小赔率的对象
	 * @param matchBetCell
	 * @param maxFlag
	 * @return
	 */
	private MatchBetPlayCellDTO maxOrMinOddsCell(MatchBetPlayCellDTO matchBetCell, boolean maxFlag) {
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
		MatchBetPlayCellDTO maxBetCell = new MatchBetPlayCellDTO();
		maxBetCell.setLotteryClassifyId(matchBetCell.getLotteryClassifyId());
		maxBetCell.setLotteryPlayClassifyId(matchBetCell.getLotteryPlayClassifyId());
		maxBetCell.setMatchId(matchBetCell.getMatchId());
		maxBetCell.setMatchTeam(matchBetCell.getMatchTeam());
		maxBetCell.setMatchTime(matchBetCell.getMatchTime());
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
	public void pullMatchResult() {
		List<LotteryMatch> matchList = lotteryMatchMapper.getMatchListUnknowScoreToday();
		if(CollectionUtils.isEmpty(matchList)) {
			return ;
		}
		log.info("准备拉取开赛结果 ： size=" + matchList.size());
		List<String> matchs = matchList.stream().map(match->{
			String changci = match.getChangci();
			Date matchTime = match.getMatchTime();
			LocalDate localDate = LocalDateTime.ofInstant(matchTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
			String matchDate = localDate.toString();
			return matchDate+"_" + changci+"_"+match.getMatchId() + "_" + match.getChangciId() + "_" + match.getMatchSn();
		}).collect(Collectors.toList());
		LotteryMatch minMatch = matchList.get(0);
		if(matchList.size() > 1) {
			minMatch = matchList.stream().min((item1,item2)->item1.getMatchTime().compareTo(item2.getMatchTime())).get();
		}
		Date miMatchTime = minMatch.getMatchTime();
		LocalDate localDate = LocalDateTime.ofInstant(miMatchTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
		String minMatchTimeStr = localDate.toString();
		log.info("minMatchTimeStr = "+minMatchTimeStr);
        Document doc = null;
        List<String> changciIds = new ArrayList<String>(matchs.size());
        List<String> issueList = new ArrayList<String>(matchs.size());
        List<LotteryMatch> matchResult = new ArrayList<LotteryMatch>(matchs.size());
        try {
            doc = Jsoup.connect("http://info.sporttery.cn/football/match_result.php?start_date="+minMatchTimeStr).get();
            Elements elementsByClass = doc.getElementsByClass("m-page");
            Elements pageLis = elementsByClass.get(0).select("tr td ul li");
            List<String> pageUrls = new ArrayList<String>();
            for(int i=2; i< (pageLis.size()-2); i++) {
            	String href = pageLis.get(i).select("a").attr("href");
            	pageUrls.add("http://info.sporttery.cn/football/" +href);
            }
            this.aa(matchs, doc, changciIds, issueList, matchResult);
            for(String url: pageUrls) {
            	doc = Jsoup.connect(url).get();
            	this.aa(matchs, doc, changciIds, issueList, matchResult);
            }
        } catch (Exception e) {
        	log.error(e.getMessage());
        }
	}

	private void aa(List<String> matchs, Document doc, List<String> changciIds, List<String> issueList,
			List<LotteryMatch> matchResult) {
		Elements elements =  doc.getElementsByClass("m-tab");
		Elements trs = elements.select("tbody tr");
		for (int i = 0; i < trs.size(); i++) {
			if(matchs.size() == 0) {
				break;
			}
			Elements tds = trs.get(i).select("td");
			if(null != tds && tds.size() == 12) {
				String status = tds.get(9).text();
				if(MATCH_RESULT_OVER.equals(status)) {
					String matchDate = tds.get(0).text();
					String changci = tds.get(1).text();
					String[] arr = this.matchId(matchs, matchDate, changci);
					if(null != arr) {
						String matchId = arr[2];
						String changciId = arr[3];
						String issue = arr[4];
						LotteryMatch lotteryMatch = new  LotteryMatch();
						String firstHalf = tds.get(4).text();
						String whole = tds.get(5).text();
						lotteryMatch.setMatchId(Integer.valueOf(matchId));
						lotteryMatch.setFirstHalf(firstHalf);
						lotteryMatch.setWhole(whole);
						lotteryMatch.setStatus(ProjectConstant.MATCH_FINISH);
						matchResult.add(lotteryMatch);
						changciIds.add(changciId);
						issueList.add(issue);
						/*log.info("保存比赛结果详情"+changciId);
						matchResultService.refreshMatchResultFromZC(Integer.valueOf(changciId));*/
						/*log.info("更新订单详情的赛事结果"+issue);
						LotteryPrintRewardParam lotteryPrintRewardParam = new LotteryPrintRewardParam();
						lotteryPrintRewardParam.setIssue(issue);
						int rstCode = orderService.updateOrderInfoByMatchResult(lotteryPrintRewardParam).getCode();
						if(rstCode != 0) {
							continue;
						}*/
						/*log.info("开奖场次："+issue);
						DlToAwardingParam dltoAwardingParm = new DlToAwardingParam();
						dltoAwardingParm.setIssue(issue);
						lotteryRewardService.toAwarding(dltoAwardingParm);*/
						log.info("保存比赛比分结果"+changciId);
						lotteryMatchMapper.updateMatchResult(lotteryMatch);
					}
				}
			}
		}
	}
	/**
	 * 通过获取的比赛结果信息获取matchid
	 * @return
	 */
	private String[] matchId(List<String> matchs, String matchDate, String changci) {
		for(String match: matchs) {
			String[] split = match.split("_");
			if(split.length == 5) {
				if(split[0].equals(matchDate) && split[1].equals(changci)){
					matchs.remove(match);
					return split;
				}
			}
		}
		return null;
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
	 * 根据查询条件查看比赛结果
	 * @param dateStr
	 * @return
	 */
	public BaseResult<List<LotteryMatchDTO>> queryMatchResult(QueryMatchParam queryMatchParam){
		List<LotteryMatchDTO> lotteryMatchDTOList = new ArrayList<LotteryMatchDTO>();
		if(!StringUtils.isEmpty(queryMatchParam.getIsAlreadyBuyMatch()) && !StringUtils.isEmpty(queryMatchParam.getLeagueIds())) {
			return ResultGenerator.genResult(LotteryResultEnum.ONLY_ONE_CONDITION.getCode(),LotteryResultEnum.ONLY_ONE_CONDITION.getMsg());
		} 
		
		String [] leagueIdArr = new String [] {};
		if(!StringUtils.isEmpty(queryMatchParam.getLeagueIds())) {
			leagueIdArr = queryMatchParam.getLeagueIds().split(",");
		}
		
		String[] issueArr = new String [] {};
		if(queryMatchParam.getIsAlreadyBuyMatch().equals("1")) {
			//我的订单中包含的今天赛事的期次号issue
			DateStrParam dateStrParam = new DateStrParam();
			dateStrParam.setDateStr(queryMatchParam.getDateStr());
			BaseResult<List<IssueDTO>> issuesDTO = orderDetailService.selectIssuesMatchInTodayOrder(dateStrParam);
			if(issuesDTO.getCode() != 0) {
				return ResultGenerator.genResult(issuesDTO.getCode(),issuesDTO.getMsg());
			}
			
			List<IssueDTO> issueDTOList = issuesDTO.getData();
			List<String> issueList = issueDTOList.stream().map(s->s.getIssue()).collect(Collectors.toList());
			if(issueList.size() > 0) {
				issueArr = (String[])issueList.toArray();
			}
		}
				
		List<LotteryMatch> lotteryMatchList = lotteryMatchMapper.queryMatchByQueryCondition(queryMatchParam.getDateStr(),
				issueArr,leagueIdArr,queryMatchParam.getMatchFinish());
		
		if(CollectionUtils.isEmpty(lotteryMatchList)) {
			return ResultGenerator.genSuccessResult("success", lotteryMatchDTOList);
		}
		
		lotteryMatchList.forEach(s->{
			LotteryMatchDTO  lotteryMatchDTO = new LotteryMatchDTO();
			BeanUtils.copyProperties(s, lotteryMatchDTO);
			lotteryMatchDTO.setMatchFinish(ProjectConstant.ONE_YES.equals(s.getStatus().toString())?ProjectConstant.ONE_YES:ProjectConstant.ZERO_NO);
			lotteryMatchDTO.setMatchTime(DateUtil.getYMD(s.getMatchTime()));
			lotteryMatchDTO.setChangci(s.getChangci().substring(2));
			lotteryMatchDTOList.add(lotteryMatchDTO);
		});
		
		return ResultGenerator.genSuccessResult("success", lotteryMatchDTOList);
	}
	
	@Transactional(readOnly=true)
	public DLZQBetInfoDTO getBetInfoByOrderInfo(OrderInfoAndDetailDTO  orderInfo, String orderSn) {
		OrderInfoDTO order = orderInfo.getOrderInfoDTO();
		List<OrderDetailDataDTO> selectByOrderId = orderInfo.getOrderDetailDataDTOs();
		List<MatchBetPlayDTO> matchBetPlays = selectByOrderId.stream().map(detail->{
    		String ticketData = detail.getTicketData();
    		String[] tickets = ticketData.split(";");
    		String playCode = null;
    		List<MatchBetCellDTO> matchBetCells = new ArrayList<MatchBetCellDTO>(tickets.length);
    		for(String tikcket: tickets) {
    			String[] split = tikcket.split("\\|");
    			if(split.length != 3) {
    				log.error("getBetInfoByOrderInfo ticket has error, orderSn="+orderSn+ " ticket="+tikcket);
    				continue;
    			}
    			String playType = split[0];
    			if(null == playCode) {
    				playCode = split[1];
    			}
    			String[] split2 = split[2].split(",");
    			List<DlJcZqMatchCellDTO> betCells = Arrays.asList(split2).stream().map(str->{
    				String[] split3 = str.split("@");
    				String matchResult = getCathecticData(split[0], split3[0]);
    				DlJcZqMatchCellDTO dto = new DlJcZqMatchCellDTO(split3[0], matchResult, split3[1]);
    				return dto;
    			}).collect(Collectors.toList());
    			MatchBetCellDTO matchBetCell = new MatchBetCellDTO();
    			matchBetCell.setPlayType(playType);
    			matchBetCell.setBetCells(betCells);
    			matchBetCells.add(matchBetCell);
    		}
    		MatchBetPlayDTO dto = new MatchBetPlayDTO();
    		dto.setChangci(detail.getChangci());
    		dto.setIsDan(detail.getIsDan());
    		dto.setLotteryClassifyId(detail.getLotteryClassifyId());
    		dto.setLotteryPlayClassifyId(detail.getLotteryPlayClassifyId());
    		dto.setMatchId(detail.getMatchId());
    		dto.setMatchTeam(detail.getMatchTeam());
    		Date matchTime = detail.getMatchTime();
    		dto.setMatchTime((int)matchTime.toInstant().getEpochSecond());
    		dto.setPlayCode(playCode);
    		dto.setMatchBetCells(matchBetCells);
    		return dto;
    	}).collect(Collectors.toList());
    	Integer times = order.getCathectic();
    	String betType = order.getPassType();
    	Integer lotteryClassifyId = order.getLotteryClassifyId();
    	Integer lotteryPlayClassifyId = order.getLotteryPlayClassifyId();
    	DlJcZqMatchBetParam param = new DlJcZqMatchBetParam();
    	param.setBetType(betType);
    	param.setLotteryClassifyId(lotteryClassifyId);
    	param.setLotteryPlayClassifyId(lotteryPlayClassifyId);
    	param.setPlayType(order.getPlayType());
    	param.setTimes(times);
    	param.setMatchBetPlays(matchBetPlays);
    	DLZQBetInfoDTO betInfo = this.getBetInfo(param);
    	List<DLZQOrderLotteryBetInfoDTO> betCells = betInfo.getBetCells();
    	List<LotteryPrint> byOrderSn = lotteryPrintMapper.getByOrderSn(orderSn);
    	betCells.forEach(betCell->{
    		String stakes = betCell.getStakes();
    		logger.info("DLZQOrderLotteryBetInfoDTO stakes: " + stakes+ " ordersn: "+ orderSn);
    		for(LotteryPrint lPrint: byOrderSn) {
    			if(stakes.equals(lPrint.getStakes())) {
    				betCell.setStatus(lPrint.getStatus());
    				logger.info("DLZQOrderLotteryBetInfoDTO stakes: " + stakes + " ordersn: "+ orderSn+" lPrint:"+ lPrint.getTicketId() + "  status:"+lPrint.getStatus());
    				logger.info("betCell status:" + betCell.getStatus());
    				break;
    			}
    		}
    	});
    	return betInfo;
	}
	/**
     * 通过玩法code与投注内容，进行转换
     * @param playCode
     * @param cathecticStr
     * @return
     */
    private String getCathecticData(String playType, String cathecticStr) {
    	int playCode = Integer.parseInt(playType);
    	String cathecticData = "";
    	if(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode() == playCode
    		|| MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode() == playCode) {
    		cathecticData = MatchResultHadEnum.getName(Integer.valueOf(cathecticStr));
    	} else if(MatchPlayTypeEnum.PLAY_TYPE_CRS.getcode() == playCode) {
    		cathecticData = MatchResultCrsEnum.getName(cathecticStr);
    	} else if(MatchPlayTypeEnum.PLAY_TYPE_TTG.getcode() == playCode) {
    		cathecticData = cathecticStr;
    	} else if(MatchPlayTypeEnum.PLAY_TYPE_HAFU.getcode() == playCode) {
    		cathecticData = MatchResultHafuEnum.getName(cathecticStr);
    	}
    	return cathecticData;
    }

    /**
     * 
     * @param matchId
     * @return
     */
    public MatchInfoForTeamDTO LotteryMatchForTeam(LotteryMatch lotteryMatch) {
    	MatchInfoForTeamDTO matchInfo = new MatchInfoForTeamDTO();
    	matchInfo.setChangci(lotteryMatch.getChangci());
    	matchInfo.setChangciId(lotteryMatch.getChangciId());
    	matchInfo.setHomeTeamAbbr(lotteryMatch.getHomeTeamAbbr());
    	Integer homeTeamId = lotteryMatch.getHomeTeamId();
		matchInfo.setHomeTeamId(homeTeamId);
    	matchInfo.setLeagueAddr(lotteryMatch.getLeagueAddr());
    	matchInfo.setMatchId(lotteryMatch.getMatchId());
    	Date matchTimeDate = lotteryMatch.getMatchTime();
		Instant instant = matchTimeDate.toInstant();
		int matchTime = Long.valueOf(instant.getEpochSecond()).intValue();
    	matchInfo.setMatchTime(matchTime);
    	matchInfo.setVisitingTeamAbbr(lotteryMatch.getVisitingTeamAbbr());
    	Integer visitingTeamId = lotteryMatch.getVisitingTeamId();
		matchInfo.setVisitingTeamId(visitingTeamId);
    	List<LotteryMatchPlay> matchPlayList = lotteryMatchPlayMapper.matchPlayListByMatchIds(new Integer[] {lotteryMatch.getMatchId()}, MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode()+"");
    	if(CollectionUtils.isNotEmpty(matchPlayList)) {
    		LotteryMatchPlay lotteryMatchPlay = matchPlayList.get(0);
    		String playContent = lotteryMatchPlay.getPlayContent();
    		JSONObject jsonObj = JSON.parseObject(playContent);
    		String hOdds = jsonObj.getString("h");
    		String dOdds = jsonObj.getString("d");
    		String aOdds = jsonObj.getString("a");
    		matchInfo.setDOdds(dOdds);
    		matchInfo.setHOdds(hOdds);
    		matchInfo.setAOdds(aOdds);
    	}
    	DlLeagueTeam homeTeam = leagueTeamMapper.getBySportteryTeamid(homeTeamId);
    	if(null != homeTeam) {
    		matchInfo.setHomeTeamPic(homeTeam.getTeamPic());
    	}
    	DlLeagueTeam visitingTeam = leagueTeamMapper.getBySportteryTeamid(visitingTeamId);
    	if(null != visitingTeam) {
    		matchInfo.setVisitingTeamPic(visitingTeam.getTeamPic());
    	}
    	return matchInfo;
    }
    /**
     * 获取球队分析信息
     * @param changciId
     */
	public MatchTeamInfosDTO matchTeamInfos(LotteryMatch lotteryMatch) {
		MatchTeamInfoDTO hvMatchTeamInfo = this.hvMatchTeamInfo(lotteryMatch);
		MatchTeamInfoDTO hhMatchTeamInfo = this.hhMatchTeamInfo(lotteryMatch);
		MatchTeamInfoDTO vvMatchTeamInfo = this.vvMatchTeamInfo(lotteryMatch);
		MatchTeamInfoDTO hMatchTeamInfo = this.hMatchTeamInfo(lotteryMatch);
		MatchTeamInfoDTO vMatchTeamInfo = this.vMatchTeamInfo(lotteryMatch);
		MatchTeamInfosDTO dto = new MatchTeamInfosDTO();
		dto.setHvMatchTeamInfo(hvMatchTeamInfo);
		dto.setHhMatchTeamInfo(hhMatchTeamInfo);
		dto.setVvMatchTeamInfo(vvMatchTeamInfo);
		dto.setHMatchTeamInfo(hMatchTeamInfo);
		dto.setVMatchTeamInfo(vMatchTeamInfo);
		return dto;
	}

	/**
	 * 客场主客
	 * @param lotteryMatch
	 * @return
	 */
	private MatchTeamInfoDTO vMatchTeamInfo(LotteryMatch lotteryMatch) {
		Integer visitingTeamId = lotteryMatch.getVisitingTeamId();
		String visitingTeamAbbr = lotteryMatch.getVisitingTeamAbbr();
		MatchTeamInfoDTO vMatchTeamInfo = new MatchTeamInfoDTO();
		List<LotteryMatch> lotteryMatchs = lotteryMatchMapper.getByTeamId(null, visitingTeamId, 15);
		if(null != lotteryMatchs) {
			int win =0, draw = 0, lose = 0;
			List<MatchInfoDTO> matchInfos = new ArrayList<MatchInfoDTO>(lotteryMatchs.size());
			for(LotteryMatch match: lotteryMatchs) {
				MatchInfoDTO matchInfo = new MatchInfoDTO();
				matchInfo.setHomeTeamAbbr(match.getHomeTeamAbbr());
				matchInfo.setLeagueAddr(match.getLeagueAddr());
				matchInfo.setVisitingTeamAbbr(match.getVisitingTeamAbbr());
				String whole = match.getWhole();
				matchInfo.setWhole(whole);
				String matchDay =LocalDateTime.ofInstant(match.getMatchTime().toInstant(), ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
				matchInfo.setMatchDay(matchDay);
				if(StringUtils.isNotBlank(whole)) {
					boolean isHome = true;
					if(!match.getHomeTeamId().equals(visitingTeamId)) {
						isHome = false;
					}
					String[] split = whole.split(":");
					Integer h = Integer.valueOf(isHome?split[0]:split[1]);
					Integer a = Integer.valueOf(isHome?split[1]:split[0]);
					if(h > a) {
						matchInfo.setMatchRs("胜");
						win++;
					}else if(h < a) {
						matchInfo.setMatchRs("负");
						lose++;
					}else {
						matchInfo.setMatchRs("平");
						draw++;
					}
				}
				matchInfos.add(matchInfo);
			}
			vMatchTeamInfo.setDraw(draw);
			vMatchTeamInfo.setLose(lose);
			vMatchTeamInfo.setMatchInfos(matchInfos);
			vMatchTeamInfo.setTeamAbbr(visitingTeamAbbr);
			vMatchTeamInfo.setWin(win);
			vMatchTeamInfo.setTotal(matchInfos.size());
		}
		return vMatchTeamInfo;
	}
	/**
	 * 客场客
	 * @param lotteryMatch
	 * @return
	 */
	private MatchTeamInfoDTO vvMatchTeamInfo(LotteryMatch lotteryMatch) {
		Integer visitingTeamId = lotteryMatch.getVisitingTeamId();
		String visitingTeamAbbr = lotteryMatch.getVisitingTeamAbbr();
		MatchTeamInfoDTO vMatchTeamInfo = new MatchTeamInfoDTO();
		List<LotteryMatch> lotteryMatchs = lotteryMatchMapper.getByTeamIdForvv(visitingTeamId, 15);
		if(null != lotteryMatchs) {
			int win =0, draw = 0, lose = 0;
			List<MatchInfoDTO> matchInfos = new ArrayList<MatchInfoDTO>(lotteryMatchs.size());
			for(LotteryMatch match: lotteryMatchs) {
				MatchInfoDTO matchInfo = new MatchInfoDTO();
				matchInfo.setHomeTeamAbbr(match.getHomeTeamAbbr());
				matchInfo.setLeagueAddr(match.getLeagueAddr());
				matchInfo.setVisitingTeamAbbr(match.getVisitingTeamAbbr());
				String whole = match.getWhole();
				matchInfo.setWhole(whole);
				String matchDay =LocalDateTime.ofInstant(match.getMatchTime().toInstant(), ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
				matchInfo.setMatchDay(matchDay);
				if(StringUtils.isNotBlank(whole)) {
					boolean isHome = true;
					if(!match.getHomeTeamId().equals(visitingTeamId)) {
						isHome = false;
					}
					String[] split = whole.split(":");
					Integer h = Integer.valueOf(isHome?split[0]:split[1]);
					Integer a = Integer.valueOf(isHome?split[1]:split[0]);
					if(h > a) {
						matchInfo.setMatchRs("胜");
						win++;
					}else if(h < a) {
						matchInfo.setMatchRs("负");
						lose++;
					}else {
						matchInfo.setMatchRs("平");
						draw++;
					}
				}
				matchInfos.add(matchInfo);
			}
			vMatchTeamInfo.setDraw(draw);
			vMatchTeamInfo.setLose(lose);
			vMatchTeamInfo.setMatchInfos(matchInfos);
			vMatchTeamInfo.setTeamAbbr(visitingTeamAbbr);
			vMatchTeamInfo.setWin(win);
			vMatchTeamInfo.setTotal(matchInfos.size());
		}
		return vMatchTeamInfo;
	}
	/**
	 * 主场主客
	 * @param lotteryMatch
	 * @return
	 */
	private MatchTeamInfoDTO hMatchTeamInfo(LotteryMatch lotteryMatch) {
		Integer homeTeamId = lotteryMatch.getHomeTeamId();
		String homeTeamAbbr = lotteryMatch.getHomeTeamAbbr();
		MatchTeamInfoDTO hMatchTeamInfo = new MatchTeamInfoDTO();
		List<LotteryMatch> lotteryMatchs = lotteryMatchMapper.getByTeamId(homeTeamId, null, 15);
		if(null != lotteryMatchs) {
			int win =0, draw = 0, lose = 0;
			List<MatchInfoDTO> matchInfos = new ArrayList<MatchInfoDTO>(lotteryMatchs.size());
			for(LotteryMatch match: lotteryMatchs) {
				MatchInfoDTO matchInfo = new MatchInfoDTO();
				matchInfo.setHomeTeamAbbr(match.getHomeTeamAbbr());
				matchInfo.setLeagueAddr(match.getLeagueAddr());
				matchInfo.setVisitingTeamAbbr(match.getVisitingTeamAbbr());
				String whole = match.getWhole();
				matchInfo.setWhole(whole);
				String matchDay =LocalDateTime.ofInstant(match.getMatchTime().toInstant(), ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
				matchInfo.setMatchDay(matchDay);
				if(StringUtils.isNotBlank(whole)) {
					boolean isHome = true;
					if(!match.getHomeTeamId().equals(homeTeamId)) {
						isHome = false;
					}
					String[] split = whole.split(":");
					Integer h = Integer.valueOf(isHome?split[0]:split[1]);
					Integer a = Integer.valueOf(isHome?split[1]:split[0]);
					if(h > a) {
						matchInfo.setMatchRs("胜");
						win++;
					}else if(h < a) {
						matchInfo.setMatchRs("负");
						lose++;
					}else {
						matchInfo.setMatchRs("平");
						draw++;
					}
				}
				matchInfos.add(matchInfo);
			}
			hMatchTeamInfo.setDraw(draw);
			hMatchTeamInfo.setLose(lose);
			hMatchTeamInfo.setMatchInfos(matchInfos);
			hMatchTeamInfo.setTeamAbbr(homeTeamAbbr);
			hMatchTeamInfo.setWin(win);
			hMatchTeamInfo.setTotal(matchInfos.size());
		}
		return hMatchTeamInfo;
	}
	/**
	 * 主场主
	 * @param lotteryMatch
	 * @return
	 */
	private MatchTeamInfoDTO hhMatchTeamInfo(LotteryMatch lotteryMatch) {
		Integer homeTeamId = lotteryMatch.getHomeTeamId();
		String homeTeamAbbr = lotteryMatch.getHomeTeamAbbr();
		MatchTeamInfoDTO hMatchTeamInfo = new MatchTeamInfoDTO();
		List<LotteryMatch> lotteryMatchs = lotteryMatchMapper.getByTeamIdForhh(homeTeamId, 15);
		if(null != lotteryMatchs) {
			int win =0, draw = 0, lose = 0;
			List<MatchInfoDTO> matchInfos = new ArrayList<MatchInfoDTO>(lotteryMatchs.size());
			for(LotteryMatch match: lotteryMatchs) {
				MatchInfoDTO matchInfo = new MatchInfoDTO();
				matchInfo.setHomeTeamAbbr(match.getHomeTeamAbbr());
				matchInfo.setLeagueAddr(match.getLeagueAddr());
				matchInfo.setVisitingTeamAbbr(match.getVisitingTeamAbbr());
				String whole = match.getWhole();
				matchInfo.setWhole(whole);
				String matchDay =LocalDateTime.ofInstant(match.getMatchTime().toInstant(), ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
				matchInfo.setMatchDay(matchDay);
				if(StringUtils.isNotBlank(whole)) {
					boolean isHome = true;
					if(!match.getHomeTeamId().equals(homeTeamId)) {
						isHome = false;
					}
					String[] split = whole.split(":");
					Integer h = Integer.valueOf(isHome?split[0]:split[1]);
					Integer a = Integer.valueOf(isHome?split[1]:split[0]);
					if(h > a) {
						matchInfo.setMatchRs("胜");
						win++;
					}else if(h < a) {
						matchInfo.setMatchRs("负");
						lose++;
					}else {
						matchInfo.setMatchRs("平");
						draw++;
					}
				}
				matchInfos.add(matchInfo);
			}
			hMatchTeamInfo.setDraw(draw);
			hMatchTeamInfo.setLose(lose);
			hMatchTeamInfo.setMatchInfos(matchInfos);
			hMatchTeamInfo.setTeamAbbr(homeTeamAbbr);
			hMatchTeamInfo.setWin(win);
			hMatchTeamInfo.setTotal(matchInfos.size());
		}
		return hMatchTeamInfo;
	}
	private MatchTeamInfoDTO hvMatchTeamInfo(LotteryMatch lotteryMatch) {
		Integer homeTeamId = lotteryMatch.getHomeTeamId();
		String homeTeamAbbr = lotteryMatch.getHomeTeamAbbr();
		Integer visitingTeamId = lotteryMatch.getVisitingTeamId();
		MatchTeamInfoDTO hvMatchTeamInfo = new MatchTeamInfoDTO();
		List<LotteryMatch> lotteryMatchs = lotteryMatchMapper.getByTeamId(homeTeamId, visitingTeamId, 10);
		if(null != lotteryMatchs) {
			int win =0, draw = 0, lose = 0;
			List<MatchInfoDTO> matchInfos = new ArrayList<MatchInfoDTO>(lotteryMatchs.size());
			for(LotteryMatch match: lotteryMatchs) {
				MatchInfoDTO matchInfo = new MatchInfoDTO();
				matchInfo.setHomeTeamAbbr(match.getHomeTeamAbbr());
				matchInfo.setLeagueAddr(match.getLeagueAddr());
				matchInfo.setVisitingTeamAbbr(match.getVisitingTeamAbbr());
				String whole = match.getWhole();
				matchInfo.setWhole(whole);
				String matchDay =LocalDateTime.ofInstant(match.getMatchTime().toInstant(), ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
				matchInfo.setMatchDay(matchDay);
				if(StringUtils.isNotBlank(whole)) {
					String[] split = whole.split(":");
					Integer h = Integer.valueOf(split[0]);
					Integer a = Integer.valueOf(split[1]);
					if(h > a) {
						matchInfo.setMatchRs("胜");
						win++;
					}else if(h < a) {
						matchInfo.setMatchRs("负");
						lose++;
					}else {
						matchInfo.setMatchRs("平");
						draw++;
					}
				}
				matchInfos.add(matchInfo);
			}
			hvMatchTeamInfo.setDraw(draw);
			hvMatchTeamInfo.setLose(lose);
			hvMatchTeamInfo.setMatchInfos(matchInfos);
			hvMatchTeamInfo.setTeamAbbr(homeTeamAbbr);
			hvMatchTeamInfo.setWin(win);
			hvMatchTeamInfo.setTotal(matchInfos.size());
		}
		return hvMatchTeamInfo;
	}
	
	public List<Integer> getChangcidIsUnEnd(){
		return lotteryMatchMapper.getChangcidIsUnEnd();
	}

	public List<LeagueInfoDTO> getFilterConditions() {
		List<LeagueInfoDTO> filterConditions = lotteryMatchMapper.getFilterConditions();
		if(filterConditions == null) {
			filterConditions = new ArrayList<LeagueInfoDTO>(0);
		}
		return filterConditions;
	}
	
	/**
	 * 历史赛事入库
	 * @throws IOException 
	 */
	@Transactional
	public BaseResult<String> historyMatchIntoDB(String filepath) {
		String separator = File.separator;
		File file=new File(filepath);
		
		if(!file.exists()) {
			return ResultGenerator.genSuccessResult("目录不存在");
		}
		
		if(!file.isDirectory()) {
			return ResultGenerator.genSuccessResult("文件不存在");
		}
		
		List<String> pathsList = new ArrayList<String>();
		String realPath = "";
		if(filepath.contains("\\")) {
			String[] filePathArr = filepath.split("\\");
			pathsList = Arrays.asList(filePathArr);
		}
		
		if(filepath.contains("/")) {
			String[] filePathArr = filepath.split("/");
			pathsList = Arrays.asList(filePathArr);
		}
		
		for(String str:pathsList) {
			realPath += str + separator;
		}
		
        String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(realPath)));
			if(StringUtils.isEmpty(content)) {
				ResultGenerator.genSuccessResult("历史赛事json文件 为空，未进行入库");
			}
		} catch (Exception e) {
			log.error("解析历史赛事json文件出错:"+e.getMessage());
			return ResultGenerator.genFailResult("解析历史赛事json文件出错");
		}
		
		List<Object> matchList = JSON.parseArray(content);
		if(CollectionUtils.isEmpty(matchList)) {
			return ResultGenerator.genFailResult("解析历史赛事json文件出错");
		}
		
		List<LotteryMatch> lotteryMatchList = new ArrayList<>();
		matchList.stream().forEach(s->{
			JSONObject str = (JSONObject)s;
			LotteryMatch m = new LotteryMatch();
			m.setLeagueAddr(str.getString("league_addr"));
			m.setChangciId(str.getInteger("cangci_id"));
			m.setChangci(str.getString("changci"));
			m.setHomeTeamAbbr("home_team_abbr");
			m.setVisitingTeamAbbr("visiting_team_abbr");
			m.setMatchTime(str.getDate("match_time"));
			m.setShowTime(str.getDate("match_time"));
			m.setCreateTime(DateUtil.getCurrentTimeLong());
			m.setFirstHalf(str.getString("first_hslf"));
			m.setWhole(str.getString("whole"));
			m.setMatchSn(this.commonCreateIssue(str.getString("match_time"), str.getString("changci")));
			m.setIsShow(Integer.valueOf(ProjectConstant.ONE_YES));
			m.setIsDel(Integer.valueOf(ProjectConstant.ZERO_NO));
			m.setStatus(Integer.valueOf(ProjectConstant.ONE_YES));
			m.setIsHot(Integer.valueOf(ProjectConstant.ZERO_NO));
			
			lotteryMatchList.add(m);
		});
		
		int historyMatchSize = lotteryMatchList.size();
		while(historyMatchSize > 0) {
			int num = matchList.size() > 200?200:historyMatchSize;
			List<LotteryMatch> lotterySubMatchList =  lotteryMatchList.subList(0, num);
			int rst = lotteryMatchMapper.batchInsertHistoryMatch(lotteryMatchList);
			if(rst != lotterySubMatchList.size()) {
				log.error("历史赛事入库失败");
			}
			lotteryMatchList.removeAll(lotterySubMatchList);
		}
		
		return ResultGenerator.genSuccessResult("历史赛事入库成功");
	}
}

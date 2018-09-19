package com.dl.shop.lottery.service;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.BasketBallMatchResultHILOEnum;
import com.dl.base.enums.MatchBasketBallResultMaxMinScoreEnum;
import com.dl.base.enums.MatchResultCrsEnum;
import com.dl.base.enums.MatchResultHadEnum;
import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.BasketBallLeagueInfoDTO;
import com.dl.lottery.dto.DlJcLqDateMatchDTO;
import com.dl.lottery.dto.DlJcLqMatchDTO;
import com.dl.lottery.dto.DlJcLqMatchListDTO;
import com.dl.lottery.dto.DlJcLqMatchPlayDTO;
import com.dl.lottery.dto.DlJcZqMatchCellDTO;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.lottery.param.DlJcLqMatchListParam;
import com.dl.shop.lottery.core.LocalWeekDate;
import com.dl.shop.lottery.dao.LotteryPlayClassifyMapper;
import com.dl.shop.lottery.dao2.DlMatchBasketballMapper;
import com.dl.shop.lottery.dao2.DlMatchPlayBasketballMapper;
import com.dl.shop.lottery.model.DlMatchBasketball;
import com.dl.shop.lottery.model.DlMatchPlayBasketball;
import com.dl.shop.lottery.model.LotteryPlayClassify;

@Service
@Transactional(value="transactionManager2")
public class DlMatchBasketballService extends AbstractService<DlMatchBasketball> {
	
	private final static Logger logger = Logger.getLogger(DlMatchBasketballService.class);
	
    @Resource
    private DlMatchBasketballMapper dlMatchBasketballMapper;
    
    @Resource
    private DlMatchPlayBasketballMapper dlMatchPlayBasketballMapper;
    
	@Resource
	private LotteryPlayClassifyMapper lotteryPlayClassifyMapper;
   
	
	public List<BasketBallLeagueInfoDTO> getBasketBallFilterConditions() {
		List<BasketBallLeagueInfoDTO> filterConditions = dlMatchBasketballMapper.getBasketBallFilterConditions();
		if(filterConditions == null) {
			filterConditions = new ArrayList<BasketBallLeagueInfoDTO>(0);
		}
		return filterConditions;
	}
	
    
    /**
     * 获取赛事列表
     * @param param
     * @return
     */
	public DlJcLqMatchListDTO getMatchList(DlJcLqMatchListParam param) {
		long start = System.currentTimeMillis();
		DlJcLqMatchListDTO dlJcLqMatchListDTO = new DlJcLqMatchListDTO();
		List<DlMatchBasketball> matchList = dlMatchBasketballMapper.getMatchList(param.getLeagueId());
		if(matchList == null || matchList.size() == 0) {
			return dlJcLqMatchListDTO;
		}
		List<Integer> changciIds = matchList.stream().map(match->match.getChangciId()).collect(Collectors.toList());
		String playType = param.getPlayType();
		Map<Integer, List<DlJcLqMatchPlayDTO>> matchPlayMap = new HashMap<Integer, List<DlJcLqMatchPlayDTO>>();
		List<DlMatchPlayBasketball> matchPlayList = dlMatchPlayBasketballMapper.matchPlayListByChangciIds(changciIds.toArray(new Integer[changciIds.size()]),playType);
		for(DlMatchPlayBasketball matchPlay: matchPlayList) {
			Integer playType2 = matchPlay.getPlayType();
			Integer changciId = matchPlay.getChangciId();
			DlJcLqMatchPlayDTO matchPlayDto = this.initDlJcZqMatchCell(matchPlay);
			if(matchPlayDto == null) {
				continue;
			}
			List<DlJcLqMatchPlayDTO> dlJcZqMatchPlayDTOs = matchPlayMap.get(changciId);
			if(dlJcZqMatchPlayDTOs == null){
				dlJcZqMatchPlayDTOs = new ArrayList<DlJcLqMatchPlayDTO>();
				matchPlayMap.put(changciId, dlJcZqMatchPlayDTOs);
			}
			dlJcZqMatchPlayDTOs.add(matchPlayDto);
		}
		long end1 = System.currentTimeMillis();
		logger.info("==============getmatchlist 准备用时 ："+(end1-start) + " playType="+param.getPlayType());
		dlJcLqMatchListDTO = this.getMatchListDTO(matchList, playType, matchPlayMap);
		long end = System.currentTimeMillis();
		logger.info("==============getmatchlist 对象转化用时 ："+(end-end1) + " playType="+param.getPlayType());
		logger.info("==============getmatchlist 用时 ："+(end-start) + " playType="+param.getPlayType());
	    return dlJcLqMatchListDTO;
	}

	private DlJcLqMatchListDTO getMatchListDTO(List<DlMatchBasketball> matchList, String playType,	Map<Integer, List<DlJcLqMatchPlayDTO>> matchPlayMap) {
		DlJcLqMatchListDTO dlJcLqMatchListDTO = new DlJcLqMatchListDTO();
		Map<String, DlJcLqDateMatchDTO> map = new HashMap<String, DlJcLqDateMatchDTO>();
		Integer totalNum = 0;
		Integer betPreTime = 1537259767;//this.getBetPreTime();
		for(DlMatchBasketball match: matchList) {
			Date matchTimeDate = match.getMatchTime();
			Instant instant = matchTimeDate.toInstant();
			int matchTime = Long.valueOf(instant.getEpochSecond()).intValue();
			int betEndTime = this.getBetEndTime(matchTime, betPreTime);
			//投注结束
//			if(Long.valueOf(betEndTime) < Instant.now().getEpochSecond()) {
//				continue;
//			}
			DlJcLqMatchDTO matchDto = new DlJcLqMatchDTO();
			matchDto.setIsShutDown(0);
			matchDto.setBetEndTime(betEndTime);
			matchDto.setChangci(match.getChangci());
			matchDto.setChangciId(match.getChangciId().toString());
			matchDto.setHomeTeamAbbr(match.getHomeTeamAbbr());
			matchDto.setHomeTeamId(match.getHomeTeamId());
			matchDto.setHomeTeamName(match.getHomeTeamName());
			matchDto.setHomeTeamRank(match.getHomeTeamRank());
			matchDto.setIsHot(match.getIsHot());
			matchDto.setLeagueAddr(match.getLeagueAbbr());
			matchDto.setLeagueId(match.getLeagueId().toString());
			matchDto.setLeagueName(match.getLeagueName());
			LocalDate showTimeDate = LocalDateTime.ofInstant(match.getShowTime().toInstant(), ZoneId.systemDefault()).toLocalDate();
			String matchDay = showTimeDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
			DayOfWeek dayOfWeek = showTimeDate.getDayOfWeek();
			String displayName = LocalWeekDate.getName(dayOfWeek.getValue());
			String showMatchDay = displayName + " " + matchDay;
			matchDto.setMatchDay(matchDay);
			matchDto.setMatchId(match.getMatchId());
			matchDto.setMatchTime(matchTime);
			matchDto.setPlayCode(match.getMatchSn());
			matchDto.setPlayType(Integer.parseInt(playType));
			matchDto.setVisitingTeamAbbr(match.getVisitingTeamAbbr());
			matchDto.setVisitingTeamId(match.getVisitingTeamId().toString());
			matchDto.setVisitingTeamName(match.getVisitingTeamName());
			matchDto.setVisitingTeamRank(match.getVisitingTeamRank());
			List<DlJcLqMatchPlayDTO> matchPlays = matchPlayMap.get(match.getChangciId());
			if(matchPlays == null || matchPlays.size() == 0) {
				continue;
			}
			
			matchPlays.sort((item1,item2)->item1.getPlayType().compareTo(item2.getPlayType()));
			matchDto.setMatchPlays(matchPlays);
			//
			DlJcLqDateMatchDTO dlJcLqMatchDTO = map.get(matchDay);
			if(null == dlJcLqMatchDTO) {
				dlJcLqMatchDTO = new DlJcLqDateMatchDTO();
				dlJcLqMatchDTO.setSortMatchDay(matchDay);
				dlJcLqMatchDTO.setMatchDay(showMatchDay);
				map.put(matchDay, dlJcLqMatchDTO);
			}
			//初始化投注选项
			if(matchDto.getIsHot() == 1) {
				dlJcLqMatchListDTO.getHotPlayList().add(matchDto);
			} else {
				dlJcLqMatchDTO.getPlayList().add(matchDto);
			}
			totalNum++;
		}
		map.forEach((key, value) ->{
			value.getPlayList().sort((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode()));
			dlJcLqMatchListDTO.getPlayList().add(value);
		});
		dlJcLqMatchListDTO.getHotPlayList().sort((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode()));
		dlJcLqMatchListDTO.getPlayList().sort((item1,item2)->item1.getSortMatchDay().compareTo(item2.getSortMatchDay()));
		dlJcLqMatchListDTO.setAllMatchCount(totalNum.toString());
		dlJcLqMatchListDTO.setLotteryClassifyId(1);
		LotteryPlayClassify playClassify = lotteryPlayClassifyMapper.getPlayClassifyByPlayType(1, Integer.parseInt(playType));
		Integer lotteryPlayClassifyId = playClassify == null?Integer.parseInt(playType):playClassify.getLotteryPlayClassifyId();
		dlJcLqMatchListDTO.setLotteryPlayClassifyId(lotteryPlayClassifyId);
		return dlJcLqMatchListDTO;
	}

	
//	public Integer getBetPreTime() {
//		Integer betPreTime = dlMatchBasketballMapper.getBetPreTime();
//		if(betPreTime == null || betPreTime <= 0) {
//			betPreTime = ProjectConstant.BET_PRESET_TIME;
//		}
//		return betPreTime;
//	}
//	
//	public int getBetEndTime(Integer matchTime) {
//		Integer betPreTime = this.getBetPreTime();
//		return this.getBetEndTime(matchTime, betPreTime);
//	}
	
	//获取出票截至时间
	private int getBetEndTime(Integer matchTime, Integer betPreTime) {
		Instant instant = Instant.ofEpochSecond(matchTime.longValue());
		LocalDateTime matchDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		int matchWeekDay = matchDateTime.getDayOfWeek().getValue();
		int matchHour = matchDateTime.getHour();
		int betEndTime = matchTime - betPreTime;
		LocalDateTime betendDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(betEndTime), ZoneId.systemDefault());
		int betHour = betendDateTime.getHour();
		//LocalDateTime showDate = LocalDateTime.ofInstant(match.getShowTime().toInstant(), ZoneId.systemDefault());
		//今天展示第二天比赛时间
		//if(betendDateTime.toLocalDate().isAfter(LocalDate.now()) && LocalDate.now().isEqual(showDate.toLocalDate())) {
		if(betendDateTime.toLocalDate().isAfter(LocalDate.now())) {
			if(matchWeekDay < 7 && matchWeekDay > 1 && (matchHour < 9 || betHour < 10)) {
				LocalDate preLocalDate = betendDateTime.plusDays(-1).toLocalDate();
				betEndTime = Long.valueOf(LocalDateTime.of(preLocalDate, LocalTime.of(23, 00, 00)).toInstant(ZoneOffset.ofHours(8)).getEpochSecond()).intValue();
			} else if(matchHour > 0 && (matchHour < 9 || betHour < 10))  {
				betEndTime = Long.valueOf(LocalDateTime.of(betendDateTime.toLocalDate(), LocalTime.of(00, 00, 00)).toInstant(ZoneOffset.ofHours(8)).getEpochSecond()).intValue();
			}
		} else {
			if(betHour > 22) {
				betEndTime = Long.valueOf(LocalDateTime.of(betendDateTime.toLocalDate(), LocalTime.of(23, 00, 00)).toInstant(ZoneOffset.ofHours(8)).getEpochSecond()).intValue();
			}
		}
		return betEndTime;
	}
	
	/**
	 * 初始化球赛类型投注选项
	 * @param dto
	 */
	private DlJcLqMatchPlayDTO initDlJcZqMatchCell(DlMatchPlayBasketball matchPlay) {
		DlJcLqMatchPlayDTO dto = new DlJcLqMatchPlayDTO();
		dto.setPlayContent(matchPlay.getPlayContent());
		dto.setPlayType(matchPlay.getPlayType());
		Integer playType = matchPlay.getPlayType();
		switch(playType) {
			case 1:
				initDlJcLqMatchCell1(dto);
				break;
			case 2:
				initDlJcLqMatchCell2(dto);
				break;
			case 3:
				initDlJcLqMatchCell3(dto);
				break;
			case 4:
				initDlJcLqMatchCell4(dto);
				break;
		}
		if(dto != null) {
			dto.setPlayContent(null);
		}
		return dto;
	}
	
	/**
	 * 胜负
		{
			'a': '4.35',
			'h': '1.09',
			'p_code': 'MNL',
			'o_type': 'F',
			'p_id': '490563',
			'p_status': 'Selling',
			'single': '0',
			'allup': '1',
			'goalline': '',
			'fixedodds': '',
			'cbt': '1',
			'int': '1',
			'vbt': '0',
			'h_trend': '0',
			'a_trend': '0',
			'd_trend': '0',
			'l_trend': '0'
		}
	 * @param dto
	 */
	private void initDlJcLqMatchCell1(DlJcLqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		String hOdds = jsonObj.getString("h");
		String aOdds = jsonObj.getString("a");
		String fixedOdds = jsonObj.getString("fixedodds");
		dto.setFixedOdds(fixedOdds);
		Integer single = jsonObj.getInteger("single");
		dto.setSingle(single);
		dto.setHomeCell(new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_H.getCode().toString(), MatchResultHadEnum.HAD_H.getMsg(), hOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_A.getCode().toString(), MatchResultHadEnum.HAD_A.getMsg(), aOdds));
	}
	
//	PLAY_TYPE_HAD(2,"HDC"), // 让分胜负
//	PLAY_TYPE_CRS(3,"WNM"), //胜分差
//	PLAY_TYPE_TTG(4,"HILO"); //大小分
	/**
	 * 让分胜负
		{
			'a': '1.75',
			'h': '1.75',
			'goalline': '',
			'p_code': 'HDC',
			'o_type': 'F',
			'p_id': '490562',
			'p_status': 'Selling',
			'single': '0',
			'allup': '1',
			'fixedodds': '-9.5',
			'cbt': '1',
			'int': '1',
			'vbt': '0',
			'h_trend': '0.07',
			'a_trend': '-0.06',
			'd_trend': '0',
			'l_trend': '0'
		}
	 * @param dto
	 */
	private void initDlJcLqMatchCell2(DlJcLqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		String hOdds = jsonObj.getString("h");
		String aOdds = jsonObj.getString("a");
		Integer single = jsonObj.getInteger("single");
		dto.setSingle(single);
		dto.setHomeCell(new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_H.getCode().toString(), MatchResultHadEnum.HAD_H.getMsg(), hOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_A.getCode().toString(), MatchResultHadEnum.HAD_A.getMsg(), aOdds));
	}
	
	/**
	 * 胜分差
		{
			'w1': '4.90',
			'w2': '3.30',
			'w3': '3.95',
			'w4': '5.80',
			'w5': '9.50',
			'w6': '9.70',
			'l1': '7.80',
			'l2': '9.50',
			'l3': '23.00',
			'l4': '44.00',
			'l5': '97.00',
			'l6': '120.0',
			'p_code': 'WNM',
			'o_type': 'F',
			'p_id': '490564',
			'p_status': 'Selling',
			'single': '1',
			'allup': '1',
			'goalline': '',
			'fixedodds': '',
			'cbt': '1',
			'int': '1',
			'vbt': '0',
			'h_trend': '0',
			'a_trend': '0',
			'd_trend': '0',
			'l_trend': '0'
		}
	 * @param dto
	 */
	private void initDlJcLqMatchCell3(DlJcLqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		Integer single = jsonObj.getInteger("single");
		dto.setSingle(single);
		Set<String> keySet = jsonObj.keySet();
		DlJcZqMatchCellDTO homeCell = new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_H.getCode().toString(), MatchResultHadEnum.HAD_H.getMsg(), null);
		homeCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(6));
		dto.setHomeCell(homeCell);
		DlJcZqMatchCellDTO visitingCell = new DlJcZqMatchCellDTO(MatchResultHadEnum.HAD_A.getCode().toString(), MatchResultHadEnum.HAD_A.getMsg(), null);
		visitingCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(6));
		dto.setVisitingCell(visitingCell);
		for(String key: keySet) {
			if(key.indexOf("l") == 0) {
				String code = key.substring(1);
				String odds = jsonObj.getString(key);
				String name = BasketBallMatchResultHILOEnum.getName(code);
				homeCell.getCellSons().add(new DlJcZqMatchCellDTO(code, name, odds));
			}else if(key.indexOf("w") == 0) {
				String level = key.substring(1);
				String code = BasketBallMatchResultHILOEnum.getCode(level);
				String odds = jsonObj.getString(key);
				String name = BasketBallMatchResultHILOEnum.getName(code);
				visitingCell.getCellSons().add(new DlJcZqMatchCellDTO(code, name, odds));
			}
		}
		dto.setHomeCell(homeCell);
		dto.setVisitingCell(visitingCell);
	}
	/**
	 * 大小分
		{
			'h': '1.69',
			'l': '1.80',
			'goalline': '',
			'p_code': 'HILO',
			'o_type': 'F',
			'p_id': '490565',
			'p_status': 'Selling',
			'single': '0',
			'allup': '1',
			'fixedodds': '+162.5',
			'cbt': '1',
			'int': '1',
			'vbt': '0',
			'h_trend': '-0.06',
			'a_trend': '0',
			'd_trend': '0',
			'l_trend': '0.05'
		}
	 * @param dto
	 */
	private void initDlJcLqMatchCell4(DlJcLqMatchPlayDTO dto) {
		String playContent = dto.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		Integer single = jsonObj.getInteger("single");
		String hOdds = jsonObj.getString("h");
		String aOdds = jsonObj.getString("l");
		String fixedOdds = jsonObj.getString("fixedodds");
		dto.setFixedOdds(fixedOdds);
		dto.setSingle(single);
		dto.setHomeCell(new DlJcZqMatchCellDTO(MatchBasketBallResultMaxMinScoreEnum.MIN_SCORE.getCode().toString(), MatchBasketBallResultMaxMinScoreEnum.MIN_SCORE.getMsg(), hOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO(MatchBasketBallResultMaxMinScoreEnum.MAX_SCORE.getCode().toString(), MatchBasketBallResultMaxMinScoreEnum.MAX_SCORE.getMsg(), aOdds));
	}
	
	
	//判断是否停售
//	private boolean isStop(DlMatchPlayBasketball matchPlay) {
//		String playContent = matchPlay.getPlayContent();
//		JSONObject jsonObj = JSON.parseObject(playContent);
//		String cbtValue = jsonObj.getString("cbt");
//		if("2".equals(cbtValue)) {
//			Boolean isStop = Boolean.TRUE;
//			/****************20180820 据抓取工程师说，停售时（00-09）,cbt也是2，我们不能隐藏赛事因此增加逻辑**************/
//			Integer changciId= matchPlay.getChangciId();
//			DlMatchLive matchLive = dlMatchLiveMapper.getByChangciId(changciId);
//			if(matchLive!=null&&!StringUtils.isEmpty(matchLive.getMatchLiveInfo())){			
//				String matchLiveInfo = matchLive.getMatchLiveInfo();
//				JSONObject matchLiveJsonObj = JSON.parseObject(matchLiveInfo);
//				String matchStatus = matchLiveJsonObj.getString("match_status");
//                if("Fixture".equalsIgnoreCase(matchStatus)){
//					isStop = Boolean.FALSE;
//				}
//			}
//			/****************20180820 据抓取工程师说，停售时（00-09）,cbt也是2，我们不能隐藏赛事因此增加逻辑**************/
//			return isStop;
//		}
//		return false;
//	}
	
}

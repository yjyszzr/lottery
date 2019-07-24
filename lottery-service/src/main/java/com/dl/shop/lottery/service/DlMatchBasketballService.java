package com.dl.shop.lottery.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.*;
import com.dl.base.result.BaseResult;
import com.dl.base.service.AbstractService;
import com.dl.base.util.JSONHelper;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.*;
import com.dl.lottery.param.DlJcLqMatchBetParam;
import com.dl.lottery.param.DlJcLqMatchListParam;
import com.dl.member.api.ISwitchConfigService;
import com.dl.member.param.UserDealActionParam;
import com.dl.shop.lottery.core.LocalWeekDate;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryPlayClassifyMapper;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.dao2.DlMatchBasketballMapper;
import com.dl.shop.lottery.dao2.DlMatchPlayBasketballMapper;
import com.dl.shop.lottery.dao2.DlResultBasketballMapper;
import com.dl.shop.lottery.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(value="transactionManager2")
@Slf4j
public class DlMatchBasketballService extends AbstractService<DlMatchBasketball> {
	
	private final static Logger logger = Logger.getLogger(DlMatchBasketballService.class);
	
    @Resource
    private DlMatchBasketballMapper dlMatchBasketballMapper;
    
    @Resource
    private DlMatchPlayBasketballMapper dlMatchPlayBasketballMapper;
    
	@Resource
	private LotteryPlayClassifyMapper lotteryPlayClassifyMapper;
	
	@Resource
	private LotteryPrintMapper	lotteryPrintMapper;
	
	@Resource
	private	ISwitchConfigService iSwitchConfigService;
	
    @Resource
    private DlResultBasketballMapper dlResultBasketballMapper;
   
	public List<DlResultBasketball> queryBasketBallResult(List<Integer> changciIdList) {
		List<DlResultBasketball> resultList = dlResultBasketballMapper.queryMatchResultsByChangciIds(changciIdList);
		return resultList;
	}
	
	//是否停售
	public boolean isBasketBallShutDownBet() {
		int shutDownBetValue = lotteryPrintMapper.shutDownBasketBallBetValue();
		if(shutDownBetValue == 1) {
			return true;
		}
		//判断用户是否有交易
		UserDealActionParam param = new UserDealActionParam();
		param.setUserId(SessionUtil.getUserId());
		BaseResult<Integer> userDealAction = iSwitchConfigService.userDealAction(param);
		Integer data = userDealAction.getData();
		if(null != data && 0 == data) {
			return true;
		}
		return false;
	}
	
	//获取篮球最小投注金额
	public Double getMinBasketBetMoney() {
		Double minBetMoney = lotteryPrintMapper.getMinBasketBetMoney();
		if(minBetMoney == null) {
			minBetMoney = 0.0;
		}
		return minBetMoney;
	}
	
	public boolean isHideMatch(Integer betEndTime, Integer matchTime) {
		Instant instant = Instant.ofEpochSecond(matchTime.longValue());
		LocalDateTime betendDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(betEndTime), ZoneId.systemDefault());
		LocalDateTime matchDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		int matchWeekDay = matchDateTime.getDayOfWeek().getValue();
		//0-9点的赛事在当天不能投注
		LocalTime localTime = LocalTime.now(ZoneId.systemDefault());
        int nowHour = localTime.getHour();
        int betHour = betendDateTime.getHour();
		if(betendDateTime.toLocalDate().isEqual(LocalDate.now())){
			if(nowHour < 9 && betHour < 9) {
				if(matchWeekDay < 6) {
					return true;
				} else if(matchWeekDay > 5 && betHour > 0 ) {//周六日的1点之后
					return true;
				}
			} else if(nowHour > 22 && betHour > 22) {//23点的比赛不再展示
				return true;
			}
		}
		return false;
	}
	
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
		List<DlMatchPlayBasketball> matchPlayList = dlMatchPlayBasketballMapper.matchPlayListByChangciIds(changciIds.toArray(new Integer[changciIds.size()]),"6".equals(playType)?"":playType);
		for(DlMatchPlayBasketball matchPlay: matchPlayList) {
//			if(this.isStop(matchPlay)) {
//				continue;
//			}
			
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
		Integer betPreTime = this.getBetPreTime();
		for(DlMatchBasketball match: matchList) {
			Date matchTimeDate = match.getMatchTime();
			Instant instant = matchTimeDate.toInstant();
			int matchTime = Long.valueOf(instant.getEpochSecond()).intValue();
			int betEndTime = this.getBetEndTime(matchTime, betPreTime);
			//投注结束
			if(Long.valueOf(betEndTime) < Instant.now().getEpochSecond()) {
				continue;
			}
			DlJcLqMatchDTO matchDto = new DlJcLqMatchDTO();
			matchDto.setMatchId(match.getMatchId());
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
			
			if("5".equals(playType)) {
				List<Integer> collect = matchPlays.stream().map(dto->dto.getPlayType()).collect(Collectors.toList());
				for(int i=1; i<= 4; i++) {
					if(!collect.contains(i)) {
						DlJcLqMatchPlayDTO dto = new DlJcLqMatchPlayDTO();
						dto.setPlayType(i);
						dto.setIsShow(0);
						matchPlays.add(dto);
					}
				}
			}

			matchPlays.sort((item1,item2)->item1.getPlayType().compareTo(item2.getPlayType()));
			matchDto.setMatchPlays(matchPlays);
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
		dlJcLqMatchListDTO.setLotteryClassifyId(3);
		LotteryPlayClassify playClassify = lotteryPlayClassifyMapper.getPlayClassifyByPlayType(3, Integer.parseInt(playType));
		Integer lotteryPlayClassifyId = playClassify == null?Integer.parseInt(playType):playClassify.getLotteryPlayClassifyId();
		dlJcLqMatchListDTO.setLotteryPlayClassifyId(lotteryPlayClassifyId);
		return dlJcLqMatchListDTO;
	}


	//{'h': '1.69', 'l': '1.80', 'goalline': '', 'p_code': 'HILO', 'o_type': 'F', 'p_id': '490628', 
	//'p_status': 'Selling', 'single': '0', 'allup': '0', 'fixedodds': '+164.5', 'cbt': '2', 
	//'int': '2', 'vbt': '0', 'h_trend': '0', 'a_trend': '0', 'd_trend': '0', 'l_trend': '0'}
	//判断是否停售
	private boolean isStop(DlMatchPlayBasketball matchPlay) {
		String playContent = matchPlay.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		String cbtValue = jsonObj.getString("cbt");
		if("2".equals(cbtValue)) {
			Boolean isStop = Boolean.TRUE;
			return isStop;
		}
		return false;
	}
	
	
	public Integer getBetPreTime() {
		Integer betPreTime = lotteryPrintMapper.getBasketBallBetPreTime();
		if(betPreTime == null || betPreTime <= 0) {
			betPreTime = ProjectConstant.BET_PRESET_TIME;
		}
		return betPreTime;
	}
	
	public Integer getBetEndTimeNew(Integer matchTime) {
		Integer betPreTime = this.getBetPreTime();
		Integer betEndTime = matchTime - betPreTime;
		return betEndTime;
	}
	
	public Integer getBetEndTime(Integer matchTime) {
		Integer betPreTime = this.getBetPreTime();
		return this.getBetEndTime(matchTime, betPreTime);
	}
	
	//获取出票截至时间
	private int getBetEndTime(Integer matchTime, Integer betPreTime) {
		Instant instant = Instant.ofEpochSecond(matchTime.longValue());
		LocalDateTime matchDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		int matchWeekDay = matchDateTime.getDayOfWeek().getValue();
		int matchHour = matchDateTime.getHour();
		int betEndTime = matchTime - betPreTime;
		LocalDateTime betendDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(matchTime), ZoneId.systemDefault());
		int betHour = betendDateTime.getHour();
        if(matchWeekDay <= 5 && matchWeekDay >= 1 && matchHour <= 10 && matchHour >=0) {
            LocalDate preLocalDate = betendDateTime.plusDays(-1).toLocalDate();
            betEndTime = Long.valueOf(LocalDateTime.of(preLocalDate, LocalTime.of(21, 50, 00)).toInstant(ZoneOffset.ofHours(8)).getEpochSecond()).intValue();
        } else if(matchWeekDay <= 5 && matchWeekDay >= 1 && matchHour <= 24 && matchHour >=10 )  {
            betEndTime = Long.valueOf(LocalDateTime.of(betendDateTime.toLocalDate(), LocalTime.of(21, 50, 00)).toInstant(ZoneOffset.ofHours(8)).getEpochSecond()).intValue();
        } else if(matchWeekDay <= 7 && matchWeekDay >= 6 && matchHour <= 10 && matchHour >=0){
            LocalDate preLocalDate = betendDateTime.plusDays(-1).toLocalDate();
            betEndTime = Long.valueOf(LocalDateTime.of(preLocalDate, LocalTime.of(22, 50, 00)).toInstant(ZoneOffset.ofHours(8)).getEpochSecond()).intValue();
        } else if(matchWeekDay <= 7 && matchWeekDay >= 6 && matchHour <= 24 && matchHour >=10){
            betEndTime = Long.valueOf(LocalDateTime.of(betendDateTime.toLocalDate(), LocalTime.of(22, 50, 00)).toInstant(ZoneOffset.ofHours(8)).getEpochSecond()).intValue();
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
		dto.setHomeCell(new DlJcZqMatchCellDTO(MatchBasketResultHdEnum.HD_H.getCode().toString(), MatchBasketResultHdEnum.HD_H.getMsg(), hOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO(MatchBasketResultHdEnum.HD_D.getCode().toString(), MatchBasketResultHdEnum.HD_D.getMsg(), aOdds));
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
		String fixedOdds = jsonObj.getString("fixedodds");
		if(StringUtils.isEmpty(fixedOdds)) {
			fixedOdds = "0.00";
		}
		dto.setFixedOdds(fixedOdds);
		Integer single = jsonObj.getInteger("single");
		dto.setSingle(single);
		dto.setHomeCell(new DlJcZqMatchCellDTO(MatchBasketBallResultHDCEnum.HHD_H.getCode().toString(), MatchBasketBallResultHDCEnum.HHD_H.getMsg(), hOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO(MatchBasketBallResultHDCEnum.HHD_A.getCode().toString(), MatchBasketBallResultHDCEnum.HHD_A.getMsg(), aOdds));
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
		dto.setSingle(1);//竞彩篮球的胜分差可以选择单关
		Set<String> keySet = jsonObj.keySet();
		DlJcZqMatchCellDTO homeCell = new DlJcZqMatchCellDTO(MatchBasketBallResultHDCEnum.HHD_H.getCode().toString(), MatchBasketBallResultHDCEnum.HHD_H.getMsg(), null);
		homeCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(6));
		dto.setHomeCell(homeCell);
		DlJcZqMatchCellDTO visitingCell = new DlJcZqMatchCellDTO(MatchBasketBallResultHDCEnum.HHD_H.getCode().toString(), MatchBasketBallResultHDCEnum.HHD_H.getMsg(), null);
		visitingCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(6));
		dto.setVisitingCell(visitingCell);
		for(String key: keySet) {
			if(key.indexOf("w") == 0 && key.length() == 2) {
				String code = key.substring(1);//1-6 cell_code 进行编码
				String odds = jsonObj.getString(key);
				String name = BasketBallHILOLeverlEnum.getName(code);
				homeCell.getCellSons().add(new DlJcZqMatchCellDTO(code, name, odds));
			}else if(key.indexOf("l" ) == 0 && key.length() == 2) {
				String code = String.valueOf((Integer.valueOf(key.substring(1)) + 6));//11-16 cell_code 编码
				String odds = jsonObj.getString(key);
				String name = BasketBallHILOLeverlEnum.getName(code);
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
		fixedOdds = fixedOdds.replace("+", "");
		dto.setHomeCell(new DlJcZqMatchCellDTO(MatchBasketBallResultHILOEnum.L_SCORE.getCode().toString(), "小于"+fixedOdds+"分", aOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO(MatchBasketBallResultHILOEnum.H_SCORE.getCode().toString(), "大于"+fixedOdds+"分", hOdds));
	}
	
	/**
	 * 出票综合信息
	 * @param param
	 * @return
	 */
	public DLLQBetInfoDTO getBetInfo1(DlJcLqMatchBetParam param) {
		long start = System.currentTimeMillis();
		List<MatchBasketBallBetPlayDTO> matchBellCellList = param.getMatchBetPlays();
		String betTypes = param.getBetType();
		Map<String, List<String>> indexMap = this.getBetIndexList(matchBellCellList, betTypes);
		long end1 = System.currentTimeMillis();
		logger.info("1计算投注排列用时：" + (end1-start)+ " - "+start);
		Map<String, List<MatchBasketBallBetPlayCellDTO>> playCellMap = this.getMatchBetPlayMap(matchBellCellList);
		TMatchBetMaxAndMinOddsList tem = this.maxMoneyBetPlayCellsForLottery(playCellMap);
		List<Double> maxOddsList = tem.getMaxOddsList();
		List<Double> minOddsList = tem.getMinOddsList();
		long end2 = System.currentTimeMillis();
		logger.info("2计算投注排列后获取不同投注的赛事信息用时：" + (end2-end1)+ " - "+start);
		//计算投票综合信息核心算法
		Double totalMaxMoney = 0.0;
		Double totalMinMoney = Double.MAX_VALUE;
		int betNums = 0;
		int ticketNum = 0;
		Double maxLotteryMoney = 0.0;
		for(String betType: indexMap.keySet()) {
			char[] charArray = betType.toCharArray();
			int num = Integer.valueOf(String.valueOf(charArray[0]));
			List<String> betIndexList = indexMap.get(betType);
			for(String str: betIndexList) {//所有注组合
				String[] strArr = str.split(",");
				Double maxMoney = 2.0*param.getTimes();
				Double minMoney = 2.0*param.getTimes();
				Integer betNum = 1;
				for(String item: strArr) {//单注组合
					MatchBasketBallBetPlayDTO betPlayDto = matchBellCellList.get(Integer.valueOf(item));
					Integer cellNums = betPlayDto.getMatchBetCells().stream().map(item1->item1.getBetCells().size()).reduce(Integer::sum).get();
					betNum*=cellNums;
					Double double1 = maxOddsList.get(Integer.valueOf(item));
					maxMoney = maxMoney*double1;
					Double double2 = minOddsList.get(Integer.valueOf(item));
					minMoney = minMoney*double2;
				}
				if(betNum > 10000) {
					Double betMoney = betNum*param.getTimes()*2.0;
					if(betMoney > maxLotteryMoney) {
						maxLotteryMoney = betMoney;
					}
				}
				log.info("minMoney:" + minMoney);
				totalMaxMoney+=maxMoney;
				totalMinMoney=Double.min(totalMinMoney, minMoney);
				betNums+=betNum;
			}
		}
		logger.info("***************最大预测奖金"+totalMaxMoney);
		logger.info("***************最小预测奖金"+totalMinMoney);
		logger.info("***************投注数："+betNums);
		long end3 = System.currentTimeMillis();
		logger.info("3计算投注基础信息用时：" + (end3-end2)+ " - "+start);
		//页面返回信息对象
		DLLQBetInfoDTO betInfoDTO = new DLLQBetInfoDTO();
		betInfoDTO.setMaxLotteryMoney(maxLotteryMoney.toString());
		betInfoDTO.setMaxBonus(String.format("%.2f", totalMaxMoney));
		betInfoDTO.setMinBonus(String.format("%.2f", totalMinMoney));
		betInfoDTO.setTimes(param.getTimes());
		betInfoDTO.setBetNum(betNums);
		betInfoDTO.setTicketNum(ticketNum);
		Double money = betNums*param.getTimes()*2.0;
		betInfoDTO.setMoney(String.format("%.2f", money));
		betInfoDTO.setBetType(param.getBetType());
		betInfoDTO.setPlayType(param.getPlayType());
		//所有选项的最后一个场次编码
		String issue = matchBellCellList.stream().max((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode())).get().getPlayCode();
		betInfoDTO.setIssue(issue);
		long end4 = System.currentTimeMillis();
		logger.info("4计算投注统计信息用时：" + (end4-end3)+ " - "+start);
		logger.info("5计算投注信息用时：" + (end4-start)+ " - "+start);
		return betInfoDTO;
	}
	
	private Map<String, List<MatchBasketBallBetPlayCellDTO>> getMatchBetPlayMap(List<MatchBasketBallBetPlayDTO> matchBellCellList) {
		//整理投注对象
		Map<String, List<MatchBasketBallBetPlayCellDTO>> playCellMap = new HashMap<String, List<MatchBasketBallBetPlayCellDTO>>(matchBellCellList.size());
		matchBellCellList.forEach(betPlayDto->{
			String playCode = betPlayDto.getPlayCode();
			List<MatchBasketBallBetCellDTO> matchBetCells = betPlayDto.getMatchBetCells();
			List<MatchBasketBallBetPlayCellDTO> list = playCellMap.get(playCode);
			if(list == null) {
				list = new ArrayList<MatchBasketBallBetPlayCellDTO>(matchBetCells.size());
				playCellMap.put(playCode, list);
			}
			
			for(MatchBasketBallBetCellDTO cell: matchBetCells) {
				MatchBasketBallBetPlayCellDTO playCellDto = new MatchBasketBallBetPlayCellDTO(betPlayDto);
				playCellDto.setPlayType(cell.getPlayType());
				List<DlJcLqMatchCellDTO> betCells = cell.getBetCells();
				playCellDto.setBetCells(betCells);

				playCellDto.setFixedodds(cell.getFixedOdds());
				list.add(playCellDto);
			}
		});
		return playCellMap;
	}
	//计算混合玩法最大投注中奖金额
	private TMatchBetMaxAndMinOddsList maxMoneyBetPlayCellsForLottery(Map<String, List<MatchBasketBallBetPlayCellDTO>> playCellMap) {
		TMatchBetMaxAndMinOddsList tem = new TMatchBetMaxAndMinOddsList();
		List<Double> maxOdds = new ArrayList<Double>(playCellMap.size());
		List<Double> minOdds = new ArrayList<Double>(playCellMap.size());
		for(String playCode: playCellMap.keySet()) {
			List<MatchBasketBallBetPlayCellDTO> list = playCellMap.get(playCode);
			List<Double> allbetComOdds   =   this.allbetComOdds(list);
//			allbetComOdds.add(1.32);
//			allbetComOdds.add(2.32);
//			allbetComOdds.add(1.50);
			log.info("allbetComOdds is not null: "+ JSONHelper.bean2json(allbetComOdds));
			if(CollectionUtils.isEmpty(allbetComOdds)) {
				continue;
			}
			if(allbetComOdds.size() ==1) {
				Double maxOrMinOdds = allbetComOdds.get(0);
				maxOdds.add(maxOrMinOdds);
				minOdds.add(maxOrMinOdds);
			}else {
				Double max = allbetComOdds.stream().max((item1,item2)->item1.compareTo(item2)).get();
				maxOdds.add(max);
				Double min = allbetComOdds.stream().min((item1,item2)->item1.compareTo(item2)).get();
				minOdds.add(min);
			}
		}
		log.info("allbetComOdds is maxOdds: "+ maxOdds);
		log.info("allbetComOdds is minOdds: "+ minOdds);
		tem.setMaxOddsList(maxOdds);
		tem.setMinOddsList(minOdds);
		return tem;
	}

	
	/**
	 * 计算混合玩法的排斥后的该场次的几种可能赔率
	 * @param list 混合玩法 同一场次的所有玩法选项
	 */
	private List<Double> allbetComOdds(List<MatchBasketBallBetPlayCellDTO> list) {
//		PLAY_TYPE_HHAD(1,"hhad"), //让球胜平负
//		PLAY_TYPE_HAD(2,"had"), // 胜平负
//		PLAY_TYPE_CRS(3,"crs"), //比分
//		PLAY_TYPE_TTG(4,"ttg"), //总进球
//		PLAY_TYPE_HAFU(5,"hafu"), //半全场
//		PLAY_TYPE_MIX(6,"mix"), //混合过关
//		PLAY_TYPE_TSO(7,"tso"); //2选1
		
		//比分 胜分差
		Optional<MatchBasketBallBetPlayCellDTO> optionalcrs = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_CRS.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO crsBetPlay = optionalcrs.isPresent()?optionalcrs.get():null;
		// 大小分
		Optional<MatchBasketBallBetPlayCellDTO> optionalttg = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_TTG.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO ttgBetPlay = optionalttg.isPresent()?optionalttg.get():null;
		//让球胜平负
		Optional<MatchBasketBallBetPlayCellDTO> optional2 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO hhadBetPlay = optional2.isPresent()?optional2.get():null;
		//胜平负
		Optional<MatchBasketBallBetPlayCellDTO> optional3 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO hadBetPlay = optional3.isPresent()?optional3.get():null;
		//半全场
		Optional<MatchBasketBallBetPlayCellDTO> optional4 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HAFU.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO hafuBetPlay = optional4.isPresent()?optional4.get():null;
		
		List<Double> rst = new ArrayList<Double>();
		if(crsBetPlay != null) {
			List<Double> cc = this.cc1(crsBetPlay, ttgBetPlay, hhadBetPlay, hadBetPlay);
			rst.addAll(cc);
		}
		if(ttgBetPlay != null) {
//			crsBetPlay = this.bb(ttgBetPlay);
			List<Double> cc = this.ccdxf(ttgBetPlay, hhadBetPlay, hadBetPlay);
			rst.addAll(cc);
		}
		if(hadBetPlay != null) {
			List<Double> c = this.cc2(hhadBetPlay, hadBetPlay, hafuBetPlay);
			rst.addAll(c);
		}
		if(hafuBetPlay != null) {
			List<Double> c = this.cc2(hhadBetPlay, null, hafuBetPlay);
			rst.addAll(c);
		}
		if(hhadBetPlay != null) {
			List<Double> c = this.cc2(hhadBetPlay, null, null);
			rst.addAll(c);
		}
		return rst;
		
	}

	private List<Double> cc1(MatchBasketBallBetPlayCellDTO sfcBetPlay, MatchBasketBallBetPlayCellDTO dxfBetPlay, MatchBasketBallBetPlayCellDTO rqsfBetPlay, MatchBasketBallBetPlayCellDTO sfBetPlay) {
		List<Double> allOdds = new ArrayList<Double>();
		List<Double> allBetSumOdds = new ArrayList<Double>();
		//胜分差
		Double sum = 0.0;
		List<DlJcLqMatchCellDTO> sfcBetCells = sfcBetPlay.getBetCells(); 
//		DlJcLqMatchCellDTO sfcMaxCellOdds = sfcBetCells.stream().max((cellOdds1, cellOdds2) -> Double.valueOf(cellOdds1.getCellCode()).compareTo(Double.valueOf(cellOdds2.getCellCode()))).get();
		DlJcLqMatchCellDTO sfcMaxCellOdds = sfcBetCells.stream().max(Comparator.comparingDouble( DlJcLqMatchCellDTO ::getCellOddsD) ).get();
		DlJcLqMatchCellDTO sfcMinCellOdds = sfcBetCells.stream().min(Comparator.comparingDouble( DlJcLqMatchCellDTO ::getCellOddsD) ).get();
		allOdds.add(Double.valueOf(sfcMaxCellOdds.getCellOdds()));
		allOdds.add(Double.valueOf(sfcMinCellOdds.getCellOdds()));
		sum += Double.valueOf(sfcMaxCellOdds.getCellOdds());
		//大小分
		if ( dxfBetPlay != null) {
			List<DlJcLqMatchCellDTO> dxfBetCells = dxfBetPlay.getBetCells(); 
			Optional<DlJcLqMatchCellDTO> dxfMaxCellOdds = dxfBetCells.stream().max(Comparator.comparingDouble( DlJcLqMatchCellDTO ::getCellOddsD));
			allOdds.add(Double.valueOf(dxfMaxCellOdds.get().getCellOdds()));
			sum += Double.valueOf(dxfMaxCellOdds.get().getCellOdds());
		}
		//让球胜负
		if (rqsfBetPlay != null) {
			List<DlJcLqMatchCellDTO> rqsfBetCells = rqsfBetPlay.getBetCells(); 
			if (Integer.parseInt(sfcMaxCellOdds.getCellCode())>6) {//code > 6 是客胜 1-6是主胜
				Optional<DlJcLqMatchCellDTO> rqsfMaxCellOdds = rqsfBetCells.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == 2 ).findFirst();
				if (rqsfMaxCellOdds.isPresent()) {
					Double odds = Double.valueOf(rqsfMaxCellOdds.get().getCellOdds());//选中的总进球玩法的可用赔率
					sum += odds;
					allOdds.add(odds);
				}
			}else {
				Optional<DlJcLqMatchCellDTO> rqsfMaxCellOdds = rqsfBetCells.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == 1 ).findFirst();
				if (rqsfMaxCellOdds.isPresent()) {
					Double odds = Double.valueOf(rqsfMaxCellOdds.get().getCellOdds());//选中的总进球玩法的可用赔率
					sum += odds;
					allOdds.add(odds);
				}
			}
		}
		//胜负
		if (sfBetPlay != null) {
			List<DlJcLqMatchCellDTO> sfBetCells = sfBetPlay.getBetCells(); 
			if (Integer.parseInt(sfcMaxCellOdds.getCellCode())>6) {//code > 6 是客胜 1-6是主胜
				Optional<DlJcLqMatchCellDTO> sfMaxCellOdds = sfBetCells.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == 2 ).findFirst();
				if (sfMaxCellOdds.isPresent()) {
					Double odds = Double.valueOf(sfMaxCellOdds.get().getCellOdds());//选中的总进球玩法的可用赔率
					sum += odds;
					allOdds.add(odds);
				}
			}else {
				Optional<DlJcLqMatchCellDTO> sfMaxCellOdds = sfBetCells.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == 1 ).findFirst();
				if (sfMaxCellOdds.isPresent()) {
					Double odds = Double.valueOf(sfMaxCellOdds.get().getCellOdds());//选中的总进球玩法的可用赔率
					sum += odds;
					allOdds.add(odds);
				}
			}
		}
		allOdds.add(sum);
		allBetSumOdds.addAll(allOdds);
		return allBetSumOdds;
	}
	
	
	private List<Double> ccdxf( MatchBasketBallBetPlayCellDTO dxfBetPlay, MatchBasketBallBetPlayCellDTO rqsfBetPlay, MatchBasketBallBetPlayCellDTO sfBetPlay) {
		List<Double> allBetSumOdds = new ArrayList<Double>();
		List<Double> c = this.cc2(rqsfBetPlay, sfBetPlay, null);
		
		Optional<Double> odds = c.stream().max((a1,a2) -> Double.valueOf(a1).compareTo(Double.valueOf(a2)));
		if (odds.isPresent()) {
			DlJcLqMatchCellDTO dxfMaxCellOdds = dxfBetPlay.getBetCells().stream().max(Comparator.comparingDouble( DlJcLqMatchCellDTO ::getCellOddsD)).get();
			double sum = odds.get();
			sum+= Double.valueOf(dxfMaxCellOdds.getCellOdds()) ;
			allBetSumOdds.add(sum);
		}
		
		for (int i = 0; i < dxfBetPlay.getBetCells().size(); i++) {
			allBetSumOdds.add(Double.valueOf(dxfBetPlay.getBetCells().get(i).getCellOdds()));
		}
		allBetSumOdds.addAll(c);
		return allBetSumOdds;
	}
	
	

	private List<Double> ccBak(MatchBasketBallBetPlayCellDTO crsBetPlay, MatchBasketBallBetPlayCellDTO ttgBetPlay,
			MatchBasketBallBetPlayCellDTO hhadBetPlay, MatchBasketBallBetPlayCellDTO hadBetPlay, MatchBasketBallBetPlayCellDTO hafuBetPlay) {
		//比分的所有项
		List<DlJcLqMatchCellDTO> betCells = crsBetPlay.getBetCells();//比分的所有选项
		List<Double> allBetSumOdds = new ArrayList<Double>();
		for(DlJcLqMatchCellDTO dto: betCells) {
			String cellCode = dto.getCellCode();
			String[] arr = cellCode.split("");
			int m = 0;
			int n = 0;
			if(arr.length == 1) {
				m = Integer.parseInt(arr[0]);
			}else {
				m = Integer.parseInt(arr[0]);
				n = Integer.parseInt(arr[1]);
			}
			int sum = m+n;//总进球数
			int sub = m-n;//进球差数
			List<Double> allOdds = new ArrayList<Double>();
			String cellOdds = dto.getCellOdds();
			if(StringUtils.isNotBlank(cellOdds)) {
				allOdds.add(Double.valueOf(cellOdds));
			}
			//1.大小分
			if(ttgBetPlay != null) {
				List<DlJcLqMatchCellDTO> betCells2 = ttgBetPlay.getBetCells();
				int sucCode = sum > 7?7:sum;
				Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
				if(optional.isPresent()) {
					Double odds = Double.valueOf(optional.get().getCellOdds());//选中的总进球玩法的可用赔率
					if(allOdds.size() == 0) {
						allOdds.add(odds);
					}else {
						Double old = allOdds.remove(0);
						allOdds.add(Double.sum(old, odds));
					}
				}
			}
			//2.让分胜平负
			if(hhadBetPlay != null) {
				List<DlJcLqMatchCellDTO> betCells2 = hhadBetPlay.getBetCells();
				double sucCode = sub + Double.valueOf(hhadBetPlay.getFixedodds());
				if(sucCode > 0) {
					sucCode = MatchResultHadEnum.HAD_H.getCode();
				}else if(sucCode < 0) {
					sucCode = MatchResultHadEnum.HAD_A.getCode();
				}else {
					sucCode = MatchResultHadEnum.HAD_D.getCode();
				}
				final double sucCode1 = sucCode;
				Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode1).findFirst();
				if(optional.isPresent()) {
					Double odds = Double.valueOf(optional.get().getCellOdds());//选中的让球胜平负玩法的可用赔率
					Double old = allOdds.remove(0);
					allOdds.add(Double.sum(old, odds));
				}
			}
			//3.胜平负
			boolean isH=false,isA=false;
			if(hadBetPlay != null) {
				List<DlJcLqMatchCellDTO> betCells2 = hadBetPlay.getBetCells();
				if(sum == 0) {//平
					int sucCode = MatchResultHadEnum.HAD_D.getCode();
					Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
					if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
						Double odds = Double.valueOf(optional.get().getCellOdds());
						Double old = allOdds.remove(0);
						allOdds.add(Double.sum(old, odds));
					}
				}else if(sum == 1) {//胜，负
					if(n ==0) {
						int sucCode = MatchResultHadEnum.HAD_H.getCode();
						Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
							isH=true;
						}
					}else {
						int sucCode = MatchResultHadEnum.HAD_A.getCode();
						Optional<DlJcLqMatchCellDTO> optional1 = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional1.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional1.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
							isA=true;
						}
					}
				}else {
					if(sub > 0) {//胜
						int sucCode = MatchResultHadEnum.HAD_H.getCode();
						Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
						}
					} else if(sub < 0) {//负
						int sucCode = MatchResultHadEnum.HAD_A.getCode();
						Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
						}
					}else {//平
						int sucCode = MatchResultHadEnum.HAD_D.getCode();
						Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
						}
					}
				}
			}
			//4.半全场
			if(hafuBetPlay != null) {
				List<DlJcLqMatchCellDTO> betCells2 = hafuBetPlay.getBetCells();
				if(sum == 0) {
					Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->MatchResultHafuEnum.HAFU_DD.getCode().equals(betCell.getCellCode())).findFirst();
					if(optional.isPresent()) {
						Double odds = Double.valueOf(optional.get().getCellOdds());
						Double old = allOdds.remove(0);
						allOdds.add(Double.sum(old, odds));
					}
				}else if(sum  == 1) {
					Double old = allOdds.remove(0);
					if(isH) {
						for(DlJcLqMatchCellDTO betCell: betCells2) {
							String betCellCode = betCell.getCellCode();
							if(betCellCode.equals(MatchResultHafuEnum.HAFU_DH.getCode()) || betCellCode.equals(MatchResultHafuEnum.HAFU_HH.getCode())) {
								Double odds = Double.valueOf(betCell.getCellOdds());
								allOdds.add(Double.sum(old, odds));
							}
						}
					}
					if(isA) {
						for(DlJcLqMatchCellDTO betCell: betCells2) {
							String betCellCode = betCell.getCellCode();
							if(betCellCode.equals(MatchResultHafuEnum.HAFU_DA.getCode()) || betCellCode.equals(MatchResultHafuEnum.HAFU_AA.getCode())) {
								Double odds = Double.valueOf(betCell.getCellOdds());
								allOdds.add(Double.sum(old, odds));
							}
						}
					}
				}else {
					Double old = allOdds.remove(0);
					if(sub > 0) {
						for(DlJcLqMatchCellDTO betCell: betCells2) {
							String betCellCode = betCell.getCellCode();
							if(betCellCode.equals(MatchResultHafuEnum.HAFU_DH.getCode()) || betCellCode.equals(MatchResultHafuEnum.HAFU_HH.getCode())) {
								Double odds = Double.valueOf(betCell.getCellOdds());
								allOdds.add(Double.sum(old, odds));
							}
							if(n != 0 && betCellCode.equals(MatchResultHafuEnum.HAFU_AH.getCode())) {
								Double odds = Double.valueOf(betCell.getCellOdds());
								allOdds.add(Double.sum(old, odds));
							}
						}
					} else if(sub < 0) {
						for(DlJcLqMatchCellDTO betCell: betCells2) {
							String betCellCode = betCell.getCellCode();
							if(betCellCode.equals(MatchResultHafuEnum.HAFU_DA.getCode()) || betCellCode.equals(MatchResultHafuEnum.HAFU_AA.getCode())) {
								Double odds = Double.valueOf(betCell.getCellOdds());
								allOdds.add(Double.sum(old, odds));
							}
							if(n != 0 && betCellCode.equals(MatchResultHafuEnum.HAFU_HA.getCode())) {
								Double odds = Double.valueOf(betCell.getCellOdds());
								allOdds.add(Double.sum(old, odds));
							}
						}
					}else {
						for(DlJcLqMatchCellDTO betCell: betCells2) {
							String betCellCode = betCell.getCellCode();
							if(betCellCode.equals(MatchResultHafuEnum.HAFU_HD.getCode()) || betCellCode.equals(MatchResultHafuEnum.HAFU_DD.getCode()) || betCellCode.equals(MatchResultHafuEnum.HAFU_AD.getCode())) {
								Double odds = Double.valueOf(betCell.getCellOdds());
								allOdds.add(Double.sum(old, odds));
							}
						}
					}
				}
			}
			allBetSumOdds.addAll(allOdds);
		}
		return allBetSumOdds;
	}
	
	private List<Double> cc2(MatchBasketBallBetPlayCellDTO hhadBetPlay, MatchBasketBallBetPlayCellDTO hadBetPlay,
			MatchBasketBallBetPlayCellDTO hafuBetPlay) {
		List<Double> allBetSumOdds = new ArrayList<Double>(1);
		//胜平负
		List<Double> allOdds = new ArrayList<Double>();
		Double hOdds = null, dOdds = null, aOdds = null;
		if(hadBetPlay != null){
			List<DlJcLqMatchCellDTO> betCells = hadBetPlay.getBetCells();
			for(DlJcLqMatchCellDTO dto: betCells) {
				Integer cellCode = Integer.parseInt(dto.getCellCode());
				Double odds = Double.valueOf(dto.getCellOdds());
				if(MatchBasketResultHdEnum.HD_H.getCode().equals(cellCode)) {
					hOdds = odds;
				} else if(MatchBasketResultHdEnum.HD_D.getCode().equals(cellCode)) {
					dOdds = odds;
				} 
			}
		}
		//半全场
		List<Double> hList = new ArrayList<Double>(0), dList = new ArrayList<Double>(0), aList=new ArrayList<Double>(0);
		if(hafuBetPlay != null) {
			List<DlJcLqMatchCellDTO> betCells = hafuBetPlay.getBetCells();
			for(DlJcLqMatchCellDTO dto: betCells) {
				Integer checkCode = Integer.parseInt(dto.getCellCode().substring(1));
				Double odds = Double.valueOf(dto.getCellOdds());
				if(hOdds == null && dOdds == null && aOdds == null) {
					if(MatchResultHadEnum.HAD_H.getCode().equals(checkCode)) {
						hList.add(odds);
					}else if(MatchResultHadEnum.HAD_D.getCode().equals(checkCode)){
						dList.add(odds);
					}else if(MatchResultHadEnum.HAD_A.getCode().equals(checkCode)){
						aList.add(odds);
					}
				} else {
					if(hOdds != null && MatchResultHadEnum.HAD_H.getCode().equals(checkCode)) {
						hList.add(odds+hOdds);
					}
					if(dOdds != null && MatchResultHadEnum.HAD_D.getCode().equals(checkCode)) {
						dList.add(odds+dOdds);					
					}
					if(aOdds != null && MatchResultHadEnum.HAD_A.getCode().equals(checkCode)) {
						aList.add(odds+aOdds);
					}
				}
			}
			
		}
		//整合前两种
		boolean ish=false,isd=false,isa=false;
		if(hOdds != null || hList.size() > 0) {
			if(hList.size() == 0) {
				hList.add(hOdds);
			} 
			ish = true;
		}
		if(dOdds != null || dList.size() > 0) {
			if(dList.size() == 0) {
				dList.add(dOdds);
			}
			isd = true;
		}
		if(aOdds != null || aList.size() > 0) {
			if(aList.size() == 0) {
				aList.add(aOdds);
			}
			isa = true;
		}
		//让球
//		Double hhOdds = null, hdOdds = null, haOdds = null;
		if(hhadBetPlay != null) {
			List<DlJcLqMatchCellDTO> betCells = hhadBetPlay.getBetCells();
			Double fixNum = Double.valueOf(hhadBetPlay.getFixedodds());
			List<Double> naList = new ArrayList<Double>(aList.size()*3);
			List<Double> ndList = new ArrayList<Double>(dList.size()*3);
			List<Double> nhList = new ArrayList<Double>(hList.size()*3);
			for(DlJcLqMatchCellDTO dto: betCells) {
				Integer cellCode = Integer.parseInt(dto.getCellCode());
				Double odds = Double.valueOf(dto.getCellOdds());
				if(!ish && !isd && !isa) {
					allOdds.add(odds);
				} else {
					if(fixNum > 0) {
						if(ish && MatchBasketBallResultHDCEnum.HHD_H.getCode().equals(cellCode)) {
						/*	hList.forEach(item->Double.sum(item, odds));
							nhList.addAll(hList);*/
							for(Double item: hList) {
								nhList.add(Double.sum(item, odds));
							}
						}
						if(isd && MatchBasketBallResultHDCEnum.HHD_H.getCode().equals(cellCode)) {
							/*dList.forEach(item->Double.sum(item, odds));
							ndList.addAll(dList);*/
							for(Double item: dList) {
								ndList.add(Double.sum(item, odds));
							}
						}
						if(isa) {
							if(!MatchBasketBallResultHDCEnum.HHD_H.getCode().equals(cellCode)) {
								List<Double> tnaList = new ArrayList<Double>(aList);
								for(Double item: tnaList) {
									naList.add(Double.sum(item, odds));
								}
							}
							/*tnaList.forEach(item->Double.sum(item, odds));
							naList.addAll(tnaList);*/
						}
					}else {
						if(ish) {
							if(!MatchBasketBallResultHDCEnum.HHD_A.getCode().equals(cellCode)) {
								List<Double> tnhList = new ArrayList<Double>(hList);
								/*tnhList.forEach(item->Double.sum(item, odds));
								nhList.addAll(tnhList);*/
								for(Double item: tnhList) {
									nhList.add(Double.sum(item, odds));
								}
							}
						}
						if(isd && MatchBasketBallResultHDCEnum.HHD_A.getCode().equals(cellCode)) {
							/*dList.forEach(item->Double.sum(item, odds));
							ndList.addAll(dList);*/
							for(Double item: dList) {
								ndList.add(Double.sum(item, odds));
							}
						}
						if(isa && MatchBasketBallResultHDCEnum.HHD_A.getCode().equals(cellCode)) {
							/*aList.forEach(item->Double.sum(item, odds));
							naList.addAll(aList);*/
							for(Double item: aList) {
								naList.add(Double.sum(item, odds));
							}
						}
					}
				}
			}
			if(nhList != null) {
				allOdds.addAll(nhList);
			}
			if(naList != null) {
				allOdds.addAll(naList);
			}
			if(ndList != null) {
				allOdds.addAll(ndList);
			}
		}
//		if(allOdds.size() == 0){
			if(hList != null) {
				allOdds.addAll(hList);
			}
			if(aList != null) {
				allOdds.addAll(aList);
			}
			if(dList != null) {
				allOdds.addAll(dList);
			}
//		}
//		logger.info("--------------" + JSONHelper.bean2json(allOdds));
		allBetSumOdds.addAll(allOdds);
		return allBetSumOdds;
	}
	
	private MatchBasketBallBetPlayCellDTO bb(MatchBasketBallBetPlayCellDTO ttgBetPlay) {
		MatchBasketBallBetPlayCellDTO crsBetPlay;
		List<DlJcLqMatchCellDTO> ttgBetCells = ttgBetPlay.getBetCells();
		List<DlJcLqMatchCellDTO> ncrsBetCells = new ArrayList<DlJcLqMatchCellDTO>();
		crsBetPlay = new MatchBasketBallBetPlayCellDTO();
		crsBetPlay.setBetCells(ncrsBetCells);
		for(DlJcLqMatchCellDTO matchCellDto: ttgBetCells) {
			Integer qiuNum = Integer.parseInt(matchCellDto.getCellCode());
			if(qiuNum == 0) {
				DlJcLqMatchCellDTO nmatchCellDto = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_00.getCode(), MatchResultCrsEnum.CRS_00.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
			} else if(qiuNum == 1) {
				DlJcLqMatchCellDTO nmatchCellDto = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_10.getCode(), MatchResultCrsEnum.CRS_10.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto1 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_01.getCode(), MatchResultCrsEnum.CRS_01.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
			}else if(qiuNum == 2) {
				DlJcLqMatchCellDTO nmatchCellDto = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_11.getCode(), MatchResultCrsEnum.CRS_11.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto1 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_02.getCode(), MatchResultCrsEnum.CRS_02.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto2 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_20.getCode(), MatchResultCrsEnum.CRS_20.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
				ncrsBetCells.add(nmatchCellDto2);
			}else if(qiuNum == 3) {
				DlJcLqMatchCellDTO nmatchCellDto = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_30.getCode(), MatchResultCrsEnum.CRS_30.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto1 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_03.getCode(), MatchResultCrsEnum.CRS_03.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto2 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_21.getCode(), MatchResultCrsEnum.CRS_21.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto3 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_12.getCode(), MatchResultCrsEnum.CRS_12.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
				ncrsBetCells.add(nmatchCellDto2);
				ncrsBetCells.add(nmatchCellDto3);
			}else if(qiuNum == 4) {
				DlJcLqMatchCellDTO nmatchCellDto =  new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_40.getCode(), MatchResultCrsEnum.CRS_40.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto1 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_04.getCode(), MatchResultCrsEnum.CRS_04.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto2 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_31.getCode(), MatchResultCrsEnum.CRS_31.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto3 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_13.getCode(), MatchResultCrsEnum.CRS_13.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto4 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_22.getCode(), MatchResultCrsEnum.CRS_22.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
				ncrsBetCells.add(nmatchCellDto2);
				ncrsBetCells.add(nmatchCellDto3);
				ncrsBetCells.add(nmatchCellDto4);
			}else if(qiuNum == 5) {
				DlJcLqMatchCellDTO nmatchCellDto =  new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_50.getCode(), MatchResultCrsEnum.CRS_50.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto1 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_05.getCode(), MatchResultCrsEnum.CRS_05.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto2 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_41.getCode(), MatchResultCrsEnum.CRS_41.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto3 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_14.getCode(), MatchResultCrsEnum.CRS_14.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto4 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_32.getCode(), MatchResultCrsEnum.CRS_32.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto5 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_23.getCode(), MatchResultCrsEnum.CRS_23.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
				ncrsBetCells.add(nmatchCellDto2);
				ncrsBetCells.add(nmatchCellDto3);
				ncrsBetCells.add(nmatchCellDto4);
				ncrsBetCells.add(nmatchCellDto5);
			}else if(qiuNum == 6) {
				DlJcLqMatchCellDTO nmatchCellDto =  new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_15.getCode(), MatchResultCrsEnum.CRS_15.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto1 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_51.getCode(), MatchResultCrsEnum.CRS_51.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto2 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_24.getCode(), MatchResultCrsEnum.CRS_24.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto3 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_42.getCode(), MatchResultCrsEnum.CRS_42.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto4 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_33.getCode(), MatchResultCrsEnum.CRS_33.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
				ncrsBetCells.add(nmatchCellDto2);
				ncrsBetCells.add(nmatchCellDto3);
				ncrsBetCells.add(nmatchCellDto4);
			}else if(qiuNum == 7) {
				DlJcLqMatchCellDTO nmatchCellDto = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_52.getCode(), MatchResultCrsEnum.CRS_52.getMsg(), null);
				DlJcLqMatchCellDTO nmatchCellDto1 = new DlJcLqMatchCellDTO(MatchResultCrsEnum.CRS_25.getCode(), MatchResultCrsEnum.CRS_25.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
			}
		}
		return crsBetPlay;
	}
	
	
	
	private List<Double> cc(MatchBasketBallBetPlayCellDTO crsBetPlay, MatchBasketBallBetPlayCellDTO ttgBetPlay,
			MatchBasketBallBetPlayCellDTO hhadBetPlay, MatchBasketBallBetPlayCellDTO hadBetPlay) {
		//比分的所有项
		List<DlJcLqMatchCellDTO> betCells = crsBetPlay.getBetCells();//比分的所有选项
		List<Double> allBetSumOdds = new ArrayList<Double>();
		for(DlJcLqMatchCellDTO dto: betCells) {
			String cellCode = dto.getCellCode();
			String[] arr = cellCode.split("");
			int m = 0;
			int n = 0;
			if(arr.length == 1) {
				m = Integer.parseInt(arr[0]);
			}else {
				m = Integer.parseInt(arr[0]);
				n = Integer.parseInt(arr[1]);
			}
			int sum = m+n;//总进球数
			int sub = m-n;//进球差数
			List<Double> allOdds = new ArrayList<Double>();
			String cellOdds = dto.getCellOdds();
			if(StringUtils.isNotBlank(cellOdds)) {
				allOdds.add(Double.valueOf(cellOdds));
			}
			//1.总进球
			if(ttgBetPlay != null) {
				List<DlJcLqMatchCellDTO> betCells2 = ttgBetPlay.getBetCells();
				int sucCode = sum > 7?7:sum;
				Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
				if(optional.isPresent()) {
					Double odds = Double.valueOf(optional.get().getCellOdds());//选中的总进球玩法的可用赔率
					if(allOdds.size() == 0) {
						allOdds.add(odds);
					}else {
						Double old = allOdds.remove(0);
						allOdds.add(Double.sum(old, odds));
					}
				}
			}
			//2。让球胜平负
			if(hhadBetPlay != null) {
				List<DlJcLqMatchCellDTO> betCells2 = hhadBetPlay.getBetCells();
				int sucCode = sub + Integer.valueOf(hhadBetPlay.getFixedodds());
				if(sucCode > 0) {
					sucCode = MatchResultHadEnum.HAD_H.getCode();
				}else {
					sucCode = MatchResultHadEnum.HAD_D.getCode();
				}
				final int sucCode1 = sucCode;
				Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode1).findFirst();
				if(optional.isPresent()) {
					Double odds = Double.valueOf(optional.get().getCellOdds());//选中的让球胜平负玩法的可用赔率
					Double old = allOdds.remove(0);
					allOdds.add(Double.sum(old, odds));
				}
			}
			//3.胜负
			boolean isH=false,isA=false;
			if(hadBetPlay != null) {
				List<DlJcLqMatchCellDTO> betCells2 = hadBetPlay.getBetCells();
				if(sum == 1) {//胜，负
					if(n ==0) {
						int sucCode = MatchResultHadEnum.HAD_H.getCode();
						Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
							isH=true;
						}
					}else {
						int sucCode = MatchResultHadEnum.HAD_A.getCode();
						Optional<DlJcLqMatchCellDTO> optional1 = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional1.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional1.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
							isA=true;
						}
					}
				}else {
					if(sub > 0) {//胜
						int sucCode = MatchResultHadEnum.HAD_H.getCode();
						Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
						}
					} else if(sub < 0) {//负
						int sucCode = MatchResultHadEnum.HAD_A.getCode();
						Optional<DlJcLqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
						}
					}
				}
			}
			allBetSumOdds.addAll(allOdds);
		}
		return allBetSumOdds;
	}
	

	/**
	 * 计算投注组合
	 * @param matchBellCellList
	 * @param betTypes
	 * @return
	 */
	private Map<String, List<String>> getBetIndexList(List<MatchBasketBallBetPlayDTO> matchBellCellList, String betTypes) {
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
		String[] split = betTypes.split(",");
		Map<String, List<String>> indexMap = new HashMap<String, List<String>>();
		for(String betType: split) {
			char[] charArray = betType.toCharArray();
			if(charArray.length == 2 && charArray[1] == '1') {
				int num = Integer.valueOf(String.valueOf(charArray[0]));
				//计算场次组合
				List<String> betIndexList = new ArrayList<String>();
				betNum1("", num, indexList, betIndexList);
				if(danIndexList.size() > 0) {
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
			}
		}
		return indexMap;
	}
	
	
	/**
	 * 计算组合
	 * @param str
	 * @param num
	 * @param list
	 * @param betList
	 */
	private static void betNum1(String str, int num, List<String> list, List<String> betList) {
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
	
	
}

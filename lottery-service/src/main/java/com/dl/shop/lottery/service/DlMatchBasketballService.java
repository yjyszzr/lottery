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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.BasketBallHILOLeverlEnum;
import com.dl.base.enums.MatchBasketBallResultHDCEnum;
import com.dl.base.enums.MatchBasketBallResultHILOEnum;
import com.dl.base.enums.MatchBasketResultHdEnum;
import com.dl.base.enums.MatchPlayTypeEnum;
import com.dl.base.enums.MatchResultCrsEnum;
import com.dl.base.enums.MatchResultHadEnum;
import com.dl.base.enums.MatchResultHafuEnum;
import com.dl.base.result.BaseResult;
import com.dl.base.service.AbstractService;
import com.dl.base.util.JSONHelper;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.BasketBallLeagueInfoDTO;
import com.dl.lottery.dto.DLLQBetInfoDTO;
import com.dl.lottery.dto.DlJcLqDateMatchDTO;
import com.dl.lottery.dto.DlJcLqMatchCellDTO;
import com.dl.lottery.dto.DlJcLqMatchDTO;
import com.dl.lottery.dto.DlJcLqMatchListDTO;
import com.dl.lottery.dto.DlJcLqMatchPlayDTO;
import com.dl.lottery.dto.DlJcZqMatchCellDTO;
import com.dl.lottery.dto.MatchBasketBallBetCellDTO;
import com.dl.lottery.dto.MatchBasketBallBetPlayCellDTO;
import com.dl.lottery.dto.MatchBasketBallBetPlayDTO;
import com.dl.lottery.dto.MatchBetPlayCellDTO;
import com.dl.lottery.dto.MatchBetPlayDTO;
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
import com.dl.shop.lottery.model.DlMatchBasketball;
import com.dl.shop.lottery.model.DlMatchPlayBasketball;
import com.dl.shop.lottery.model.DlResultBasketball;
import com.dl.shop.lottery.model.LotteryPlayClassify;
import com.dl.shop.lottery.model.TMatchBetMaxAndMinOddsList;

import lombok.extern.slf4j.Slf4j;

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
			if(this.isStop(matchPlay)) {
				continue;
			}
			
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
//			if(Long.valueOf(betEndTime) < Instant.now().getEpochSecond()) {
//				continue;
//			}
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
	
	public int getBetEndTime(Integer matchTime) {
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
		dto.setSingle(single);
		Set<String> keySet = jsonObj.keySet();
		DlJcZqMatchCellDTO homeCell = new DlJcZqMatchCellDTO(MatchBasketBallResultHDCEnum.HHD_H.getCode().toString(), MatchBasketBallResultHDCEnum.HHD_H.getMsg(), null);
		homeCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(6));
		dto.setHomeCell(homeCell);
		DlJcZqMatchCellDTO visitingCell = new DlJcZqMatchCellDTO(MatchBasketBallResultHDCEnum.HHD_H.getCode().toString(), MatchBasketBallResultHDCEnum.HHD_H.getMsg(), null);
		visitingCell.setCellSons(new ArrayList<DlJcZqMatchCellDTO>(6));
		dto.setVisitingCell(visitingCell);
		for(String key: keySet) {
			if(key.indexOf("l") == 0 && key.length() == 2) {
				String code = key.substring(1);//1-6 cell_code 进行编码
				String odds = jsonObj.getString(key);
				String name = BasketBallHILOLeverlEnum.getName(code);
				homeCell.getCellSons().add(new DlJcZqMatchCellDTO(code, name, odds));
			}else if(key.indexOf("w" ) == 0 && key.length() == 2) {
				String code = String.valueOf((Integer.valueOf(key.substring(1)) + 10));//11-16 cell_code 编码
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
		dto.setHomeCell(new DlJcZqMatchCellDTO(MatchBasketBallResultHILOEnum.L_SCORE.getCode().toString(), "小于"+fixedOdds+"分", hOdds));
		dto.setVisitingCell(new DlJcZqMatchCellDTO(MatchBasketBallResultHILOEnum.H_SCORE.getCode().toString(), "大于"+fixedOdds+"分", aOdds));
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
//		Map<String, List<MatchBetPlayCellDTO>> playCellMap = matchBetPlayMap.getPlayCellMap();
//		List<Double> minOddsList = matchBetPlayMap.getMinOddsList();
//		minOddsList.sort((item1,item2)->Double.compare(item1, item2));
//		logger.info("-----------最小赔率展示："+JSONHelper.bean2json(minOddsList));
		TMatchBetMaxAndMinOddsList tem = this.maxMoneyBetPlayCellsForLottery(playCellMap);
		List<Double> maxOddsList = tem.getMaxOddsList();
		List<Double> minOddsList = tem.getMinOddsList();
		long end2 = System.currentTimeMillis();
		logger.info("2计算投注排列后获取不同投注的赛事信息用时：" + (end2-end1)+ " - "+start);
		//计算投票综合信息核心算法
		Double totalMaxMoney = 0.0;
		Double totalMinMoney = Double.MAX_VALUE;
		int betNums = 0;
//		BetResultInfo betResult = new BetResultInfo();
		int ticketNum = 0;
//		double srcMoney = 2.0*param.getTimes();
		Double maxLotteryMoney = 0.0;
//		Map<String, Integer> cellNumsMap = matchBetPlayMap.getCellNumsMap();
//		Map<String, List<List<MatchBetPlayCellDTO>>> betPlayCellMap = new HashMap<String, List<List<MatchBetPlayCellDTO>>>();
		for(String betType: indexMap.keySet()) {
			char[] charArray = betType.toCharArray();
			int num = Integer.valueOf(String.valueOf(charArray[0]));
			List<String> betIndexList = indexMap.get(betType);
//			List<List<MatchBetPlayCellDTO>> result = new ArrayList<List<MatchBetPlayCellDTO>>(betIndexList.size());
			for(String str: betIndexList) {//所有注组合
				String[] strArr = str.split(",");
				Double maxMoney = 2.0*param.getTimes();
				Double minMoney = 2.0*param.getTimes();
//				List<String> playCodes = new ArrayList<String>(strArr.length);
				Integer betNum = 1;
				for(String item: strArr) {//单注组合
					MatchBasketBallBetPlayDTO betPlayDto = matchBellCellList.get(Integer.valueOf(item));
//					String playCode = betPlayDto.getPlayCode();
					Integer cellNums = betPlayDto.getMatchBetCells().stream().map(item1->item1.getBetCells().size()).reduce(Integer::sum).get();
					betNum*=cellNums;
//					playCodes.add(playCode);
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
//				List<MatchBetPlayCellDTO> dtos = new ArrayList<MatchBetPlayCellDTO>(0);
//				this.matchBetPlayCellsForLottery(playCodes.size(), playCellMap, playCodes, dtos, result);
				betNums+=betNum;
			}
//			betPlayCellMap.put(betType, result);
			//计算投票信息
			/*for(List<MatchBetPlayCellDTO> subList: result) {
				Integer oldBetNum = betResult.getBetNum();//记录原始值 
				this.betNumtemp(srcMoney, num, subList, subList.size(), betResult);
				ticketNum++;
				Double betMoney = (betResult.getBetNum() - oldBetNum)*param.getTimes()*2.0;
				if(betMoney > maxLotteryMoney) {
					maxLotteryMoney = betMoney;
				}
			}*/
			/*minMoney = 2.0*param.getTimes();
			double allOdds = minOddsList.subList(0, num).stream().reduce((odds,item)-> odds*=item).get();
			minMoney = minMoney*allOdds;*/
		}
		logger.info("***************最大预测奖金"+totalMaxMoney);
		logger.info("***************最小预测奖金"+totalMinMoney);
		logger.info("***************投注数："+betNums);
//		logger.info("***************投注数2："+betNums2);
		long end3 = System.currentTimeMillis();
		logger.info("3计算投注基础信息用时：" + (end3-end2)+ " - "+start);
		/*for(String betType: betPlayCellMap.keySet()) {
			char[] charArray = betType.toCharArray();
			int num = Integer.valueOf(String.valueOf(charArray[0]));
			List<List<MatchBetPlayCellDTO>> betIndexList = betPlayCellMap.get(betType);
			for(List<MatchBetPlayCellDTO> subList: betIndexList) {
				Integer oldBetNum = betResult.getBetNum();//记录原始值 
				this.betNumtemp(srcMoney, num, subList, subList.size(), betResult);
				ticketNum++;
				Double betMoney = (betResult.getBetNum() - oldBetNum)*param.getTimes()*2.0;
				if(betMoney > maxLotteryMoney) {
					maxLotteryMoney = betMoney;
				}
			}
		}*/
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
//		TMatchBetInfoWithMinOddsList tbml = new TMatchBetInfoWithMinOddsList();
//		List<Double> minList = new ArrayList<Double>(matchBellCellList.size());
		Map<String, List<MatchBasketBallBetPlayCellDTO>> playCellMap = new HashMap<String, List<MatchBasketBallBetPlayCellDTO>>(matchBellCellList.size());
//		Map<String, Double> minCellOddsMap = new HashMap<String, Double>(matchBellCellList.size());
//		Map<String, Integer> cellNumsMap = new HashMap<String, Integer>(matchBellCellList.size());
		matchBellCellList.forEach(betPlayDto->{
			String playCode = betPlayDto.getPlayCode();
			List<MatchBasketBallBetCellDTO> matchBetCells = betPlayDto.getMatchBetCells();
			List<MatchBasketBallBetPlayCellDTO> list = playCellMap.get(playCode);
//			Double minCellOdds = minCellOddsMap.get(playCode);
//			Integer cellNums = cellNumsMap.get(playCode);
			if(list == null) {
				list = new ArrayList<MatchBasketBallBetPlayCellDTO>(matchBetCells.size());
				playCellMap.put(playCode, list);
//				minCellOdds = Double.MAX_VALUE;
//				cellNums = 0;
			}
			
			for(MatchBasketBallBetCellDTO cell: matchBetCells) {
				MatchBasketBallBetPlayCellDTO playCellDto = new MatchBasketBallBetPlayCellDTO(betPlayDto);
				playCellDto.setPlayType(cell.getPlayType());
				List<DlJcLqMatchCellDTO> betCells = cell.getBetCells();
				playCellDto.setBetCells(betCells);
//				logger.info("=====cell.getFixedOdds()============  " + cell.getFixedOdds());
				playCellDto.setFixedodds(cell.getFixedOdds());
				list.add(playCellDto);
				/*if(betCells.size() == 1) {
					String cellOdds = betCells.get(0).getCellOdds();
					minCellOdds = Double.min(minCellOdds, Double.valueOf(cellOdds));
				}else {
					String cellOdds = betCells.stream().min((item1,item2)->Double.valueOf(item1.getCellOdds()).compareTo(Double.valueOf(item2.getCellOdds()))).get().getCellOdds();
					minCellOdds = Double.min(minCellOdds, Double.valueOf(cellOdds));
				}*/
			}
//			cellNums += matchBetCells.size();
//			minCellOddsMap.put(playCode, minCellOdds);
//			cellNumsMap.put(playCode, cellNums);
		});
		/*minList.addAll(minCellOddsMap.values());
		tbml.setMinOddsList(minList);
		tbml.setPlayCellMap(playCellMap);*/
//		tbml.setCellNumsMap(cellNumsMap);
		return playCellMap;
	}
	//计算混合玩法最大投注中奖金额
	private TMatchBetMaxAndMinOddsList maxMoneyBetPlayCellsForLottery(Map<String, List<MatchBasketBallBetPlayCellDTO>> playCellMap) {
		TMatchBetMaxAndMinOddsList tem = new TMatchBetMaxAndMinOddsList();
		List<Double> maxOdds = new ArrayList<Double>(playCellMap.size());
		List<Double> minOdds = new ArrayList<Double>(playCellMap.size());
		for(String playCode: playCellMap.keySet()) {
			List<MatchBasketBallBetPlayCellDTO> list = playCellMap.get(playCode);
			List<Double> allbetComOdds = this.allbetComOdds(list);
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
	private List<Double> allBasketBetComOdds(List<MatchBasketBallBetPlayCellDTO> list) {
		//比分
		Optional<MatchBasketBallBetPlayCellDTO> optionalcrs = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_CRS.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO crsBetPlay = optionalcrs.isPresent()?optionalcrs.get():null;
		//总进球
		Optional<MatchBasketBallBetPlayCellDTO> optionalttg = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_TTG.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO ttgBetPlay = optionalttg.isPresent()?optionalttg.get():null;
		//让球胜平负
		Optional<MatchBasketBallBetPlayCellDTO> optional2 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO hhadBetPlay = optional2.isPresent()?optional2.get():null;
		//胜平负
		Optional<MatchBasketBallBetPlayCellDTO> optional3 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO hadBetPlay = optional3.isPresent()?optional3.get():null;
//		logger.info(JSONHelper.bean2json(hadBetPlay));
		//半全场
		Optional<MatchBasketBallBetPlayCellDTO> optional4 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HAFU.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO hafuBetPlay = optional4.isPresent()?optional4.get():null;
		/*if(crsBetPlay == null && ttgBetPlay != null) {
			crsBetPlay = this.bb(ttgBetPlay);
		}
		if(crsBetPlay != null) {
			return this.cc(crsBetPlay, ttgBetPlay, hhadBetPlay, hadBetPlay, hafuBetPlay);
		}
		return this.cc2(hhadBetPlay, hadBetPlay, hafuBetPlay);*/
		
		List<Double> rst = new ArrayList<Double>();
		if(crsBetPlay != null) {
			List<Double> cc = this.cc(crsBetPlay, ttgBetPlay, hhadBetPlay, hadBetPlay, hafuBetPlay);
			rst.addAll(cc);
		}
		if(ttgBetPlay != null) {
			crsBetPlay = this.bb(ttgBetPlay);
			List<Double> cc = this.cc(crsBetPlay, ttgBetPlay, hhadBetPlay, hadBetPlay, hafuBetPlay);
			rst.addAll(cc);
		}
		if(hadBetPlay != null) {
			List<Double> c = this.cc2(hhadBetPlay, hadBetPlay, hafuBetPlay);
//			log.info("hadBetPlay is not null: "+ JSONHelper.bean2json(c));
			rst.addAll(c);
		}
		if(hafuBetPlay != null) {
			List<Double> c = this.cc2(hhadBetPlay, null, hafuBetPlay);
			rst.addAll(c);
		}
		if(hhadBetPlay != null) {
			List<Double> c = this.cc2(hhadBetPlay, null, null);
//			log.info("hadBetPlay is not null: "+ JSONHelper.bean2json(c));
			rst.addAll(c);
		}
		return rst;
		
	}	
	
	
	/**
	 * 计算混合玩法的排斥后的该场次的几种可能赔率
	 * @param list 混合玩法 同一场次的所有玩法选项
	 */
	private List<Double> allbetComOdds(List<MatchBasketBallBetPlayCellDTO> list) {
		//比分
		Optional<MatchBasketBallBetPlayCellDTO> optionalcrs = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_CRS.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO crsBetPlay = optionalcrs.isPresent()?optionalcrs.get():null;
		//总进球
		Optional<MatchBasketBallBetPlayCellDTO> optionalttg = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_TTG.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO ttgBetPlay = optionalttg.isPresent()?optionalttg.get():null;
		//让球胜平负
		Optional<MatchBasketBallBetPlayCellDTO> optional2 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO hhadBetPlay = optional2.isPresent()?optional2.get():null;
		//胜平负
		Optional<MatchBasketBallBetPlayCellDTO> optional3 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO hadBetPlay = optional3.isPresent()?optional3.get():null;
//		logger.info(JSONHelper.bean2json(hadBetPlay));
		//半全场
		Optional<MatchBasketBallBetPlayCellDTO> optional4 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HAFU.getcode())).findFirst();
		MatchBasketBallBetPlayCellDTO hafuBetPlay = optional4.isPresent()?optional4.get():null;
		/*if(crsBetPlay == null && ttgBetPlay != null) {
			crsBetPlay = this.bb(ttgBetPlay);
		}
		if(crsBetPlay != null) {
			return this.cc(crsBetPlay, ttgBetPlay, hhadBetPlay, hadBetPlay, hafuBetPlay);
		}
		return this.cc2(hhadBetPlay, hadBetPlay, hafuBetPlay);*/
		
		List<Double> rst = new ArrayList<Double>();
		if(crsBetPlay != null) {
			List<Double> cc = this.cc(crsBetPlay, ttgBetPlay, hhadBetPlay, hadBetPlay, hafuBetPlay);
			rst.addAll(cc);
		}
		if(ttgBetPlay != null) {
			crsBetPlay = this.bb(ttgBetPlay);
			List<Double> cc = this.cc(crsBetPlay, ttgBetPlay, hhadBetPlay, hadBetPlay, hafuBetPlay);
			rst.addAll(cc);
		}
		if(hadBetPlay != null) {
			List<Double> c = this.cc2(hhadBetPlay, hadBetPlay, hafuBetPlay);
//			log.info("hadBetPlay is not null: "+ JSONHelper.bean2json(c));
			rst.addAll(c);
		}
		if(hafuBetPlay != null) {
			List<Double> c = this.cc2(hhadBetPlay, null, hafuBetPlay);
			rst.addAll(c);
		}
		if(hhadBetPlay != null) {
			List<Double> c = this.cc2(hhadBetPlay, null, null);
//			log.info("hadBetPlay is not null: "+ JSONHelper.bean2json(c));
			rst.addAll(c);
		}
		return rst;
		
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
				}else if(sucCode < 0) {
					sucCode = MatchResultHadEnum.HAD_A.getCode();
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
			Integer fixNum = Integer.valueOf(hhadBetPlay.getFixedodds());
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
						if(ish && MatchResultHadEnum.HAD_H.getCode().equals(cellCode)) {
						/*	hList.forEach(item->Double.sum(item, odds));
							nhList.addAll(hList);*/
							for(Double item: hList) {
								nhList.add(Double.sum(item, odds));
							}
						}
						if(isd && MatchResultHadEnum.HAD_H.getCode().equals(cellCode)) {
							/*dList.forEach(item->Double.sum(item, odds));
							ndList.addAll(dList);*/
							for(Double item: dList) {
								ndList.add(Double.sum(item, odds));
							}
						}
						if(isa) {
							if(!MatchResultHadEnum.HAD_H.getCode().equals(cellCode)) {
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
							if(!MatchResultHadEnum.HAD_A.getCode().equals(cellCode)) {
								List<Double> tnhList = new ArrayList<Double>(hList);
								/*tnhList.forEach(item->Double.sum(item, odds));
								nhList.addAll(tnhList);*/
								for(Double item: tnhList) {
									nhList.add(Double.sum(item, odds));
								}
							}
						}
						if(isd && MatchResultHadEnum.HAD_A.getCode().equals(cellCode)) {
							/*dList.forEach(item->Double.sum(item, odds));
							ndList.addAll(dList);*/
							for(Double item: dList) {
								ndList.add(Double.sum(item, odds));
							}
						}
						if(isa && MatchResultHadEnum.HAD_A.getCode().equals(cellCode)) {
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

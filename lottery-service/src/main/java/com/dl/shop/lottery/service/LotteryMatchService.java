package com.dl.shop.lottery.service;
import java.io.IOException;
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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.BasketBallHILOLeverlEnum;
import com.dl.base.enums.MatchBasketBallResultHDCEnum;
import com.dl.base.enums.MatchBasketBallResultHILOEnum;
import com.dl.base.enums.MatchBasketPlayTypeEnum;
import com.dl.base.enums.MatchBasketResultHdEnum;
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
import com.dl.base.util.JSONHelper;
import com.dl.base.util.NetWorkUtil;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.DLBetLottoInfoDTO;
import com.dl.lottery.dto.DLBetMatchCellDTO;
import com.dl.lottery.dto.DLLQBetInfoDTO;
import com.dl.lottery.dto.DLZQBetInfoDTO;
import com.dl.lottery.dto.DLZQOrderLotteryBetInfoDTO;
import com.dl.lottery.dto.DlJcLqMatchCellDTO;
import com.dl.lottery.dto.DlJcZqDateMatchDTO;
import com.dl.lottery.dto.DlJcZqMatchCellDTO;
import com.dl.lottery.dto.DlJcZqMatchDTO;
import com.dl.lottery.dto.DlJcZqMatchListDTO;
import com.dl.lottery.dto.DlJcZqMatchPlayDTO;
import com.dl.lottery.dto.KeyValueDTO;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.lottery.dto.LeagueMatchResultDTO;
import com.dl.lottery.dto.LotteryMatchDTO;
import com.dl.lottery.dto.LotteryPrintDTO;
import com.dl.lottery.dto.MatchBasketBallBetPlayCellDTO;
import com.dl.lottery.dto.MatchBetCellDTO;
import com.dl.lottery.dto.MatchBetPlayCellDTO;
import com.dl.lottery.dto.MatchBetPlayDTO;
import com.dl.lottery.dto.MatchDateDTO;
import com.dl.lottery.dto.MatchInfoDTO;
import com.dl.lottery.dto.MatchInfoForTeamDTO;
import com.dl.lottery.dto.MatchMinuteAndScoreDTO;
import com.dl.lottery.dto.MatchTeamInfoDTO;
import com.dl.lottery.dto.MatchTeamInfosDTO;
import com.dl.lottery.dto.QueryMatchResultDTO;
import com.dl.lottery.enums.LotteryResultEnum;
import com.dl.lottery.enums.MatchStatusEnums;
import com.dl.lottery.param.DlJcZqMatchBetParam;
import com.dl.lottery.param.DlJcZqMatchListParam;
import com.dl.lottery.param.JCQueryParam;
import com.dl.lottery.param.QueryMatchParam;
import com.dl.lottery.param.QueryMatchParamByType;
import com.dl.member.api.ISwitchConfigService;
import com.dl.member.api.IUserCollectService;
import com.dl.member.param.UserDealActionParam;
import com.dl.order.api.IOrderDetailService;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.OrderDetailDataDTO;
import com.dl.order.dto.OrderInfoAndDetailDTO;
import com.dl.order.dto.OrderInfoDTO;
import com.dl.order.param.DateStrParam;
import com.dl.shop.lottery.core.LocalWeekDate;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryPlayClassifyMapper;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.dao2.DlLeagueMatchResultMapper;
import com.dl.shop.lottery.dao2.DlLeagueTeamMapper;
import com.dl.shop.lottery.dao2.DlMatchBasketballMapper;
import com.dl.shop.lottery.dao2.DlMatchLiveMapper;
import com.dl.shop.lottery.dao2.DlResultBasketballMapper;
import com.dl.shop.lottery.dao2.LotteryMatchMapper;
import com.dl.shop.lottery.dao2.LotteryMatchPlayMapper;
import com.dl.shop.lottery.model.BetResultInfo;
import com.dl.shop.lottery.model.DlLeagueInfo;
import com.dl.shop.lottery.model.DlLeagueMatchResult;
import com.dl.shop.lottery.model.DlLeagueTeam;
import com.dl.shop.lottery.model.DlMatchBasketball;
import com.dl.shop.lottery.model.DlMatchLive;
import com.dl.shop.lottery.model.DlResultBasketball;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.model.LotteryMatchPlay;
import com.dl.shop.lottery.model.LotteryPlayClassify;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.model.TMatchBetMaxAndMinOddsList;
import com.dl.shop.lottery.utils.PlayTypeUtil;

import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

@Service
//@Transactional
@Slf4j
public class LotteryMatchService extends AbstractService<LotteryMatch> {
    
	private final static Logger logger = Logger.getLogger(LotteryMatchService.class);
	@Resource
    private LotteryMatchMapper lotteryMatchMapper;
	
	@Resource
	private LotteryMatchPlayMapper lotteryMatchPlayMapper;
	
	@Resource
	private DlMatchLiveMapper dlMatchLiveMapper;
	
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
	private IUserCollectService	iUserCollectService;
	
	@Resource
	private IOrderService orderService;
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	
	@Resource
	private LotteryPlayClassifyMapper lotteryPlayClassifyMapper;
	
	@Resource
	private  DlLeagueTeamMapper dlLeagueTeamMapper;
	
    @Resource
    private DlLeagueMatchResultMapper dlLeagueMatchResultMapper;
    
    @Resource
    private DlMatchBasketballMapper dlMatchBasketballMapper;
    
    @Resource
    private DlMatchBasketballService dlMatchBasketballService;
    
    @Resource
    private DlMatchLiveService dlMatchLiveService;
    
	@Value("${match.url}")
	private String matchUrl;
	
	@Resource
	private ISwitchConfigService iSwitchConfigService;
	
	/*@Value("${spring.datasource.druid.url}")
	private String dbUrl;
	
	@Value("${spring.datasource.druid.username}")
	private String dbUserName;
	
	@Value("${spring.datasource.druid.password}")
	private String dbPass;
	
	@Value("${spring.datasource.druid.driver-class-name}")
	private String dbDriver;*/
	
	private final static String MATCH_RESULT_OVER = "已完成";
	private final static String MATCH_RESULT_CANCEL = "取消";
	
	private final static String CACHE_MATCH_LIST_KEY = "match_list_key";
	private final static long MATCH_EXPRICE_MINUTE = 10;
	private final static String CACHE_MATCH_PLAY_LIST_KEY = "match_play_List_key";

	private void refreshCache() {
		/*List<LotteryMatch> matchList = lotteryMatchMapper.getMatchList(null);
		if(!CollectionUtils.isEmpty(matchList)) {
			List<Integer> changciIds = matchList.stream().map(match->match.getChangciId()).collect(Collectors.toList());
			List<LotteryMatchPlay> matchPlayList = lotteryMatchPlayMapper.matchPlayListByChangciIds(changciIds.toArray(new Integer[changciIds.size()]), null);
			String matchListStr = JSONHelper.bean2json(matchList);
			stringRedisTemplate.opsForValue().set(CACHE_MATCH_LIST_KEY, matchListStr);
			String matchPlayStr = JSONHelper.bean2json(matchPlayList);
			stringRedisTemplate.opsForValue().set(CACHE_MATCH_PLAY_LIST_KEY, matchPlayStr);
		}*/
	}
	public DlJcZqMatchListDTO getMatchList1(DlJcZqMatchListParam param) {
		long start = System.currentTimeMillis();
		String leagueId = param.getLeagueId();
//		String playTypeStr = param.getPlayType();
//		Integer playType = Integer.parseInt(playTypeStr);
		DlJcZqMatchListDTO dlJcZqMatchListDTO = null;
		String matchListStr = null;
		try {
			matchListStr = stringRedisTemplate.opsForValue().get(CACHE_MATCH_LIST_KEY);
		} catch (Exception e1) {
		}
		if(StringUtils.isBlank(matchListStr)) {
			param.setLeagueId("");
			dlJcZqMatchListDTO = this.getMatchList(param);
			if(dlJcZqMatchListDTO.getPlayList().size() > 0) {
				matchListStr = JSONHelper.bean2json(dlJcZqMatchListDTO);
				stringRedisTemplate.opsForValue().set(CACHE_MATCH_LIST_KEY, matchListStr, MATCH_EXPRICE_MINUTE, TimeUnit.MINUTES);
			}
		}else {
			try {
				dlJcZqMatchListDTO = JSONHelper.getSingleBean(matchListStr, DlJcZqMatchListDTO.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long end1 = System.currentTimeMillis();
		logger.info("==============getmatchlist1 数据获取用时 ："+(end1-start) + " playType="+param.getPlayType());
		if(StringUtils.isNotBlank(leagueId)) {
			final List<String> leagueIds = Arrays.asList(leagueId.split(","));
			List<DlJcZqDateMatchDTO> playList = dlJcZqMatchListDTO.getPlayList();
			List<DlJcZqMatchDTO> hotPlayList = dlJcZqMatchListDTO.getHotPlayList();
			int num = 0;
			logger.info("  leagueIds size = "+ leagueIds.size());
			if(playList.size() > 0) {
				for(DlJcZqDateMatchDTO play: playList) {
					List<DlJcZqMatchDTO> playList2 = play.getPlayList();
					List<DlJcZqMatchDTO> nPlaylist = playList2.stream().filter(match->leagueIds.contains(match.getLeagueId())).collect(Collectors.toList());
					play.setPlayList(nPlaylist);
					num += nPlaylist.size();
				}
			}
			if(hotPlayList.size() > 0) {
				List<DlJcZqMatchDTO> nPlaylist = hotPlayList.stream().filter(match->leagueIds.contains(match.getLeagueId())).collect(Collectors.toList());
				dlJcZqMatchListDTO.setHotPlayList(nPlaylist);
				num+=nPlaylist.size();
			}
			dlJcZqMatchListDTO.setAllMatchCount(num+"");
		}
		long end = System.currentTimeMillis();
		logger.info("==============getmatchlist1 对象过滤用时 ："+(end-start) + " playType="+param.getPlayType() + "  leagueId="+leagueId);
		return dlJcZqMatchListDTO;
	}
    /**
     * 获取赛事列表
     * @param param
     * @return
     */
	public DlJcZqMatchListDTO getMatchList(DlJcZqMatchListParam param) {
		long start = System.currentTimeMillis();
		DlJcZqMatchListDTO dlJcZqMatchListDTO = new DlJcZqMatchListDTO();
		List<LotteryMatch> matchList = lotteryMatchMapper.getMatchList(param.getLeagueId());
		if(matchList == null || matchList.size() == 0) {
			return dlJcZqMatchListDTO;
		}
		List<Integer> changciIds = matchList.stream().map(match->match.getChangciId()).collect(Collectors.toList());
		String playType = param.getPlayType();
		Map<Integer, List<DlJcZqMatchPlayDTO>> matchPlayMap = new HashMap<Integer, List<DlJcZqMatchPlayDTO>>();
		if("7".equals(playType)) {
			List<LotteryMatchPlay> hmatchPlayList = lotteryMatchPlayMapper.matchPlayListByChangciIds(changciIds.toArray(new Integer[changciIds.size()]), "1");
			List<LotteryMatchPlay> matchPlayList = lotteryMatchPlayMapper.matchPlayListByChangciIds(changciIds.toArray(new Integer[changciIds.size()]), "2");
			Map<Integer, LotteryMatchPlay> playMap = new HashMap<Integer, LotteryMatchPlay>(matchPlayList.size());
			matchPlayList.forEach(item->{
				playMap.put(item.getChangciId(), item);
			});
			for(LotteryMatchPlay matchPlay: hmatchPlayList) {
				if(this.isStop(matchPlay)) {
					continue;
				}
				Integer changciId = matchPlay.getChangciId();
				LotteryMatchPlay lotteryMatchPlay = playMap.get(changciId);
				if(lotteryMatchPlay == null)continue;
				String playContent = matchPlay.getPlayContent();
				JSONObject hhadJo = JSON.parseObject(playContent);
				String fixedodds = hhadJo.getString("fixedodds");
				if(StringUtils.isBlank(fixedodds)) {
					continue;
				}
				String playContent2 = lotteryMatchPlay.getPlayContent();
				JSONObject hadJo = JSON.parseObject(playContent2);
				DlJcZqMatchPlayDTO matchPlayDto = new DlJcZqMatchPlayDTO();
				if(Integer.parseInt(fixedodds) == 1) {
					matchPlayDto.setHomeCell(new DlJcZqMatchCellDTO("32", "主不败", hhadJo.getString("h")));
					matchPlayDto.setVisitingCell(new DlJcZqMatchCellDTO("30", "主败", hadJo.getString("a")));
				} else if(Integer.parseInt(fixedodds) == -1) {
					matchPlayDto.setVisitingCell(new DlJcZqMatchCellDTO("33", "主不胜", hhadJo.getString("a")));
					matchPlayDto.setHomeCell(new DlJcZqMatchCellDTO("31", "主胜", hadJo.getString("h")));
				}else {
					continue;
				}
				List<DlJcZqMatchPlayDTO> dlJcZqMatchPlayDTOs = matchPlayMap.get(changciId);
				if(dlJcZqMatchPlayDTOs == null){
					dlJcZqMatchPlayDTOs = new ArrayList<DlJcZqMatchPlayDTO>();
					matchPlayMap.put(changciId, dlJcZqMatchPlayDTOs);
				}
				matchPlayDto.setPlayType(MatchPlayTypeEnum.PLAY_TYPE_TSO.getcode());
				matchPlayDto.setFixedOdds(fixedodds);
				dlJcZqMatchPlayDTOs.add(matchPlayDto);
			}
		}else {
			List<LotteryMatchPlay> matchPlayList = lotteryMatchPlayMapper.matchPlayListByChangciIds(changciIds.toArray(new Integer[changciIds.size()]), "6".equals(playType)?"":playType);
			for(LotteryMatchPlay matchPlay: matchPlayList) {
				if(this.isStop(matchPlay)) {
					continue;
				}
				Integer playType2 = matchPlay.getPlayType();
				if("6".equals(playType) && playType2 == 7) {
					continue;
				}
				Integer changciId = matchPlay.getChangciId();
				DlJcZqMatchPlayDTO matchPlayDto = this.initDlJcZqMatchCell(matchPlay);
				if(matchPlayDto == null) {
					continue;
				}
				List<DlJcZqMatchPlayDTO> dlJcZqMatchPlayDTOs = matchPlayMap.get(changciId);
				if(dlJcZqMatchPlayDTOs == null){
					dlJcZqMatchPlayDTOs = new ArrayList<DlJcZqMatchPlayDTO>();
					matchPlayMap.put(changciId, dlJcZqMatchPlayDTOs);
				}
				dlJcZqMatchPlayDTOs.add(matchPlayDto);
			}
		}
		long end1 = System.currentTimeMillis();
		logger.info("==============getmatchlist 准备用时 ："+(end1-start) + " playType="+param.getPlayType());
		dlJcZqMatchListDTO = this.getMatchListDTO(matchList, playType, matchPlayMap);
		long end = System.currentTimeMillis();
		logger.info("==============getmatchlist 对象转化用时 ："+(end-end1) + " playType="+param.getPlayType());
		logger.info("==============getmatchlist 用时 ："+(end-start) + " playType="+param.getPlayType());
	    return dlJcZqMatchListDTO;
	}
	//判断是否停售
	private boolean isStop(LotteryMatchPlay matchPlay) {
		String playContent = matchPlay.getPlayContent();
		JSONObject jsonObj = JSON.parseObject(playContent);
		String cbtValue = jsonObj.getString("cbt");
		if("2".equals(cbtValue)) {
			Boolean isStop = Boolean.TRUE;
			/****************20180820 据抓取工程师说，停售时（00-09）,cbt也是2，我们不能隐藏赛事因此增加逻辑**************/
			Integer changciId= matchPlay.getChangciId();
			DlMatchLive matchLive = dlMatchLiveMapper.getByChangciId(changciId);
			if(matchLive!=null&&!StringUtils.isEmpty(matchLive.getMatchLiveInfo())){			
				String matchLiveInfo = matchLive.getMatchLiveInfo();
				JSONObject matchLiveJsonObj = JSON.parseObject(matchLiveInfo);
				String matchStatus = matchLiveJsonObj.getString("match_status");
                if("Fixture".equalsIgnoreCase(matchStatus)){
					isStop = Boolean.FALSE;
				}
			}
			/****************20180820 据抓取工程师说，停售时（00-09）,cbt也是2，我们不能隐藏赛事因此增加逻辑**************/
			return isStop;
		}
		return false;
	}
	
	private DlJcZqMatchListDTO getMatchListDTO(List<LotteryMatch> matchList, String playType,	Map<Integer, List<DlJcZqMatchPlayDTO>> matchPlayMap) {
		int shutDownBetValue = lotteryPrintMapper.shutDownBetValue();
		DlJcZqMatchListDTO dlJcZqMatchListDTO = new DlJcZqMatchListDTO();
		Map<String, DlJcZqDateMatchDTO> map = new HashMap<String, DlJcZqDateMatchDTO>();
		Integer totalNum = 0;
		Integer betPreTime = this.getBetPreTime();
//		Locale defaultLocal = Locale.getDefault();
		for(LotteryMatch match: matchList) {
			Date matchTimeDate = match.getMatchTime();
			Instant instant = matchTimeDate.toInstant();
			int matchTime = Long.valueOf(instant.getEpochSecond()).intValue();
			int betEndTime = this.getBetEndTime(matchTime, betPreTime);
			
			/*LocalDateTime betendDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(betEndTime), ZoneId.systemDefault());
			LocalDateTime matchDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			int matchWeekDay = matchDateTime.getDayOfWeek().getValue();*/
			/*
			int matchHour = matchDateTime.getHour();
			int betEndTime = matchTime - betPreTime;
			LocalDateTime showDate = LocalDateTime.ofInstant(match.getShowTime().toInstant(), ZoneId.systemDefault());
			//今天展示第二天比赛时间
//			if(betendDateTime.toLocalDate().isAfter(LocalDate.now()) && LocalDate.now().isEqual(showDate.toLocalDate())) {
			if(betendDateTime.toLocalDate().isAfter(LocalDate.now())) {
				if(matchWeekDay < 6 && matchHour < 9) {
					betEndTime = Long.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 00, 00)).toInstant(ZoneOffset.ofHours(8)).getEpochSecond()).intValue();
				} else if(matchWeekDay > 5 && matchHour < 9 && matchHour > 0) {
					betEndTime = Long.valueOf(LocalDateTime.of(betendDateTime.toLocalDate(), LocalTime.of(00, 00, 00)).toInstant(ZoneOffset.ofHours(8)).getEpochSecond()).intValue();
				}
			}*/
			//0-9点的赛事在当天不能投注
			boolean hideMatch = this.isHideMatch(betEndTime, matchTime);
			if(hideMatch) {
				continue;
			}
			/*LocalTime localTime = LocalTime.now(ZoneId.systemDefault());
	        int nowHour = localTime.getHour();
	        int betHour = betendDateTime.getHour();
			if(betendDateTime.toLocalDate().isEqual(LocalDate.now())){
				if(nowHour < 9 && betHour < 9) {
					if(matchWeekDay < 6) {
						continue;
					} else if(matchWeekDay > 5 && betHour > 0 ) {//周六日的1点之后
						continue;
					}
				} else if(nowHour > 22 && betHour > 22) {//23点的比赛不再展示
					continue;
				}
			}*/
			//投注结束
			if(Long.valueOf(betEndTime) < Instant.now().getEpochSecond()) {
				continue;
			}
			DlJcZqMatchDTO matchDto = new DlJcZqMatchDTO();
			matchDto.setIsShutDown(shutDownBetValue);
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
			List<DlJcZqMatchPlayDTO> matchPlays = matchPlayMap.get(match.getChangciId());
			if(matchPlays == null || matchPlays.size() == 0) {
				continue;
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
				dlJcZqMatchDTO.setSortMatchDay(matchDay);
				dlJcZqMatchDTO.setMatchDay(showMatchDay);
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
			value.getPlayList().sort((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode()));
			dlJcZqMatchListDTO.getPlayList().add(value);
		});
		dlJcZqMatchListDTO.getHotPlayList().sort((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode()));
		dlJcZqMatchListDTO.getPlayList().sort((item1,item2)->item1.getSortMatchDay().compareTo(item2.getSortMatchDay()));
		dlJcZqMatchListDTO.setAllMatchCount(totalNum.toString());
		dlJcZqMatchListDTO.setLotteryClassifyId(1);
		LotteryPlayClassify playClassify = lotteryPlayClassifyMapper.getPlayClassifyByPlayType(1, Integer.parseInt(playType));
		Integer lotteryPlayClassifyId = playClassify == null?Integer.parseInt(playType):playClassify.getLotteryPlayClassifyId();
		dlJcZqMatchListDTO.setLotteryPlayClassifyId(lotteryPlayClassifyId);
		return dlJcZqMatchListDTO;
	}
	
	/**
	 * 初始化球赛类型投注选项
	 * @param dto
	 */
	private DlJcZqMatchPlayDTO initDlJcZqMatchCell(LotteryMatchPlay matchPlay) {
		DlJcZqMatchPlayDTO dto = new DlJcZqMatchPlayDTO();
		dto.setPlayContent(matchPlay.getPlayContent());
		dto.setPlayType(matchPlay.getPlayType());
		Integer playType = matchPlay.getPlayType();
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
		if(dto != null) {
			dto.setPlayContent(null);
		}
		return dto;
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
				matchCells.add(new DlJcZqMatchCellDTO(code, name+"球", odds));
			}
		}
		if(CollectionUtils.isNotEmpty(matchCells)) {
			matchCells.sort((cell1,cell2)->cell1.getCellCode().compareTo(cell2.getCellCode()));
			dto.setMatchCells(matchCells);
		} else {
			dto = null;
		}
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
		matchCells.sort((cell1,cell2)->cell2.getCellCode().compareTo(cell1.getCellCode()));
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
//	@Transactional
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
//		logger.info(lotteryMatchs.toString());
		
		//保存赛事数据
		saveMatchData(lotteryMatchs, matchPlays);
		//刷新缓存
		this.refreshCache();
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
			List<DlLeagueInfo> legueInfos = leagueInfoService.findAll();
			Map<String, Integer> leagueMap = new HashMap<String, Integer>(legueInfos.size());
			for(DlLeagueInfo league: legueInfos) {
				Integer leagueId = league.getLeagueId();
				String leagueAddr = league.getLeagueAddr();
				leagueMap.put(leagueAddr, leagueId);
			}
			for (Map.Entry<String, JSONObject> entry : matchs.entrySet()) {
				JSONObject jo = entry.getValue();
				Integer changciId = Integer.parseInt(jo.getString("id"));
				Integer leagueId = leagueMap.get(jo.getString("l_cn_abbr"));
				if(leagueId == null) {
					leagueId = this.getLeagueId(changciId);
				}
				LotteryMatch lotteryMatch = new LotteryMatch();
				lotteryMatch.setLeagueId(leagueId);
				lotteryMatch.setLeagueName(jo.getString("l_cn"));
				lotteryMatch.setLeagueAddr(jo.getString("l_cn_abbr"));
				lotteryMatch.setChangciId(changciId);
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
	private Integer getLeagueId(Integer changciId) {
		Integer leagueId = null;
		JSONObject singleMatch = this.getSingleMatch(changciId);
		if(singleMatch != null) {
			leagueId = singleMatch.getInteger("l_id_dc");
		}
		return leagueId;
	}
	/**
	 * 获取单场赛事信息
	 * @param changciId
	 * @return
	 */
	private JSONObject getSingleMatch(Integer changciId) {
		String requestUrl = "http://i.sporttery.cn/api/fb_match_info/get_match_info";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mid", changciId);
		String json = NetWorkUtil.doGet(requestUrl, map, "UTF-8");
	    if (json.contains("error")) {
	        throw new ServiceException(RespStatusEnum.FAIL.getCode(), changciId + "赛事查询失败");
	    }
	    JSONObject jsonObject = JSONObject.parseObject(json);
	    JSONObject jo = jsonObject.getJSONObject("result");
	    return jo;
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
	private LotteryMatchPlay getLotteryMatchPlayData(JSONObject matchPlay, Integer changciId, String playType){
		if(null == matchPlay) return null;
		LotteryMatchPlay lotteryMatchPlay = new LotteryMatchPlay();
		lotteryMatchPlay.setChangciId(changciId);
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
								LotteryMatchPlay lotteryMatchPlay = getLotteryMatchPlayData((JSONObject)map.get(lotteryMatch.getChangciId().toString()), lotteryMatch.getChangciId(), map.get("playType").toString());
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
				LotteryMatchPlay existPlay = lotteryMatchPlayMapper.lotteryMatchPlayByChangciIdAndPlayType(play.getChangciId(), play.getPlayType());
				if(existPlay == null) {
					lotteryMatchPlayMapper.insert(play);
				} else {
					lotteryMatchPlayMapper.updatePlayContent(play);
				}
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
	private void betNum(DLBetMatchCellDTO str, int num, List<MatchBetPlayCellDTO> list, List<DLBetMatchCellDTO> betList, Map<Integer, String> playTypeNameMap) {
		LinkedList<MatchBetPlayCellDTO> link = new LinkedList<MatchBetPlayCellDTO>(list);
		while(link.size() > 0) {
			MatchBetPlayCellDTO remove = link.remove(0);
			String changci = remove.getChangci();
//			String playCode = remove.getPlayCode();
			String playType = remove.getPlayType();
			String playName = playTypeNameMap.get(Integer.valueOf(playType));
			if(Integer.valueOf(playType).equals(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode())) {
				String fixedodds = remove.getFixedodds();
				playName = StringUtils.isBlank(fixedodds)?playName:("["+fixedodds+"]"+playName);
			}
			List<DlJcZqMatchCellDTO> betCells = remove.getBetCells();
			for(DlJcZqMatchCellDTO betCell: betCells) {
// 				String cellCode = betCell.getCellCode();
				DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
				dto.setPlayType(playType);
				Double amount = str.getAmount()*Double.valueOf(betCell.getCellOdds());
				dto.setAmount(Double.valueOf(String.format("%.2f", amount)));
				String betContent = str.getBetContent() + changci + "(" + playName + "_"+ betCell.getCellName() + " " + betCell.getCellOdds() +")X";
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
					betNum(dto,num-1,link, betList, playTypeNameMap);
				}
			}
		}
	}
	private void betNum2(DLBetMatchCellDTO str, int num, List<MatchBetPlayCellDTO> list, List<DLBetMatchCellDTO> betList, Map<Integer, String> playTypeNameMap) {
		LinkedList<MatchBetPlayCellDTO> link = new LinkedList<MatchBetPlayCellDTO>(list);
		while(link.size() > 0) {
			MatchBetPlayCellDTO remove = link.remove(0);
			String changci = remove.getChangci();
			String playType = remove.getPlayType();
			String playName = playTypeNameMap.get(Integer.valueOf(playType));
			if(Integer.valueOf(playType).equals(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode())) {
				String fixedodds = remove.getFixedodds();
				playName = StringUtils.isBlank(fixedodds)?playName:("["+fixedodds+"]"+playName);
			}
			List<DlJcZqMatchCellDTO> betCells = remove.getBetCells();
			for(DlJcZqMatchCellDTO betCell: betCells) {
				DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
				dto.setPlayType(playType);
				String cellOdds = betCell.getCellOdds();
				cellOdds = StringUtils.isBlank(cellOdds)?"-":cellOdds;
				String betContent = str.getBetContent() + changci + "(" + playName + "_"+ betCell.getCellName() + " " + cellOdds +")X";
				
				if(num == 1) {
					betContent = betContent.substring(0, betContent.length()-1);
				}
				dto.setBetContent(betContent);
				dto.setBetType(str.getBetType());
				dto.setTimes(str.getTimes());
				if(num == 1) {
					betList.add(dto);
				}else {
					betNum2(dto,num-1,link, betList, playTypeNameMap);
				}
			}
		}
	}
	
	private void betBasketNum2(DLBetMatchCellDTO str, int num, List<MatchBasketBallBetPlayCellDTO> list, List<DLBetMatchCellDTO> betList, Map<Integer, String> playTypeNameMap) {
		LinkedList<MatchBasketBallBetPlayCellDTO> link = new LinkedList<MatchBasketBallBetPlayCellDTO>(list);
		while(link.size() > 0) {
			MatchBasketBallBetPlayCellDTO remove = link.remove(0);
			String changci = remove.getChangci();
			String playType = remove.getPlayType();
			String playName = playTypeNameMap.get(Integer.valueOf(playType));
			if(Integer.valueOf(playType).equals(MatchBasketPlayTypeEnum.PLAY_TYPE_HDC.getcode())) {
				String fixedodds = remove.getFixedodds();
				playName = StringUtils.isBlank(fixedodds)?playName:("["+fixedodds+"]"+playName);
			}
			List<DlJcLqMatchCellDTO> betCells = remove.getBetCells();
			for(DlJcLqMatchCellDTO betCell: betCells) {
				DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
				dto.setPlayType(playType);
				String cellOdds = betCell.getCellOdds();
				cellOdds = StringUtils.isBlank(cellOdds)?"-":cellOdds;
				String betContent = str.getBetContent() + changci + "(" + playName + "_"+ betCell.getCellName() + " " + cellOdds +")X";
				
				if(num == 1) {
					betContent = betContent.substring(0, betContent.length()-1);
				}
				dto.setBetContent(betContent);
				dto.setBetType(str.getBetType());
				dto.setTimes(str.getTimes());
				if(num == 1) {
					betList.add(dto);
				}else {
					betBasketNum2(dto,num-1,link, betList, playTypeNameMap);
				}
			}
		}
	}
	
	private void betNumtemp(Double srcAmount, int num, List<MatchBetPlayCellDTO> subList, int indexSize, BetResultInfo betResult) {
//		LinkedList<Integer> link = new LinkedList<Integer>(subListIndex);
		while(indexSize > 0) {
			Integer index = subList.size() - indexSize;
			indexSize--;
			MatchBetPlayCellDTO remove = subList.get(index);
			List<DlJcZqMatchCellDTO> betCells = remove.getBetCells();
			for(DlJcZqMatchCellDTO betCell: betCells) {
				Double amount = srcAmount*Double.valueOf(betCell.getCellOdds());
				if(num == 1) {
					betResult.setBetNum(betResult.getBetNum()+1);
					Double minBonus = betResult.getMinBonus();
					if(Double.compare(minBonus, amount) > 0 || minBonus.equals(0.0)) {
						betResult.setMinBonus(amount);
					}
				}else {
					betNumtemp(amount,num-1,subList, indexSize, betResult);
				}
			}
		}
	}
	/*private void betMaxAmount(Double srcAmount, int num, List<MatchBetPlayCellDTO> subList, int indexSize, BetResultInfo betResult) {
		//LinkedList<Integer> link = new LinkedList<Integer>(subListIndex);
		while(indexSize > 0) {
			Integer index = subList.size() - indexSize;
			indexSize--;
			MatchBetPlayCellDTO remove = subList.get(index);
			List<DlJcZqMatchCellDTO> betCells = remove.getBetCells();
			for(DlJcZqMatchCellDTO betCell: betCells) {
				Double amount = srcAmount*Double.valueOf(betCell.getCellOdds());
				DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
				dto.setAmount(amount);
				if(num == 1) {
					betResult.setMaxBonus(betResult.getMaxBonus() + amount);
				}else {
					betMaxAmount(amount,num-1,subList,indexSize, betResult);
				}
			}
		}
	}*/
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
		long start = System.currentTimeMillis();
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
		long end1 = System.currentTimeMillis();
		logger.info("1计算投注排列用时：" + (end1-start)+ " - "+start);
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
						playCellDto.setFixedodds(cell.getFixedOdds());
						list.add(playCellDto);
					}
				});
				List<MatchBetPlayCellDTO> dtos = new ArrayList<MatchBetPlayCellDTO>(0);
				matchBetPlayCellsForLottery(playCodes.size(), playCellMap, playCodes, dtos, result);
			}
			betPlayCellMap.put(betType, result);
		}
		long end2 = System.currentTimeMillis();
		logger.info("2计算投注排列后获取不同投注的赛事信息用时：" + (end2-end1)+ " - "+start);
		List<LotteryPlayClassify> allPlays = lotteryPlayClassifyMapper.getAllPlays(param.getLotteryClassifyId());
		Map<Integer, String> playTypeNameMap = new HashMap<Integer, String>();
    	if(!Collections.isEmpty(allPlays)) {
    		for(LotteryPlayClassify type: allPlays) {
    			playTypeNameMap.put(type.getPlayType(), type.getPlayName());
    		}
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
				this.betNum(dto, num, subList, betCellList1, playTypeNameMap);
				this.betNum(dto, num, maxList, maxBetCellList1, playTypeNameMap);
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
		long end3 = System.currentTimeMillis();
		logger.info("3计算投注基础信息用时：" + (end3-end2)+ " - "+start);
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
		betInfoDTO.setMaxBonus(String.format("%.2f", maxBonus));
		betInfoDTO.setMinBonus(String.format("%.2f", minBonus));
		betInfoDTO.setTimes(param.getTimes());
		betInfoDTO.setBetNum(betCellList.size());
		betInfoDTO.setTicketNum(lotteryPrints.size());
		Double money = betCellList.size()*param.getTimes()*2.0;
		betInfoDTO.setMoney(String.format("%.2f", money));
		betInfoDTO.setBetType(param.getBetType());
		betInfoDTO.setPlayType(param.getPlayType());
		betInfoDTO.setBetCells(orderLotteryBetInfos);//投注方案
		betInfoDTO.setLotteryPrints(lotteryPrints);
		//所有选项的最后一个场次编码
		String issue = matchBellCellList.stream().max((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode())).get().getPlayCode();
		betInfoDTO.setIssue(issue);
		long end4 = System.currentTimeMillis();
		logger.info("4计算投注统计信息用时：" + (end4-end3)+ " - "+start);
		logger.info("5计算投注信息用时：" + (end4-start)+ " - "+start);
		return betInfoDTO;
	}
	/**
	 * 订单使用出票方案获取
	 * @param param
	 * @return
	 */
	public DLZQBetInfoDTO getBetProgram(DlJcZqMatchBetParam param) {
		long start = System.currentTimeMillis();
		List<MatchBetPlayDTO> matchBellCellList = param.getMatchBetPlays();
		String betTypes = param.getBetType();
		Map<String, List<String>> indexMap = this.getBetIndexList(matchBellCellList, betTypes);
		long end1 = System.currentTimeMillis();
		logger.info("1计算投注排列用时：" + (end1-start)+ " - "+start);
		Map<String, List<MatchBetPlayCellDTO>> playCellMap = this.getMatchBetPlayMap(matchBellCellList);
		long end2 = System.currentTimeMillis();
		logger.info("2计算预出票获取不同投注的赛事信息用时：" + (end2-end1)+ " - "+start);
		//
		List<LotteryPlayClassify> allPlays = lotteryPlayClassifyMapper.getAllPlays(param.getLotteryClassifyId());
		Map<Integer, String> playTypeNameMap = new HashMap<Integer, String>();
		if(!Collections.isEmpty(allPlays)) {
			for(LotteryPlayClassify type: allPlays) {
				playTypeNameMap.put(type.getPlayType(), type.getPlayName());
			}
		}
		//计算核心
		List<DLZQOrderLotteryBetInfoDTO> orderLotteryBetInfos = new ArrayList<DLZQOrderLotteryBetInfoDTO>();
		for(String betType: indexMap.keySet()) {
			char[] charArray = betType.toCharArray();
			int num = Integer.valueOf(String.valueOf(charArray[0]));
			List<String> betIndexList = indexMap.get(betType);
			List<List<MatchBetPlayCellDTO>> result = new ArrayList<List<MatchBetPlayCellDTO>>(betIndexList.size());
			for(String str: betIndexList) {
				String[] strArr = str.split(",");
				List<String> playCodes = new ArrayList<String>(strArr.length);
				Arrays.asList(strArr).stream().forEach(item->{
					MatchBetPlayDTO betPlayDto = matchBellCellList.get(Integer.valueOf(item));
					String playCode = betPlayDto.getPlayCode();
					playCodes.add(playCode);
				});
				List<MatchBetPlayCellDTO> dtos = new ArrayList<MatchBetPlayCellDTO>(0);
				matchBetPlayCellsForLottery(playCodes.size(), playCellMap, playCodes, dtos, result);
			}
			for(List<MatchBetPlayCellDTO> subList: result) {
				DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
				dto.setBetType(betType);
				dto.setTimes(param.getTimes());
				dto.setBetContent("");
				dto.setBetStakes("");
				dto.setAmount(2.0*param.getTimes());
				List<DLBetMatchCellDTO> betCellList1 = new ArrayList<DLBetMatchCellDTO>();
				this.betNum(dto, num, subList, betCellList1, playTypeNameMap);
				String stakes = subList.stream().map(cdto->{
					String playCode = cdto.getPlayCode();
					String playType = cdto.getPlayType();
					String cellCodes = cdto.getBetCells().stream().map(cell->{
						return cell.getCellCode();
					}).collect(Collectors.joining(","));
					return playType + "|" + playCode + "|" + cellCodes;
				}).collect(Collectors.joining(";"));
				orderLotteryBetInfos.add(new DLZQOrderLotteryBetInfoDTO(stakes, betCellList1));
			}
		}
		
		//页面返回信息对象
		DLZQBetInfoDTO betInfoDTO = new DLZQBetInfoDTO();
		betInfoDTO.setTimes(param.getTimes());
		betInfoDTO.setBetType(param.getBetType());
		betInfoDTO.setPlayType(param.getPlayType());
		betInfoDTO.setBetCells(orderLotteryBetInfos);//投注方案
		long end4 = System.currentTimeMillis();
		logger.info("4计算投注统计信息用时：" + (end4-end2)+ " - "+start);
		logger.info("5计算投注信息用时：" + (end4-start)+ " - "+start);
		return betInfoDTO;
	}
	/**
	 * 获取预出票信息
	 * @param param
	 * @return
	 */
	public List<LotteryPrintDTO> getPrintLotteryList(DlJcZqMatchBetParam param) {
		long start = System.currentTimeMillis();
		List<MatchBetPlayDTO> matchBellCellList = param.getMatchBetPlays();
		String betTypes = param.getBetType();
		Map<String, List<String>> indexMap = this.getBetIndexList(matchBellCellList, betTypes);
		long end1 = System.currentTimeMillis();
		logger.info("1计算预出票投注排列用时：" + (end1-start)+ " - "+start);
		Map<String, List<MatchBetPlayCellDTO>> playCellMap = this.getMatchBetPlayMap(matchBellCellList);
		long end2 = System.currentTimeMillis();
		logger.info("2计算预出票获取不同投注的赛事信息用时：" + (end2-end1)+ " - "+start);
		//计算核心
		List<LotteryPrintDTO> lotteryPrints = new ArrayList<LotteryPrintDTO>();
		double srcMoney = 2.0*param.getTimes();
		BetResultInfo betResult = new BetResultInfo();
		for(String betType: indexMap.keySet()) {
			char[] charArray = betType.toCharArray();
			int num = Integer.valueOf(String.valueOf(charArray[0]));
			List<String> betIndexList = indexMap.get(betType);
			List<List<MatchBetPlayCellDTO>> result = new ArrayList<List<MatchBetPlayCellDTO>>(betIndexList.size());
			for(String str: betIndexList) {
				String[] strArr = str.split(",");
				List<String> playCodes = new ArrayList<String>(strArr.length);
				for(String item: strArr) {
					MatchBetPlayDTO betPlayDto = matchBellCellList.get(Integer.valueOf(item));
					String playCode = betPlayDto.getPlayCode();
					playCodes.add(playCode);
				}
				List<MatchBetPlayCellDTO> dtos = new ArrayList<MatchBetPlayCellDTO>(0);
				this.matchBetPlayCellsForLottery(playCodes.size(), playCellMap, playCodes, dtos, result);
			}
			//出票信息
			for(List<MatchBetPlayCellDTO> subList: result) {
				Integer oldBetNum = betResult.getBetNum();
				this.betNumtemp(srcMoney, num, subList, subList.size(), betResult);
				String stakes = subList.stream().map(cdto->{
					String playCode = cdto.getPlayCode();
					String playType1 = cdto.getPlayType();
					String cellCodes = cdto.getBetCells().stream().map(cell->{
						return cell.getCellCode();
					}).collect(Collectors.joining(","));
					return playType1 + "|" + playCode + "|" + cellCodes;
				}).collect(Collectors.joining(";"));
				Set<Integer> collect = subList.stream().map(cdto->Integer.parseInt(cdto.getPlayType())).collect(Collectors.toSet());
				String playType = param.getPlayType();
				if(Integer.parseInt(playType) == 6 && collect.size() == 1) {
					playType = "0"+collect.toArray(new Integer[1])[0].toString();
				}
				String issue = subList.get(0).getPlayCode();
				if(subList.size() > 1) {
					issue = subList.stream().max((item1,item2)->item1.getPlayCode().compareTo(item2.getPlayCode())).get().getPlayCode();
				}
				int betNum = betResult.getBetNum() - oldBetNum;
				int maxTime = 10000/betNum > 99? 99:10000/betNum;
				int times = param.getTimes();
				int n = times/maxTime;
				int m = times%maxTime;
				if(n > 0) {
					for(int i=0; i< n; i++) {
						Double money = betNum*maxTime*2.0;
						LotteryPrintDTO lotteryPrintDTO = new LotteryPrintDTO();
						lotteryPrintDTO.setBetType(betType);
						lotteryPrintDTO.setIssue(issue);
						lotteryPrintDTO.setMoney(money);
						lotteryPrintDTO.setPlayType(playType);
						lotteryPrintDTO.setStakes(stakes);
						String ticketId = SNGenerator.nextSN(SNBusinessCodeEnum.TICKET_SN.getCode());
						lotteryPrintDTO.setTicketId(ticketId);
						lotteryPrintDTO.setTimes(maxTime);
						lotteryPrints.add(lotteryPrintDTO);
					}
				}
				if(m > 0) {
					Double money = betNum*m*2.0;
//					String playType = param.getPlayType();
					LotteryPrintDTO lotteryPrintDTO = new LotteryPrintDTO();
					lotteryPrintDTO.setBetType(betType);
					lotteryPrintDTO.setIssue(issue);
					lotteryPrintDTO.setMoney(money);
					lotteryPrintDTO.setPlayType(playType);
					lotteryPrintDTO.setStakes(stakes);
					String ticketId = SNGenerator.nextSN(SNBusinessCodeEnum.TICKET_SN.getCode());
					lotteryPrintDTO.setTicketId(ticketId);
					lotteryPrintDTO.setTimes(m);
					lotteryPrints.add(lotteryPrintDTO);
				}
			}
		}
		long end3 = System.currentTimeMillis();
		logger.info("3计算预出票基础信息用时：" + (end3-end2)+ " - "+start);
		logger.info("5计算预出票信息用时：" + (end3-start)+ " - "+start);
		return lotteryPrints;
	}
	/**
	 * 计算投注组合
	 * @param matchBellCellList
	 * @param betTypes
	 * @return
	 */
	private Map<String, List<String>> getBetIndexList(List<MatchBetPlayDTO> matchBellCellList, String betTypes) {
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
	 * 出票综合信息
	 * @param param
	 * @return
	 */
	public DLZQBetInfoDTO getBetInfo1(DlJcZqMatchBetParam param) {
		long start = System.currentTimeMillis();
		List<MatchBetPlayDTO> matchBellCellList = param.getMatchBetPlays();
		String betTypes = param.getBetType();
		Map<String, List<String>> indexMap = this.getBetIndexList(matchBellCellList, betTypes);
		long end1 = System.currentTimeMillis();
		logger.info("1计算投注排列用时：" + (end1-start)+ " - "+start);
		Map<String, List<MatchBetPlayCellDTO>> playCellMap = this.getMatchBetPlayMap(matchBellCellList);
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
					MatchBetPlayDTO betPlayDto = matchBellCellList.get(Integer.valueOf(item));
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
		DLZQBetInfoDTO betInfoDTO = new DLZQBetInfoDTO();
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
	private Map<String, List<MatchBetPlayCellDTO>> getMatchBetPlayMap(List<MatchBetPlayDTO> matchBellCellList) {
		//整理投注对象
//		TMatchBetInfoWithMinOddsList tbml = new TMatchBetInfoWithMinOddsList();
//		List<Double> minList = new ArrayList<Double>(matchBellCellList.size());
		Map<String, List<MatchBetPlayCellDTO>> playCellMap = new HashMap<String, List<MatchBetPlayCellDTO>>(matchBellCellList.size());
//		Map<String, Double> minCellOddsMap = new HashMap<String, Double>(matchBellCellList.size());
//		Map<String, Integer> cellNumsMap = new HashMap<String, Integer>(matchBellCellList.size());
		matchBellCellList.forEach(betPlayDto->{
			String playCode = betPlayDto.getPlayCode();
			List<MatchBetCellDTO> matchBetCells = betPlayDto.getMatchBetCells();
			List<MatchBetPlayCellDTO> list = playCellMap.get(playCode);
//			Double minCellOdds = minCellOddsMap.get(playCode);
//			Integer cellNums = cellNumsMap.get(playCode);
			if(list == null) {
				list = new ArrayList<MatchBetPlayCellDTO>(matchBetCells.size());
				playCellMap.put(playCode, list);
//				minCellOdds = Double.MAX_VALUE;
//				cellNums = 0;
			}
			
			for(MatchBetCellDTO cell: matchBetCells) {
				MatchBetPlayCellDTO playCellDto = new MatchBetPlayCellDTO(betPlayDto);
				playCellDto.setPlayType(cell.getPlayType());
				List<DlJcZqMatchCellDTO> betCells = cell.getBetCells();
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
	private TMatchBetMaxAndMinOddsList maxMoneyBetPlayCellsForLottery(Map<String, List<MatchBetPlayCellDTO>> playCellMap) {
		TMatchBetMaxAndMinOddsList tem = new TMatchBetMaxAndMinOddsList();
		List<Double> maxOdds = new ArrayList<Double>(playCellMap.size());
		List<Double> minOdds = new ArrayList<Double>(playCellMap.size());
		for(String playCode: playCellMap.keySet()) {
			List<MatchBetPlayCellDTO> list = playCellMap.get(playCode);
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
	 * 计算混合玩法的排斥后该场次的几种可能 赔率（没有比分，）
	 * @param list
	 */
	private void maxMoneyBetPlayByTTG(List<MatchBetPlayCellDTO> list) {
		
	}
	/**
	 * 计算混合玩法的排斥后的该场次的几种可能赔率
	 * @param list 混合玩法 同一场次的所有玩法选项
	 */
	private List<Double> allbetComOdds(List<MatchBetPlayCellDTO> list) {
		//比分
		Optional<MatchBetPlayCellDTO> optionalcrs = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_CRS.getcode())).findFirst();
		MatchBetPlayCellDTO crsBetPlay = optionalcrs.isPresent()?optionalcrs.get():null;
		//总进球
		Optional<MatchBetPlayCellDTO> optionalttg = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_TTG.getcode())).findFirst();
		MatchBetPlayCellDTO ttgBetPlay = optionalttg.isPresent()?optionalttg.get():null;
		//让球胜平负
		Optional<MatchBetPlayCellDTO> optional2 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode())).findFirst();
		MatchBetPlayCellDTO hhadBetPlay = optional2.isPresent()?optional2.get():null;
		//胜平负
		Optional<MatchBetPlayCellDTO> optional3 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode())).findFirst();
		MatchBetPlayCellDTO hadBetPlay = optional3.isPresent()?optional3.get():null;
//		logger.info(JSONHelper.bean2json(hadBetPlay));
		//半全场
		Optional<MatchBetPlayCellDTO> optional4 = list.stream().filter(dto->Integer.parseInt(dto.getPlayType()) == (MatchPlayTypeEnum.PLAY_TYPE_HAFU.getcode())).findFirst();
		MatchBetPlayCellDTO hafuBetPlay = optional4.isPresent()?optional4.get():null;
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
	private List<Double> cc2(MatchBetPlayCellDTO hhadBetPlay, MatchBetPlayCellDTO hadBetPlay,
			MatchBetPlayCellDTO hafuBetPlay) {
		List<Double> allBetSumOdds = new ArrayList<Double>(1);
		//胜平负
		List<Double> allOdds = new ArrayList<Double>();
		Double hOdds = null, dOdds = null, aOdds = null;
		if(hadBetPlay != null){
			List<DlJcZqMatchCellDTO> betCells = hadBetPlay.getBetCells();
			for(DlJcZqMatchCellDTO dto: betCells) {
				Integer cellCode = Integer.parseInt(dto.getCellCode());
				Double odds = Double.valueOf(dto.getCellOdds());
				if(MatchResultHadEnum.HAD_H.getCode().equals(cellCode)) {
					hOdds = odds;
				} else if(MatchResultHadEnum.HAD_D.getCode().equals(cellCode)) {
					dOdds = odds;
				} else if(MatchResultHadEnum.HAD_A.getCode().equals(cellCode)) {
					aOdds = odds;
				} 
			}
		}
		//半全场
		List<Double> hList = new ArrayList<Double>(0), dList = new ArrayList<Double>(0), aList=new ArrayList<Double>(0);
		if(hafuBetPlay != null) {
			List<DlJcZqMatchCellDTO> betCells = hafuBetPlay.getBetCells();
			for(DlJcZqMatchCellDTO dto: betCells) {
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
			List<DlJcZqMatchCellDTO> betCells = hhadBetPlay.getBetCells();
			Integer fixNum = Integer.valueOf(hhadBetPlay.getFixedodds());
			List<Double> naList = new ArrayList<Double>(aList.size()*3);
			List<Double> ndList = new ArrayList<Double>(dList.size()*3);
			List<Double> nhList = new ArrayList<Double>(hList.size()*3);
			for(DlJcZqMatchCellDTO dto: betCells) {
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
	private List<Double> cc(MatchBetPlayCellDTO crsBetPlay, MatchBetPlayCellDTO ttgBetPlay,
			MatchBetPlayCellDTO hhadBetPlay, MatchBetPlayCellDTO hadBetPlay, MatchBetPlayCellDTO hafuBetPlay) {
		//比分的所有项
		List<DlJcZqMatchCellDTO> betCells = crsBetPlay.getBetCells();//比分的所有选项
		List<Double> allBetSumOdds = new ArrayList<Double>();
		for(DlJcZqMatchCellDTO dto: betCells) {
			String cellCode = dto.getCellCode();
			String[] arr = cellCode.split("");
			int m = Integer.parseInt(arr[0]);
			int n = Integer.parseInt(arr[1]);
			int sum = m+n;//总进球数
			int sub = m-n;//进球差数
			List<Double> allOdds = new ArrayList<Double>();
			String cellOdds = dto.getCellOdds();
			if(StringUtils.isNotBlank(cellOdds)) {
				allOdds.add(Double.valueOf(cellOdds));
			}
			//1.总进球
			if(ttgBetPlay != null) {
				List<DlJcZqMatchCellDTO> betCells2 = ttgBetPlay.getBetCells();
				int sucCode = sum > 7?7:sum;
				Optional<DlJcZqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
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
				List<DlJcZqMatchCellDTO> betCells2 = hhadBetPlay.getBetCells();
				int sucCode = sub + Integer.valueOf(hhadBetPlay.getFixedodds());
				if(sucCode > 0) {
					sucCode = MatchResultHadEnum.HAD_H.getCode();
				}else if(sucCode < 0) {
					sucCode = MatchResultHadEnum.HAD_A.getCode();
				}else {
					sucCode = MatchResultHadEnum.HAD_D.getCode();
				}
				final int sucCode1 = sucCode;
				Optional<DlJcZqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode1).findFirst();
				if(optional.isPresent()) {
					Double odds = Double.valueOf(optional.get().getCellOdds());//选中的让球胜平负玩法的可用赔率
					Double old = allOdds.remove(0);
					allOdds.add(Double.sum(old, odds));
				}
			}
			//3.胜平负
			boolean isH=false,isA=false;
			if(hadBetPlay != null) {
				List<DlJcZqMatchCellDTO> betCells2 = hadBetPlay.getBetCells();
				if(sum == 0) {//平
					int sucCode = MatchResultHadEnum.HAD_D.getCode();
					Optional<DlJcZqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
					if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
						Double odds = Double.valueOf(optional.get().getCellOdds());
						Double old = allOdds.remove(0);
						allOdds.add(Double.sum(old, odds));
					}
				}else if(sum == 1) {//胜，负
					if(n ==0) {
						int sucCode = MatchResultHadEnum.HAD_H.getCode();
						Optional<DlJcZqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
							isH=true;
						}
					}else {
						int sucCode = MatchResultHadEnum.HAD_A.getCode();
						Optional<DlJcZqMatchCellDTO> optional1 = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
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
						Optional<DlJcZqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
						}
					} else if(sub < 0) {//负
						int sucCode = MatchResultHadEnum.HAD_A.getCode();
						Optional<DlJcZqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
						if(optional.isPresent()) {//选中的胜平负玩法的可用赔率
							Double odds = Double.valueOf(optional.get().getCellOdds());
							Double old = allOdds.remove(0);
							allOdds.add(Double.sum(old, odds));
						}
					}else {//平
						int sucCode = MatchResultHadEnum.HAD_D.getCode();
						Optional<DlJcZqMatchCellDTO> optional = betCells2.stream().filter(betCell->Integer.parseInt(betCell.getCellCode()) == sucCode).findFirst();
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
				List<DlJcZqMatchCellDTO> betCells2 = hafuBetPlay.getBetCells();
				if(sum == 0) {
					Optional<DlJcZqMatchCellDTO> optional = betCells2.stream().filter(betCell->MatchResultHafuEnum.HAFU_DD.getCode().equals(betCell.getCellCode())).findFirst();
					if(optional.isPresent()) {
						Double odds = Double.valueOf(optional.get().getCellOdds());
						Double old = allOdds.remove(0);
						allOdds.add(Double.sum(old, odds));
					}
				}else if(sum  == 1) {
					Double old = allOdds.remove(0);
					if(isH) {
						for(DlJcZqMatchCellDTO betCell: betCells2) {
							String betCellCode = betCell.getCellCode();
							if(betCellCode.equals(MatchResultHafuEnum.HAFU_DH.getCode()) || betCellCode.equals(MatchResultHafuEnum.HAFU_HH.getCode())) {
								Double odds = Double.valueOf(betCell.getCellOdds());
								allOdds.add(Double.sum(old, odds));
							}
						}
					}
					if(isA) {
						for(DlJcZqMatchCellDTO betCell: betCells2) {
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
						for(DlJcZqMatchCellDTO betCell: betCells2) {
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
						for(DlJcZqMatchCellDTO betCell: betCells2) {
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
						for(DlJcZqMatchCellDTO betCell: betCells2) {
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
	private MatchBetPlayCellDTO bb(MatchBetPlayCellDTO ttgBetPlay) {
		MatchBetPlayCellDTO crsBetPlay;
		List<DlJcZqMatchCellDTO> ttgBetCells = ttgBetPlay.getBetCells();
		List<DlJcZqMatchCellDTO> ncrsBetCells = new ArrayList<DlJcZqMatchCellDTO>();
		crsBetPlay = new MatchBetPlayCellDTO();
		crsBetPlay.setBetCells(ncrsBetCells);
		for(DlJcZqMatchCellDTO matchCellDto: ttgBetCells) {
			Integer qiuNum = Integer.parseInt(matchCellDto.getCellCode());
			if(qiuNum == 0) {
				DlJcZqMatchCellDTO nmatchCellDto = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_00.getCode(), MatchResultCrsEnum.CRS_00.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
			} else if(qiuNum == 1) {
				DlJcZqMatchCellDTO nmatchCellDto = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_10.getCode(), MatchResultCrsEnum.CRS_10.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto1 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_01.getCode(), MatchResultCrsEnum.CRS_01.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
			}else if(qiuNum == 2) {
				DlJcZqMatchCellDTO nmatchCellDto = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_11.getCode(), MatchResultCrsEnum.CRS_11.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto1 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_02.getCode(), MatchResultCrsEnum.CRS_02.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto2 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_20.getCode(), MatchResultCrsEnum.CRS_20.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
				ncrsBetCells.add(nmatchCellDto2);
			}else if(qiuNum == 3) {
				DlJcZqMatchCellDTO nmatchCellDto = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_30.getCode(), MatchResultCrsEnum.CRS_30.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto1 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_03.getCode(), MatchResultCrsEnum.CRS_03.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto2 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_21.getCode(), MatchResultCrsEnum.CRS_21.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto3 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_12.getCode(), MatchResultCrsEnum.CRS_12.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
				ncrsBetCells.add(nmatchCellDto2);
				ncrsBetCells.add(nmatchCellDto3);
			}else if(qiuNum == 4) {
				DlJcZqMatchCellDTO nmatchCellDto = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_40.getCode(), MatchResultCrsEnum.CRS_40.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto1 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_04.getCode(), MatchResultCrsEnum.CRS_04.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto2 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_31.getCode(), MatchResultCrsEnum.CRS_31.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto3 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_13.getCode(), MatchResultCrsEnum.CRS_13.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto4 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_22.getCode(), MatchResultCrsEnum.CRS_22.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
				ncrsBetCells.add(nmatchCellDto2);
				ncrsBetCells.add(nmatchCellDto3);
				ncrsBetCells.add(nmatchCellDto4);
			}else if(qiuNum == 5) {
				DlJcZqMatchCellDTO nmatchCellDto = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_50.getCode(), MatchResultCrsEnum.CRS_50.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto1 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_05.getCode(), MatchResultCrsEnum.CRS_05.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto2 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_41.getCode(), MatchResultCrsEnum.CRS_41.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto3 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_14.getCode(), MatchResultCrsEnum.CRS_14.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto4 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_32.getCode(), MatchResultCrsEnum.CRS_32.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto5 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_23.getCode(), MatchResultCrsEnum.CRS_23.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
				ncrsBetCells.add(nmatchCellDto2);
				ncrsBetCells.add(nmatchCellDto3);
				ncrsBetCells.add(nmatchCellDto4);
				ncrsBetCells.add(nmatchCellDto5);
			}else if(qiuNum == 6) {
				DlJcZqMatchCellDTO nmatchCellDto = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_15.getCode(), MatchResultCrsEnum.CRS_15.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto1 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_51.getCode(), MatchResultCrsEnum.CRS_51.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto2 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_24.getCode(), MatchResultCrsEnum.CRS_24.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto3 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_42.getCode(), MatchResultCrsEnum.CRS_42.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto4 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_33.getCode(), MatchResultCrsEnum.CRS_33.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
				ncrsBetCells.add(nmatchCellDto2);
				ncrsBetCells.add(nmatchCellDto3);
				ncrsBetCells.add(nmatchCellDto4);
			}else if(qiuNum == 7) {
				DlJcZqMatchCellDTO nmatchCellDto = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_52.getCode(), MatchResultCrsEnum.CRS_52.getMsg(), null);
				DlJcZqMatchCellDTO nmatchCellDto1 = new DlJcZqMatchCellDTO(MatchResultCrsEnum.CRS_25.getCode(), MatchResultCrsEnum.CRS_25.getMsg(), null);
				ncrsBetCells.add(nmatchCellDto);
				ncrsBetCells.add(nmatchCellDto1);
			}
		}
		return crsBetPlay;
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
		logger.info("准备拉取开赛结果 ： size=" + matchList.size());
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
		logger.info("minMatchTimeStr = "+minMatchTimeStr);
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
        	logger.error(e.getMessage());
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
						/*logger.info("保存比赛结果详情"+changciId);
						matchResultService.refreshMatchResultFromZC(Integer.valueOf(changciId));*/
						/*logger.info("更新订单详情的赛事结果"+issue);
						LotteryPrintRewardParam lotteryPrintRewardParam = new LotteryPrintRewardParam();
						lotteryPrintRewardParam.setIssue(issue);
						int rstCode = orderService.updateOrderInfoByMatchResult(lotteryPrintRewardParam).getCode();
						if(rstCode != 0) {
							continue;
						}*/
						/*logger.info("开奖场次："+issue);
						DlToAwardingParam dltoAwardingParm = new DlToAwardingParam();
						dltoAwardingParm.setIssue(issue);
						lotteryRewardService.toAwarding(dltoAwardingParm);*/
						logger.info("保存比赛比分结果"+changciId);
						lotteryMatchMapper.updateMatchResult(lotteryMatch);
					}
				}else if(MATCH_RESULT_CANCEL.equals(status)) {
					String matchDate = tds.get(0).text();
					String changci = tds.get(1).text();
					String[] arr = this.matchId(matchs, matchDate, changci);
					if(null != arr) {
						String matchId = arr[2];
						String changciId = arr[3];
						String issue = arr[4];
						LotteryMatch lotteryMatch = new  LotteryMatch();
						lotteryMatch.setMatchId(Integer.valueOf(matchId));
						lotteryMatch.setFirstHalf("-");
						lotteryMatch.setWhole("-");
						lotteryMatch.setStatus(ProjectConstant.MATCH_CANCEL);
						matchResult.add(lotteryMatch);
						changciIds.add(changciId);
						issueList.add(issue);
						logger.info("保存比赛比分结果"+changciId);
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
	 * 构造比赛筛选日期20天
	 * @return
	 */
	public List<MatchDateDTO> giveMatchChooseDate(){
		List<MatchDateDTO> returnList = new ArrayList<>();
		Date date = new Date();
		List<String> before16days = DateUtil.getBeforeDatePeriod(date,16);
		String today = DateUtil.getCurrentDateTime(LocalDateTime.now(), DateUtil.date_sdf);
		List<String> after3days = DateUtil.getAfterDatePeriod(new Date(),3);
		
		for(String bs:before16days) {
			MatchDateDTO dto = new MatchDateDTO();
			dto.setIsSelected("0");
			dto.setStrDate(bs);
			returnList.add(dto);
		}
		
		MatchDateDTO todayDto = new MatchDateDTO();
		todayDto.setIsSelected("1");
		todayDto.setStrDate(today);
		returnList.add(todayDto);
		
		for(String as:after3days) {
			MatchDateDTO dto = new MatchDateDTO();
			dto.setIsSelected("0");
			dto.setStrDate(as);
			returnList.add(dto);
		}
		
		return returnList;
	}
	
	/**
	 * 根据查询条件查看比赛结果  2018-07-06 新加
	 * @param dateStr
	 * @return
	 */
	public BaseResult<QueryMatchResultDTO> queryMatchResultNew(QueryMatchParamByType queryMatchParamByType) {
		log.info("查看比赛结果 参数:" + JSON.toJSONString(queryMatchParamByType));
		QueryMatchResultDTO returnDTO = new QueryMatchResultDTO();
		List<LotteryMatchDTO> lotteryMatchDTOList = new ArrayList<LotteryMatchDTO>();
		Integer[] matchIdArr = new Integer[] {};
		LinkedList<LotteryMatch> lotteryMatchList = new LinkedList<>();
		Integer matchSize = 0;
		List<Integer> matchIdList = new ArrayList<>();
		Integer userId = SessionUtil.getUserId();
		String[] leagueIdArr = new String[] {};
		String dateStr = queryMatchParamByType.getDateStr();
		if (!StringUtils.isEmpty(queryMatchParamByType.getLeagueIds())) {
			leagueIdArr = queryMatchParamByType.getLeagueIds().split(",");
		}
		if(StringUtils.isEmpty(dateStr)) {
			dateStr = DateUtil.getCurrentDateTime(LocalDateTime.now(), DateUtil.date_sdf);
		}
		
		if (null != userId) {//登录状态下查询收藏的赛事
			com.dl.member.param.DateStrParam dateStrParam = new com.dl.member.param.DateStrParam();
			dateStrParam.setDateStr(dateStr);
			BaseResult<List<Integer>> matchIdsRst = iUserCollectService.matchIdlist(dateStrParam);
			if (matchIdsRst.getCode() == 0) {
				matchIdList = matchIdsRst.getData();
				if (matchIdList.size() > 0) {
					matchIdArr = matchIdList.stream().toArray(Integer[]::new);
				}
			}else {
				log.error("赛事比分查询收藏结果异常:"+matchIdsRst.getMsg());
			}
		}
		
		if ("2".equals(queryMatchParamByType.getType())) {//我的赛事收藏
			if (null == userId) {
				return ResultGenerator.genNeedLoginResult("请登录");
			}
			if(matchIdArr.length > 0) {
				lotteryMatchList = lotteryMatchMapper.queryMatchByQueryConditionNew(null,matchIdArr, leagueIdArr, "");
				matchSize = lotteryMatchList.size();
			}
		} else if("0".equals(queryMatchParamByType.getType())) {//未结束
			lotteryMatchList = lotteryMatchMapper.queryMatchByQueryConditionNew(dateStr,null, leagueIdArr,null);
			matchSize = lotteryMatchList.size();
			//查询的是当天的，把当天比赛放入其中
			if(DateUtil.isToday(dateStr, "yyyy-MM-dd")) {
				List<LotteryMatch> lotteryMatchNowList = lotteryMatchMapper.queryMatchByQueryCondition(dateStr, null, leagueIdArr, null);
				for(LotteryMatch l:lotteryMatchNowList) {
					lotteryMatchList.addFirst(l);
				}
			}		
		} else if("1".equals(queryMatchParamByType.getType())) {//已结束
			lotteryMatchList = lotteryMatchMapper.queryMatchByQueryConditionNew(dateStr,null, leagueIdArr,null);
			matchSize = lotteryMatchList.size();
		}
		
		if (CollectionUtils.isEmpty(lotteryMatchList)) {
			return ResultGenerator.genSuccessResult("success", returnDTO);
		}
		// 查询球队logo
		List<Integer> homeTeamIdList = lotteryMatchList.stream().map(s -> s.getHomeTeamId()).collect(Collectors.toList());
		List<Integer> visitingTeamIdList = lotteryMatchList.stream().map(s -> s.getVisitingTeamId()).collect(Collectors.toList());
		homeTeamIdList.addAll(visitingTeamIdList);
		List<DlLeagueTeam> leagueList = dlLeagueTeamMapper.queryLeagueTeamByTeamIds(homeTeamIdList);
		for (LotteryMatch s : lotteryMatchList) {
			LotteryMatchDTO lotteryMatchDTO = new LotteryMatchDTO();
			BeanUtils.copyProperties(s, lotteryMatchDTO);
			for (DlLeagueTeam ss : leagueList) {
				if(s.getHomeTeamId().equals(ss.getSportteryTeamid())) {
					lotteryMatchDTO.setHomeTeamLogo(ss.getTeamPic());
				}
				if(s.getVisitingTeamId().equals(ss.getSportteryTeamid())) {
					lotteryMatchDTO.setVisitingTeamLogo(ss.getTeamPic());
				}
				continue;
			}
			lotteryMatchDTO.setMatchTime(DateUtil.getYMD(s.getMatchTime()));
			Long matchTime = s.getMatchTime().getTime()/1000;
			lotteryMatchDTO.setMatchTimeStart(String.valueOf((matchTime)));
			lotteryMatchDTO.setChangci(s.getChangci());
			lotteryMatchDTO.setChangciId(s.getChangciId());
			if (null != userId) {
				if (matchIdList.contains(s.getMatchId())) {
					lotteryMatchDTO.setIsCollect("1");
				} else {
					lotteryMatchDTO.setIsCollect("0");
				}
			} else {
				lotteryMatchDTO.setIsCollect("0");
			}
			
			//用直播表查询这4个实时数据
			MatchMinuteAndScoreDTO dto = dlMatchLiveService.getMatchInfoNow(s.getChangciId());
			if(StringUtils.isNotEmpty(dto.getMatchStatus())) {
				lotteryMatchDTO.setFirstHalf(dto.getFirstHalf());
				lotteryMatchDTO.setWhole(dto.getWhole());
				lotteryMatchDTO.setMinute(dto.getMinute());
				//冗余异常数据：比赛进行了150min还未结束,状态按结束算
				if(MatchStatusEnums.Playing.getEnName().equals(dto.getMatchStatus()) && this.beyond150min(s.getMatchTime())) {
					lotteryMatchDTO.setMatchFinish(MatchStatusEnums.Played.getCode());
				}else {
					lotteryMatchDTO.setMatchFinish(MatchStatusEnums.getCodeByEnName(dto.getMatchStatus()));
				}
			}else {
				lotteryMatchDTO.setFirstHalf("0:0");
				lotteryMatchDTO.setWhole("0:0");
				lotteryMatchDTO.setMinute("0");
				lotteryMatchDTO.setMatchFinish(String.valueOf(s.getStatus()));
			}
			lotteryMatchDTOList.add(lotteryMatchDTO);
		}
		
		if(queryMatchParamByType.getType().equals("0")) {//未结束
			lotteryMatchDTOList.removeIf(s->MatchStatusEnums.Played.getCode().equals(s.getMatchFinish()));
		    List<LotteryMatchDTO> unique = lotteryMatchDTOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(LotteryMatchDTO::getChangciId))), ArrayList::new));
			lotteryMatchDTOList = unique;
			//优先展示正在进行的比赛
			unique.stream().sorted(Comparator.comparing(LotteryMatchDTO::getMatchFinish).reversed().thenComparing(LotteryMatchDTO::getChangciId));
			String weekDay = DateUtil.getWeekByDateStr(dateStr);
			matchSize = unique.stream().filter(s->s.getChangci().indexOf(weekDay) > -1).collect(Collectors.toList()).size();
		}else if(queryMatchParamByType.getType().equals("1")) {//已结束
			lotteryMatchDTOList.removeIf(s->!MatchStatusEnums.Played.getCode().equals(s.getMatchFinish()));
			matchSize = lotteryMatchDTOList.size();
		}
		
		returnDTO.setMatchDateStr(this.createMatchDateStr(dateStr, matchSize));
		returnDTO.setLotteryMatchDTOList(lotteryMatchDTOList);
		return ResultGenerator.genSuccessResult("success", returnDTO);
	}
	
	/**
	 * 显示比分日期字符串
	 * @param dateStr
	 * @param matchSize
	 * @return
	 */
	public String createMatchDateStr(String dateStr,Integer matchSize) {
		return  "<html><body><font  color='#9F9F9F'>"+dateStr + " " +DateUtil.getWeekByDateStr(dateStr) +"共有</font><font  color='#EA5504'>"+matchSize+"</font><font  color='#9F9F9F'>场比赛</font></body></html>";
	}
	
	/**
	 * 判断比赛是否超过了150min
	 * @param matchTime
	 * @return
	 */
	public Boolean beyond150min(Date matchTime) {
		 Integer beyondTime = DateUtil.getCurrentTimeLong() - DateUtil.getTimeSomeDate(matchTime);
		 if(beyondTime > 9000) {
			 return true;
		 }
		 return false;
	} 
	
	/**
	 * 查询竞彩结果
	 * @param dateStr
	 * @return
	 */
	public List<LeagueMatchResultDTO> queryJLJcOpenPrizesByDate(JCQueryParam jcParam) {
		List<LeagueMatchResultDTO> lmrList = new ArrayList<>();
		String dateStr = jcParam.getDateStr();
		if(StringUtils.isEmpty(dateStr)) {
			dateStr = DateUtil.getCurrentDateTime(LocalDateTime.now(), DateUtil.date_sdf);
		}
		List<DlMatchBasketball> lotteryMatchList = dlMatchBasketballMapper.queryMatchByQueryCondition(dateStr,null,null,MatchStatusEnums.Played.getCode());
		if(lotteryMatchList.size() == 0) {
			return lmrList;
		}
		List<Integer> changciIdList = lotteryMatchList.stream().map(s->s.getChangciId()).collect(Collectors.toList());
		List<DlResultBasketball> resultList = dlMatchBasketballService.queryBasketBallResult(changciIdList);
		Map<Integer, List<DlResultBasketball>> leagueMatchMap = resultList.stream().collect(Collectors.groupingBy(DlResultBasketball::getChangciId));

//		PLAY_TYPE_MNL(1,"MNL"), //胜负
//		PLAY_TYPE_HDC(2,"HDC"), // 让分胜负
//		PLAY_TYPE_WNM(3,"WNM"), //胜分差
//		PLAY_TYPE_HILO(4,"HILO"); //大小分
		
		lotteryMatchList.stream().forEach(s->{
			LeagueMatchResultDTO lmrDto = new LeagueMatchResultDTO();
			Integer changciId = s.getChangciId();		
			List<DlResultBasketball> list = leagueMatchMap.get(changciId);
			if(null != list) {
				lmrDto.setChangciId(String.valueOf(changciId));
				lmrDto.setCupName(s.getLeagueAbbr());
				lmrDto.setChangci(s.getChangci());
				lmrDto.setHomeTeamAbbr(s.getHomeTeamAbbr());
				lmrDto.setVisitTeamAbbr(s.getVisitingTeamAbbr());
				String matchTime = DateUtil.getCurrentTimeString(Long.valueOf(DateUtil.getTimeSomeDate(s.getMatchTime())),DateUtil.hh_mm_sdf);
				lmrDto.setMatchTime(matchTime);
				lmrDto.setWhole(s.getWhole());
				DlResultBasketball resultBasketBall = list.get(0);
				String dataJson = resultBasketBall.getDataJson();
					
				JSONObject dataObj = JSON.parseObject(dataJson);
				lmrDto.setMnl(dataObj.getString("mnl_result"));
				lmrDto.setWnm(dataObj.getString("wnm_result"));
				lmrDto.setHdc(StringUtils.isEmpty(dataObj.getString("hdc_let"))?dataObj.getString("hdc_result"):"("+dataObj.getString("hdc_let")+")"+dataObj.getString("hdc_result"));
				lmrDto.setHilo(dataObj.getString("hilo_result")+"于"+dataObj.getString("hilo_score"));
				lmrList.add(lmrDto);
			}
		});
		return lmrList;
	}
	
	/**
	 * 查询竞彩结果
	 * @param dateStr
	 * @return
	 */
	public List<LeagueMatchResultDTO> queryJcOpenPrizesByDate(JCQueryParam jcParam) {
		List<LeagueMatchResultDTO> lmrList = new ArrayList<>();
		String dateStr = jcParam.getDateStr();
		if(StringUtils.isEmpty(dateStr)) {
			dateStr = DateUtil.getCurrentDateTime(LocalDateTime.now(), DateUtil.date_sdf);
		}
		List<LotteryMatch> lotteryMatchList = lotteryMatchMapper.queryMatchByQueryCondition(dateStr,null,null,MatchStatusEnums.Played.getCode());
		if(lotteryMatchList.size() == 0) {
			return lmrList;
		}
		List<Integer> changciIdList = lotteryMatchList.stream().map(s->s.getChangciId()).collect(Collectors.toList());
		List<DlLeagueMatchResult> leagueMatchResultList = dlLeagueMatchResultMapper.queryMatchResultsByChangciIds(changciIdList);
		Map<Integer, List<DlLeagueMatchResult>> leagueMatchMap = leagueMatchResultList.stream().collect(Collectors.groupingBy(DlLeagueMatchResult::getChangciId));
		lotteryMatchList.stream().forEach(s->{
			LeagueMatchResultDTO lmrDto = new LeagueMatchResultDTO();
			Integer changciId = s.getChangciId();		
			List<DlLeagueMatchResult> list = leagueMatchMap.get(changciId);
			if(null != list) {
				lmrDto.setChangciId(String.valueOf(changciId));
				lmrDto.setHomeTeamAbbr(s.getHomeTeamAbbr());
				lmrDto.setVisitTeamAbbr(s.getVisitingTeamAbbr());
				lmrDto.setWhole(s.getWhole());
				lmrDto.setHalf(s.getFirstHalf());
				lmrDto.setCupName(s.getLeagueAddr());
				String matchTime = DateUtil.getCurrentTimeString(Long.valueOf(DateUtil.getTimeSomeDate(s.getMatchTime())),DateUtil.hh_mm_sdf);
				lmrDto.setMatchTime(matchTime);
				
				for(DlLeagueMatchResult lmr:list) {
					Integer playType = lmr.getPlayType();
					if(1 == playType) {
						lmrDto.setHhad("("+lmr.getGoalline()+")"+ lmr.getCellName());
					}else if(2 ==playType) {
						lmrDto.setHad(lmr.getCellName());
					}else if(3 ==playType) {
						lmrDto.setCrs(lmr.getCellName());
					}else if(4 ==playType) {
						lmrDto.setTtg(lmr.getCellName());
					}else if(5 ==playType) {
						lmrDto.setHafu(lmr.getCellName());
					}
				}
			}
			lmrList.add(lmrDto);
		});
		return lmrList;
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
		
		log.info("=========================查询的leagueId:"+JSON.toJSONString(leagueIdArr));
		String[] matchIdArr = new String [] {};
		if(queryMatchParam.getIsAlreadyBuyMatch().equals("1")) {
			Integer userId = SessionUtil.getUserId();
			if(null == userId) {
				return ResultGenerator.genNeedLoginResult("请登录");
			}
			//查询 用户当天所下的订单 包含某天的比赛ID集合
			DateStrParam dateStrParam = new DateStrParam();
			dateStrParam.setDateStr(queryMatchParam.getDateStr());
			BaseResult<List<String>> matchIdsRst = orderDetailService.selectMatchIdsInSomeDayOrder(dateStrParam);
			if(matchIdsRst.getCode() != 0) {
				return ResultGenerator.genResult(matchIdsRst.getCode(),matchIdsRst.getMsg());
			}
			List<String> matchIdList = matchIdsRst.getData();
			if(matchIdList.size() == 0) {
				return ResultGenerator.genSuccessResult("success", lotteryMatchDTOList);
			}
			matchIdArr = matchIdList.stream().toArray(String[]::new);
		}

		List<LotteryMatch> lotteryMatchList = lotteryMatchMapper.queryMatchByQueryCondition(queryMatchParam.getDateStr(),matchIdArr,leagueIdArr,queryMatchParam.getMatchFinish());
		if(CollectionUtils.isEmpty(lotteryMatchList)) {
			return ResultGenerator.genSuccessResult("success", lotteryMatchDTOList);
		}
		//查询球队logo
		List<Integer> homeTeamIdList = lotteryMatchList.stream().map(s->s.getHomeTeamId()).collect(Collectors.toList());
		List<Integer> visitingTeamIdList = lotteryMatchList.stream().map(s->s.getVisitingTeamId()).collect(Collectors.toList());
		homeTeamIdList.addAll(visitingTeamIdList);
		List<DlLeagueTeam> leagueList = dlLeagueTeamMapper.queryLeagueTeamByTeamIds(homeTeamIdList);
		
	    for(LotteryMatch s:lotteryMatchList) {
			LotteryMatchDTO  lotteryMatchDTO = new LotteryMatchDTO();
			BeanUtils.copyProperties(s, lotteryMatchDTO);
			for(DlLeagueTeam ss:leagueList) {
				if(s.getHomeTeamId().equals(ss.getSportteryTeamid())) {
					lotteryMatchDTO.setHomeTeamLogo(ss.getTeamPic());
				}
				if(s.getVisitingTeamId().equals(ss.getSportteryTeamid())) {
					lotteryMatchDTO.setVisitingTeamLogo(ss.getTeamPic());
				}
				continue;
			}
			lotteryMatchDTO.setMatchFinish(ProjectConstant.ONE_YES.equals(s.getStatus().toString())?ProjectConstant.ONE_YES:ProjectConstant.ZERO_NO);
			lotteryMatchDTO.setMatchTime(DateUtil.getYMD(s.getMatchTime()));
			lotteryMatchDTO.setChangci(s.getChangci().substring(2));
			lotteryMatchDTOList.add(lotteryMatchDTO);
	    }
	    
	    return ResultGenerator.genSuccessResult("success", lotteryMatchDTOList);
	}	
	
	public List<DLBetLottoInfoDTO> getBetInfoByLottoInfo(String orderSn) {
		List<DLBetLottoInfoDTO> list = new ArrayList<>();
		List<LotteryPrint> prints = lotteryPrintMapper.getByOrderSn(orderSn);
		prints.forEach(item->{
			DLBetLottoInfoDTO dto = new DLBetLottoInfoDTO();
			dto.setTicketId(item.getTicketId());
			dto.setStakes(item.getStakes());
			dto.setBetType(item.getBetType());
			dto.setPlayType(item.getPlayType());
			dto.setStatus(item.getStatus());
			dto.setTimes(item.getTimes());
			dto.setAmount(item.getMoney().doubleValue());
			list.add(dto);
		});
		return list;
	}
	
//	@Transactional(readOnly=true)
	public DLZQBetInfoDTO getBetInfoByOrderInfo1(String orderSn) {
		List<LotteryPlayClassify> allPlays = lotteryPlayClassifyMapper.getAllPlays(1);
		Map<Integer, String> playTypeNameMap = new HashMap<Integer, String>();
		if(!Collections.isEmpty(allPlays)) {
			for(LotteryPlayClassify type: allPlays) {
				playTypeNameMap.put(type.getPlayType(), type.getPlayName());
			}
		}
		List<DLZQOrderLotteryBetInfoDTO> orderLotteryBetInfos = new ArrayList<DLZQOrderLotteryBetInfoDTO>();
		List<LotteryPrint> prints = lotteryPrintMapper.getByOrderSn(orderSn);
		for(LotteryPrint lPrint: prints) {
			String printSp = lPrint.getPrintSp();
			String stakes = lPrint.getStakes();
			String betType = lPrint.getBetType();
			int num = Integer.parseInt(betType)/10;
			String[] stakeList = stakes.split(";");
			Map<String, String> map = this.printspMap(printSp);
			List<MatchBetPlayCellDTO> subList = new ArrayList<MatchBetPlayCellDTO>(stakeList.length);
			for(String stake: stakeList) {
				String[] arr = stake.split("\\|");
				String playType = arr[0];
				String playCode = arr[1];
				String cells = StringUtils.isBlank(map.get(playCode))?arr[2]:map.get(playCode);
				List<DlJcZqMatchCellDTO> betCells = this.betCells(cells.split(","), playType);
				int weekNum = Integer.parseInt(String.valueOf(playCode.charAt(8)));
				String changci = DateUtil.weekDays[weekNum-1] + playCode.substring(9);
				MatchBetPlayCellDTO matchBetPlayCellDto = new MatchBetPlayCellDTO();
				matchBetPlayCellDto.setChangci(changci);
				matchBetPlayCellDto.setPlayType(playType);
//				matchBetPlayCellDto.setFixedodds(fixedodds);
				matchBetPlayCellDto.setBetCells(betCells);
				subList.add(matchBetPlayCellDto);
			}
			List<DLBetMatchCellDTO> betCellList1 = new ArrayList<DLBetMatchCellDTO>();
			DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
			dto.setBetType(betType);
			dto.setTimes(lPrint.getTimes());
			dto.setBetContent("");
			dto.setBetStakes("");
			dto.setAmount(2.0*lPrint.getTimes());
			this.betNum2(dto, num, subList, betCellList1, playTypeNameMap);
			orderLotteryBetInfos.add(new DLZQOrderLotteryBetInfoDTO(stakes, betCellList1, lPrint.getStatus()));
		}
		DLZQBetInfoDTO dto = new DLZQBetInfoDTO();
		dto.setBetCells(orderLotteryBetInfos);
		return dto;
	}
	
	public DLLQBetInfoDTO getBasketBetInfoByOrderInfo1(String orderSn) {
		List<LotteryPlayClassify> allPlays = lotteryPlayClassifyMapper.getAllPlays(3);
		Map<Integer, String> playTypeNameMap = new HashMap<Integer, String>();
		if(!Collections.isEmpty(allPlays)) {
			for(LotteryPlayClassify type: allPlays) {
				playTypeNameMap.put(type.getPlayType(), type.getPlayName());
			}
		}
		List<DLZQOrderLotteryBetInfoDTO> orderLotteryBetInfos = new ArrayList<DLZQOrderLotteryBetInfoDTO>();
		List<LotteryPrint> prints = lotteryPrintMapper.getByOrderSn(orderSn);
		for(LotteryPrint lPrint: prints) {
			String printSp = lPrint.getPrintSp();
			String stakes = lPrint.getStakes();
			String betType = lPrint.getBetType();
			int num = Integer.parseInt(betType)/10;
			String[] stakeList = stakes.split(";");
			Map<String, String> map = this.printspMap(printSp);
			List<MatchBasketBallBetPlayCellDTO> subList = new ArrayList<MatchBasketBallBetPlayCellDTO>(stakeList.length);
			for(String stake: stakeList) {
				String[] arr = stake.split("\\|");
				String playType = arr[0];
				String playCode = arr[1];
				String cells = StringUtils.isBlank(map.get(playCode))?arr[2]:map.get(playCode);
				List<DlJcLqMatchCellDTO> betCells = this.betBasketCells(cells.split(","), playType);
				int weekNum = Integer.parseInt(String.valueOf(playCode.charAt(8)));
				String changci = DateUtil.weekDays[weekNum-1] + playCode.substring(9);
				MatchBasketBallBetPlayCellDTO matchBetPlayCellDto = new MatchBasketBallBetPlayCellDTO();
				matchBetPlayCellDto.setChangci(changci);
				matchBetPlayCellDto.setPlayType(playType);
//				matchBetPlayCellDto.setFixedodds(fixedodds);
				matchBetPlayCellDto.setBetCells(betCells);
				subList.add(matchBetPlayCellDto);
			}
			List<DLBetMatchCellDTO> betCellList1 = new ArrayList<DLBetMatchCellDTO>();
			DLBetMatchCellDTO dto = new DLBetMatchCellDTO();
			dto.setBetType(betType);
			dto.setTimes(lPrint.getTimes());
			dto.setBetContent("");
			dto.setBetStakes("");
			dto.setAmount(2.0*lPrint.getTimes());
			this.betBasketNum2(dto, num, subList, betCellList1, playTypeNameMap);
			orderLotteryBetInfos.add(new DLZQOrderLotteryBetInfoDTO(stakes, betCellList1, lPrint.getStatus()));
		}
		DLLQBetInfoDTO dto = new DLLQBetInfoDTO();
		dto.setBetCells(orderLotteryBetInfos);
		return dto;
	}	
	
	
	private Map<String, String> printspMap(String printSp) {
		Map<String, String> map = new HashMap<String, String>();
		if(StringUtils.isBlank(printSp)) {
			return map;
		}
		String[] split = printSp.split(";");
		for(String str: split) {
			String[] split2 = str.split("\\|");
			map.put(split2[0], split2[1]);
		}
		return map;
	}
	private List<DlJcZqMatchCellDTO> betCells(String[] cells, String playTypeStr) {
		int playType = Integer.parseInt(playTypeStr);
		List<DlJcZqMatchCellDTO> dtos = new ArrayList<DlJcZqMatchCellDTO>(cells.length);
		for(String cell: cells) {
			DlJcZqMatchCellDTO dto = new DlJcZqMatchCellDTO();
			String cellName = cell;
			if(cell.contains("@")) {
				String[] split = cell.split("@");
				cellName = split[0];
				dto.setCellOdds(split[1]);
			}
			dto.setCellCode(cellName);
			if(playType == MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode()) {
				String name = MatchResultHadEnum.getName(Integer.parseInt(cellName));
				dto.setCellName(name);
				dtos.add(dto);
			}else if(playType == MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode()) {
				String name = MatchResultHadEnum.getName(Integer.parseInt(cellName));
				dto.setCellName(name);
				dtos.add(dto);
			}else if(playType == MatchPlayTypeEnum.PLAY_TYPE_CRS.getcode()) {
				String name = MatchResultCrsEnum.getName(cellName);
				dto.setCellName(name);
				dtos.add(dto);
			}else if(playType == MatchPlayTypeEnum.PLAY_TYPE_TTG.getcode()) {
				String name = cellName;
				if(cellName.equals("7")) {
					name = "7+";
				}
				dto.setCellName(name);
				dtos.add(dto);
			}else if(playType == MatchPlayTypeEnum.PLAY_TYPE_HAFU.getcode()) {
				String name = MatchResultHafuEnum.getName(cellName);
				dto.setCellName(name);
				dtos.add(dto);
			}
		}
		return dtos;
	}
	
	private List<DlJcLqMatchCellDTO> betBasketCells(String[] cells, String playTypeStr) {
		int playType = Integer.parseInt(playTypeStr);
		List<DlJcLqMatchCellDTO> dtos = new ArrayList<DlJcLqMatchCellDTO>(cells.length);
		for(String cell: cells) {
			DlJcLqMatchCellDTO dto = new DlJcLqMatchCellDTO();
			String cellName = cell;
			if(cell.contains("@")) {
				String[] split = cell.split("@");
				cellName = split[0];
				dto.setCellOdds(split[1]);
			}
			dto.setCellCode(cellName);
			if(playType == MatchBasketPlayTypeEnum.PLAY_TYPE_HDC.getcode()) {
				String name = MatchBasketBallResultHDCEnum.getName(Integer.parseInt(cellName));
				dto.setCellName(name);
				dtos.add(dto);
			}else if(playType == MatchBasketPlayTypeEnum.PLAY_TYPE_MNL.getcode()) {
				String name = MatchBasketResultHdEnum.getName(Integer.parseInt(cellName));
				dto.setCellName(name);
				dtos.add(dto);
			}else if(playType == MatchBasketPlayTypeEnum.PLAY_TYPE_WNM.getcode()) {
				String name = BasketBallHILOLeverlEnum.getName(cellName);
				dto.setCellName(name);
				dtos.add(dto);
			}else if(playType == MatchBasketPlayTypeEnum.PLAY_TYPE_HILO.getcode()) {
				String name = MatchBasketBallResultHILOEnum.getName(cellName);
				dto.setCellName(name);
				dtos.add(dto);
			}
		}
		return dtos;
	}
	
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
    				logger.error("getBetInfoByOrderInfo ticket has error, orderSn="+orderSn+ " ticket="+tikcket);
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
    	DLZQBetInfoDTO betInfo = this.getBetProgram(param);
    	
    	List<DLZQOrderLotteryBetInfoDTO> betCells = betInfo.getBetCells();
    	List<LotteryPrint> byOrderSn = lotteryPrintMapper.getByOrderSn(orderSn);
    	betCells.forEach(betCell->{
    		String stakes = betCell.getStakes();
//    		logger.info("DLZQOrderLotteryBetInfoDTO stakes: " + stakes+ " ordersn: "+ orderSn);
    		for(LotteryPrint lPrint: byOrderSn) {
    			if(stakes.equals(lPrint.getStakes())) {
    				betCell.setStatus(lPrint.getStatus());
//    				logger.info("DLZQOrderLotteryBetInfoDTO stakes: " + stakes + " ordersn: "+ orderSn+" lPrint:"+ lPrint.getTicketId() + "  status:"+lPrint.getStatus());
//    				logger.info("betCell status:" + betCell.getStatus());
    				break;
    			}
    		}
    	});
    	return betInfo;
	}
//	@Transactional(readOnly=true)
	public List<LotteryPrintDTO> getPrintLotteryListByOrderInfo(OrderInfoAndDetailDTO  orderInfo, String orderSn) {
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
					logger.error("getBetInfoByOrderInfo ticket has error, orderSn="+orderSn+ " ticket="+tikcket);
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
		List<LotteryPrintDTO> printLotteryList = this.getPrintLotteryList(param);
		return printLotteryList;
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
		matchInfo.setHomeTeamRank(lotteryMatch.getHomeTeamRank());
    	matchInfo.setLeagueAddr(lotteryMatch.getLeagueAddr());
    	matchInfo.setMatchId(lotteryMatch.getMatchId());
    	Date matchTimeDate = lotteryMatch.getMatchTime();
		Instant instant = matchTimeDate.toInstant();
		int matchTime = Long.valueOf(instant.getEpochSecond()).intValue();
    	matchInfo.setMatchTime(matchTime);
    	matchInfo.setVisitingTeamAbbr(lotteryMatch.getVisitingTeamAbbr());
    	Integer visitingTeamId = lotteryMatch.getVisitingTeamId();
		matchInfo.setVisitingTeamId(visitingTeamId);
		matchInfo.setVisitingTeamRank(lotteryMatch.getVisitingTeamRank());
    	List<LotteryMatchPlay> matchPlayList = lotteryMatchPlayMapper.matchPlayListByChangciIds(new Integer[] {lotteryMatch.getChangciId()}, MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode()+"");
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
		List<LotteryMatch> lotteryMatchs = lotteryMatchMapper.getByTeamIdForhv(visitingTeamId, 15);
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
		List<LotteryMatch> lotteryMatchs = lotteryMatchMapper.getByTeamIdForhv(homeTeamId, 15);
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
	/**
	 * 历史交锋
	 * @param lotteryMatch
	 * @return
	 */
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
						if(homeTeamId.equals(match.getHomeTeamId())) {
							matchInfo.setMatchRs("胜");
							win++;
						}else {
							matchInfo.setMatchRs("负");
							lose++;
						}
					}else if(h < a) {
						if(homeTeamId.equals(match.getHomeTeamId())) {
							matchInfo.setMatchRs("负");
							lose++;
						}else {
							matchInfo.setMatchRs("胜");
							win++;
						}
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
	 * 获取当天比赛的league信息
	 * @return
	 */
	public List<LeagueInfoDTO> getFilterConditionsSomeDay(String dateStr) {
		if(StringUtils.isEmpty(dateStr)) {
			dateStr = DateUtil.getCurrentDateTime(LocalDateTime.now(), DateUtil.date_sdf);
		}
		List<LeagueInfoDTO> filterConditions = lotteryMatchMapper.getFilterConditionsSomeDay(dateStr);
		if(filterConditions == null) {
			filterConditions = new ArrayList<LeagueInfoDTO>(0);
		}
		return filterConditions;
	}
	/**
	 * 获取取消赛事
	 * @param playCodes
	 * @return
	 */
	public List<String> getCancelMatches(List<String> playCodes) {
		List<String> filterConditions = lotteryMatchMapper.getCancelMatches(playCodes);
		if(filterConditions == null) {
			filterConditions = new ArrayList<String>(0);
		}
		return filterConditions;
	}

	/**
	 * 历史赛事入库
	 * @throws IOException 
	 */
//	@Transactional
	public BaseResult<String> historyMatchIntoDB() {
//		String separator = File.separator;
//		File file=new File(filepath);
//		
//		if(!file.exists()) {
//			return ResultGenerator.genSuccessResult("目录不存在");
//		}
//		
//		if(!file.isDirectory()) {
//			return ResultGenerator.genSuccessResult("文件不存在");
//		}
//		
//		List<String> pathsList = new ArrayList<String>();
//		String realPath = "";
//		if(filepath.contains("\\")) {
//			String[] filePathArr = filepath.split("\\");
//			pathsList = Arrays.asList(filePathArr);
//		}
//		
//		if(filepath.contains("/")) {
//			String[] filePathArr = filepath.split("/");
//			pathsList = Arrays.asList(filePathArr);
//		}
//		
//		for(String str:pathsList) {
//			realPath += str + separator;
//		}
		String realPath = "D:\\historyMatch.json";
        String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(realPath)));
			if(StringUtils.isEmpty(content)) {
				ResultGenerator.genSuccessResult("历史赛事json文件 为空，未进行入库");
			}
		} catch (Exception e) {
			logger.error("解析历史赛事json文件出错:"+e.getMessage());
			return ResultGenerator.genFailResult("解析历史赛事json文件出错");
		}
		
		List<Object> matchList = JSON.parseArray(content);
		if(CollectionUtils.isEmpty(matchList)) {
			return ResultGenerator.genFailResult("解析历史赛事json文件出错");
		}
		
		List<LotteryMatch> lotteryMatchList = new ArrayList<>();
        int cancleSize = 0;
		for(Object s:matchList) {
			JSONObject str = (JSONObject)s;
			if(!str.getString("code").equals("已完成")) {
				cancleSize++;
				continue;
			}
			LotteryMatch m = new LotteryMatch();
			m.setLeagueAddr(str.getString("league_addr"));
			m.setChangciId(null == str.getInteger("cangci_id")?1990:str.getInteger("cangci_id"));
			m.setChangci(str.getString("changci"));
			m.setHomeTeamAbbr(str.getString("home_team_abbr"));
			m.setVisitingTeamAbbr(str.getString("visiting_team_abbr"));
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
		}
		
		int historyMatchSize = lotteryMatchList.size();
		logger.info("历史赛事共"+matchList+"场");
		logger.info("已取消历史赛事共"+cancleSize+"场,不入库");
		
//		while(historyMatchSize > 0) {
//			int num = matchList.size() > 500?500:historyMatchSize;
//			List<LotteryMatch> lotterySubMatchList =  lotteryMatchList.subList(0, num);
//			int rst = lotteryMatchMapper.batchInsertHistoryMatch(lotterySubMatchList);
//			if(rst != lotterySubMatchList.size()) {
//				logger.error("历史赛事入库失败");
//			}
//			lotteryMatchList.removeAll(lotterySubMatchList);
//		}
		
		
		int rst = this.insertBatchHistoryMatch(lotteryMatchList);
		if(rst != 1) {
			return ResultGenerator.genFailResult("历史赛事入库失败");
		}
		return ResultGenerator.genSuccessResult("历史赛事入库成功");
	}
	
	
	/**
	 * 高速批量插入dl_match 10万条数据 18s
	 * @param list
	 * @param peroidId
	 */
	public int insertBatchHistoryMatch(List<LotteryMatch> list) {
		super.save(list);
		return 1;
	}
	//获取今天的可投注金额
	public int canBetMoney() {
		Long zero = (System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset())/1000;
		int startTime = zero.intValue();
		return lotteryPrintMapper.canBetMoney(startTime);
	}
	//是否停售
	public boolean isShutDownBet() {
		int shutDownBetValue = lotteryPrintMapper.shutDownBetValue();
		if(shutDownBetValue == 1) {
			return true;
		}
		//判断用户是否有交易
//		UserDealActionParam param = new UserDealActionParam();
//		param.setUserId(SessionUtil.getUserId());
//		BaseResult<Integer> userDealAction = iSwitchConfigService.userDealAction(param);
//		Integer data = userDealAction.getData();
//		if(null != data && 0 == data) {
//			return true;
//		}
		return false;
	}
	//获取最小投注金额
	public Double getMinBetMoney() {
		Double minBetMoney = lotteryPrintMapper.getMinBetMoney();
		if(minBetMoney == null) {
			minBetMoney = 0.0;
		}
		return minBetMoney;
	}
	public Double printLotteryRoutAmount() {
		return lotteryPrintMapper.printLotteryRoutAmount();
	}
	
	public Integer getBetPreTime() {
		Integer betPreTime = lotteryPrintMapper.getBetPreTime();
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
	/*public boolean isHideMatch(Integer matchTime) {
		Integer betEndTime = this.getBetEndTime(matchTime);
		return this.isHideMatch(betEndTime, matchTime);
	}*/
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
	
	/**
	 * 根据当前时间和最小比赛时间是否在这个范围判断是否可以投注：
	 * 1-5:9-24 点可投注,其他时间不可投
	 * 6-7:9-24 点可投注，其他时间不可投
	 * @return
	 */
	public Boolean canBetByTime(Integer weekday,Integer hour) {
        if(weekday >= 1 && weekday <= 5 && hour >= 9 && hour <= 24) {
        	return true;
        }
        if(weekday >= 6 && weekday <= 7 && hour >= 9 && hour <= 24){
        	return true;
        }
		return false;
	}

	public List<LotteryMatch> queryLatest3Match(){
		return lotteryMatchMapper.queryLatest3Match();
	}
}

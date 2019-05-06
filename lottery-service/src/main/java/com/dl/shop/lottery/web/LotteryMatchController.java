package com.dl.shop.lottery.web;
import com.dl.base.context.BaseContextHandler;
import com.dl.base.enums.MatchPlayTypeEnum;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.JSONHelper;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.*;
import com.dl.lottery.enums.LotteryResultEnum;
import com.dl.lottery.param.DateStrParam;
import com.dl.lottery.param.*;
import com.dl.member.api.ISysConfigService;
import com.dl.member.api.IUserAccountService;
import com.dl.member.api.IUserBonusService;
import com.dl.member.api.IUserService;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.*;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.OrderDTO;
import com.dl.order.param.SubmitOrderParam;
import com.dl.order.param.SubmitOrderParam.TicketDetail;
import com.dl.order.param.UpdateOrderInfoParam;
import com.dl.shop.auth.api.IAuthService;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao2.DlLeagueTeamMapper;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.service.*;
import com.dl.shop.lottery.utils.MD5;
import com.dl.shop.payment.dto.UserBetDetailInfoDTO;
import com.dl.shop.payment.dto.UserBetPayInfoDTO;
import com.dl.shop.payment.enums.PayEnums;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//import com.dl.shop.lottery.service.MerchantService;

/**
* Created by CodeGenerator on 2018/03/21.
*/
@RestController
@RequestMapping("/lottery/match")
public class LotteryMatchController {
	
	private final static Logger logger = Logger.getLogger(LotteryMatchController.class);
    @Resource
    private LotteryMatchService lotteryMatchService;
    @Resource
    private DlMatchBasketballService dlMatchBasketballService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IUserBonusService userBonusService;
    @Resource
    private IUserService userService;
    @Resource
    private DlLeagueInfoService dlLeagueInfoService;
    @Resource
    private IOrderService orderService;
    @Resource
    private DlLeagueMatchAsiaService dlLeagueMatchAsiaService;
    @Resource
    private DlMatchTeamScoreService dlMatchTeamScoreService;
    @Resource
    private DlLeagueMatchEuropeService dlLeagueMatchEuropeService;
    @Resource
    private DlLeagueMatchDaoXiaoService dlLeagueMatchDaoXiaoService;
    @Resource
    private DlMatchSupportService dlMatchSupportService;
    @Resource
    private LotteryMatchPlayService lotteryMatchPlayService;
    @Resource
    private DlFutureMatchService dlFutureMatchService;
    @Resource
    private DlLeagueTeamMapper dlLeagueTeamMapper;
    @Resource
    private DlLeagueTeamService dlLeagueTeamService;
    @Resource
    private DlMatchPlayBasketballService dlMatchPlayBasketballService;
    @Resource
	private ISysConfigService iSysConfigService;
    @Resource
    private IAuthService authService;
	@Resource
	private IUserService iUserService;
//	@Resource
//	private UserMapper userMapper;
	@Resource
	private IUserAccountService iUserAccountService;
	
//    @Resource
//    private MerchantService merchantService;
	
    
    @ApiOperation(value = "获取筛选条件列表-足球", notes = "获取筛选条件列表-足球")
    @PostMapping("/filterConditions")
    public BaseResult<List<LeagueInfoDTO>> getFilterConditions(@Valid @RequestBody GetFilterConditionsParam param) {
    	List<LeagueInfoDTO> leagueInfos = lotteryMatchService.getFilterConditions();
    	return ResultGenerator.genSuccessResult("获取筛选条件列表成功", leagueInfos);
    }
    
    @ApiOperation(value = "获取筛选条件列表-籃球", notes = "获取筛选条件列表-籃球")
    @PostMapping("/filterBasketBallConditions")
    public BaseResult<List<BasketBallLeagueInfoDTO>> getBasketBallFilterConditions(@Valid @RequestBody GetFilterConditionsParam param) {
    	List<BasketBallLeagueInfoDTO> leagueInfos = dlMatchBasketballService.getBasketBallFilterConditions();
    	return ResultGenerator.genSuccessResult("获取筛选条件列表成功", leagueInfos);
    }
    
	@ApiOperation(value = "获取赛事列表-足球", notes = "获取赛事列表-足球")
    @PostMapping("/getMatchList")
    public BaseResult<DlJcZqMatchListDTO> getMatchList(@Valid @RequestBody DlJcZqMatchListParam param) {
		DlJcZqMatchListDTO dlJcZqMatchListDTO = null;
		UserDeviceInfo uinfo = SessionUtil.getUserDevice();
		if(uinfo!=null && "11".equals(uinfo.getAppCodeName())) {
			dlJcZqMatchListDTO = lotteryMatchService.getMatchListQdd(param);
		}else {
			dlJcZqMatchListDTO = lotteryMatchService.getMatchList(param);
		}
		String allMatchCount = dlJcZqMatchListDTO.getAllMatchCount();
		if(allMatchCount == null || Integer.valueOf(allMatchCount) <= 0){
			return ResultGenerator.genResult(LotteryResultEnum.NO_MATCH.getCode(),LotteryResultEnum.NO_MATCH.getMsg());
		}
    	return ResultGenerator.genSuccessResult("获取赛事列表成功", dlJcZqMatchListDTO);
    }
	
	@ApiOperation(value = "获取赛事列表-篮球", notes = "获取赛事列表-篮球")
    @PostMapping("/getBasketBallMatchList")
    public BaseResult<DlJcLqMatchListDTO> getBasketBallMatchList(@Valid @RequestBody DlJcLqMatchListParam param) {
		DlJcLqMatchListDTO dlJcLqMatchListDTO = dlMatchBasketballService.getMatchList(param);
    	return ResultGenerator.genSuccessResult("获取赛事列表成功", dlJcLqMatchListDTO);   
    }
	
	@ApiOperation(value = "保存篮彩投注信息", notes = "保存篮彩投注信息")
	@PostMapping("/saveBasketBallBetInfo")
	public BaseResult<String> saveBasketBallBetInfo(@Valid @RequestBody DlJcLqMatchBetParam param) {
		if(lotteryMatchService.isShutDownBet()) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(), LotteryResultEnum.BET_MATCH_STOP.getMsg());
		}
		List<MatchBasketBallBetPlayDTO> matchBetPlays = param.getMatchBetPlays();
		if(matchBetPlays == null || matchBetPlays.size() < 1) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_EMPTY.getCode(), LotteryResultEnum.BET_CELL_EMPTY.getMsg());
		}
		//设置投注倍数
		Integer times = param.getTimes();
		if(null == times || times < 1) {
			param.setTimes(1);
		}
		if(param.getTimes() >= 99999) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIMES_LIMIT.getCode(), LotteryResultEnum.BET_TIMES_LIMIT.getMsg());
		}
		String playType = param.getPlayType();
		if(StringUtils.isBlank(playType)) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
		}
		try {
			int parseInt = Integer.parseInt(playType);
			if(parseInt < 1 || parseInt > 6 || parseInt == 5) {
				return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
			}
		} catch (NumberFormatException e) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
		}
		//校验赛事投注时间
		MatchBasketBallBetPlayDTO min = matchBetPlays.get(0);
		if(matchBetPlays.size() > 1) {
			min = matchBetPlays.stream().min((cell1,cell2)->cell1.getMatchTime()-cell2.getMatchTime()).get();
		}
		
		int betEndTime = lotteryMatchService.getBetEndTime(min.getMatchTime());
		Date now = new Date();
		int nowTime = Long.valueOf(now.toInstant().getEpochSecond()).intValue();
		if(nowTime - betEndTime > 0) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIME_LIMIT.getCode(), LotteryResultEnum.BET_TIME_LIMIT.getMsg());
		}
		boolean hideMatch = lotteryMatchService.isHideMatch(betEndTime, min.getMatchTime());
		if(hideMatch) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIME_LIMIT.getCode(), LotteryResultEnum.BET_TIME_LIMIT.getMsg());
		}
		//校验串关
		String betTypeStr = param.getBetType();
		if(StringUtils.isBlank(betTypeStr)) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getMsg());
		}
		
		boolean isCellError = false;
		boolean isAllSingle = true;
		for(MatchBasketBallBetPlayDTO betPlay : matchBetPlays){
			List<MatchBasketBallBetCellDTO> matchBetCells = betPlay.getMatchBetCells();
			if(CollectionUtils.isEmpty(matchBetCells)) {
				isCellError = true;
				break;
			}
			for(MatchBasketBallBetCellDTO betCell: matchBetCells){
				List<DlJcLqMatchCellDTO> betCells = betCell.getBetCells();
				if(CollectionUtils.isEmpty(betCells)) {
					isCellError = true;
					break;
				}
				for(DlJcLqMatchCellDTO dto: betCells) {
					String cellCode = dto.getCellCode();
					String cellName = dto.getCellName();
					String cellOdds = dto.getCellOdds();
					if(StringUtils.isBlank(cellCode) || StringUtils.isBlank(cellName) || StringUtils.isBlank(cellOdds)) {
						isCellError = true;
						break;
					}
				}
				Integer single = betCell.getSingle();
				if(single == null || single.equals(0)) {
					isAllSingle = false;
				}
				if(isCellError) {
					break;
				}
			}
			if(isCellError) {
				break;
			}
		}
		//校验投注选项
		if(isCellError) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_HAS_NULL.getCode(), LotteryResultEnum.BET_CELL_HAS_NULL.getMsg());
		}
		if(betTypeStr.contains("11")) {
			if(!isAllSingle) {
				return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_NO_SINGLE.getCode(), LotteryResultEnum.BET_CELL_NO_SINGLE.getMsg());
			}
		}
		String[] betTypes = betTypeStr.split(",");
		boolean isCheckedBetType = true;
		int minBetNum = 9;
		try {
			int maxBetNum = 1;
			for(String betType: betTypes) {
				char[] charArray = betType.toCharArray();
				if(charArray.length == 2 && charArray[1] == '1') {
					int num = Integer.valueOf(String.valueOf(charArray[0]));
					if(num > maxBetNum) {
						maxBetNum = num;
					}
					if(minBetNum > num) {
						minBetNum = num;
					}
					if("3".equals(playType)) {
						if(num > 4) {
							isCheckedBetType = false;
						}
					}else if("4".equals(playType)) {
						if(num > 6) {
							isCheckedBetType = false;
						}
					}else if("6".equals(playType)) {
						if(num == 1) {
							isCheckedBetType = false;
						}
					}					
					if(num < 1 || num > 8) {
						isCheckedBetType = false;
					}
				}
			}
			if(maxBetNum > matchBetPlays.size()) {
				isCheckedBetType = false;
			}
		} catch (NumberFormatException e) {
		}
		if(!isCheckedBetType) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getMsg());
		}
		//校验胆的个数设置
		int danEnableNum = minBetNum -1;
		if(minBetNum == matchBetPlays.size()) {
			danEnableNum = 0;
		}
		long danNum = matchBetPlays.stream().filter(dto->dto.getIsDan() == 1).count();
		if(danNum > danEnableNum) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_DAN_ERR.getCode(), LotteryResultEnum.BET_CELL_DAN_ERR.getMsg());
		}
		//投注计算
		DLLQBetInfoDTO betInfo = dlMatchBasketballService.getBetInfo1(param);
		if(Double.valueOf(betInfo.getMaxLotteryMoney()) >= 20000) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MONEY_LIMIT.getCode(), LotteryResultEnum.BET_MONEY_LIMIT.getMsg());
		}
		int betNum = betInfo.getBetNum();
		if(betNum >= 10000 || betNum < 0) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_NUMBER_LIMIT.getCode(), LotteryResultEnum.BET_NUMBER_LIMIT.getMsg());
		}
		String betMoney = betInfo.getMoney();
		Double orderMoney = Double.valueOf(betMoney);
		Double minBetMoney = lotteryMatchService.getMinBetMoney();
		if(orderMoney < minBetMoney) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_WC.getCode(), "最低投注"+minBetMoney.intValue()+"元!");
		}
		int canBetMoney = lotteryMatchService.canBetMoney();
		if(orderMoney > canBetMoney) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(), LotteryResultEnum.BET_MATCH_STOP.getMsg());
		}
		
		//缓存订单支付信息
		UserBetPayInfoDTO dto = new UserBetPayInfoDTO();
		List<UserBetDetailInfoDTO> betDetailInfos = new ArrayList<UserBetDetailInfoDTO>(matchBetPlays.size());
		for(MatchBasketBallBetPlayDTO matchCell: matchBetPlays) {
			UserBetDetailInfoDTO dizqUserBetCellInfoDTO = new UserBetDetailInfoDTO();
			dizqUserBetCellInfoDTO.setMatchId(matchCell.getMatchId());
			dizqUserBetCellInfoDTO.setChangci(matchCell.getChangci());
			dizqUserBetCellInfoDTO.setIsDan(matchCell.getIsDan());
			dizqUserBetCellInfoDTO.setLotteryClassifyId(matchCell.getLotteryClassifyId());
			dizqUserBetCellInfoDTO.setLotteryPlayClassifyId(matchCell.getLotteryPlayClassifyId());
			dizqUserBetCellInfoDTO.setMatchTeam(matchCell.getMatchTeam());
			dizqUserBetCellInfoDTO.setMatchTime(matchCell.getMatchTime());
			String playCode = matchCell.getPlayCode();
			dizqUserBetCellInfoDTO.setPlayCode(playCode);
			List<MatchBasketBallBetCellDTO> matchBetCells = matchCell.getMatchBetCells();
			String ticketData = matchBetCells.stream().map(betCell->{
				String ticketData1 = "0" + betCell.getPlayType() + "|" + playCode + "|";
				return ticketData1 + betCell.getBetCells().stream().map(cell->cell.getCellCode()+"@"+cell.getCellOdds())
						.collect(Collectors.joining(","));
			}).collect(Collectors.joining(";"));
			dizqUserBetCellInfoDTO.setTicketData(ticketData);
			Optional<MatchBasketBallBetCellDTO> findFirst = matchCell.getMatchBetCells().stream().filter(item->Integer.valueOf(item.getPlayType()).equals(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode())).findFirst();
			if(findFirst.isPresent()) {
				String fixOdds = findFirst.get().getFixedOdds();
				logger.info("**************************fixOdds="+fixOdds);
				dizqUserBetCellInfoDTO.setFixedodds(fixOdds);
			}
			betDetailInfos.add(dizqUserBetCellInfoDTO);
		}
		dto.setTimes(param.getTimes());
		dto.setBetType(param.getBetType());
		dto.setPlayType(param.getPlayType());
		dto.setLotteryClassifyId(param.getLotteryClassifyId());
		dto.setLotteryPlayClassifyId(param.getLotteryPlayClassifyId());
		dto.setBetDetailInfos(betDetailInfos);
		dto.setBetNum(betNum);
		dto.setTicketNum(betInfo.getTicketNum());
		dto.setOrderMoney(orderMoney);
//		dto.setBonusAmount(bonusAmount);
//		dto.setBonusId(bonusId);
//		dto.setSurplus(surplus);
		String forecastMoney = betInfo.getMinBonus() + "~" + betInfo.getMaxBonus();
		dto.setForecastMoney(forecastMoney);
//		dto.setThirdPartyPaid(thirdPartyPaid);
		String requestFrom = "0";
		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		if(userDevice != null) {
			requestFrom = userDevice.getPlat();
		}
		dto.setRequestFrom(requestFrom);
		dto.setUserId(SessionUtil.getUserId());
		dto.setIssue(betInfo.getIssue());
		String dtoJson = JSONHelper.bean2json(dto);
		String keyStr = "bet_info_" + SessionUtil.getUserId() +"_"+ System.currentTimeMillis();
		String payToken = MD5.crypt(keyStr);
		stringRedisTemplate.opsForValue().set(payToken, dtoJson, ProjectConstant.BET_INFO_EXPIRE_TIME, TimeUnit.MINUTES);
		return ResultGenerator.genSuccessResult("success", payToken);
	}
	
	@ApiOperation(value = "计算篮球投注信息", notes = "计算篮球投注信息,times默认值为1，betType默认值为11")
	@PostMapping("/getBasketBallBetInfo")
	public BaseResult<DLLQBetInfoDTO> getBasketBallBetInfo(@Valid @RequestBody DlJcLqMatchBetParam param) {
		//是否停售
		if(dlMatchBasketballService.isBasketBallShutDownBet()) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(), LotteryResultEnum.BET_MATCH_STOP.getMsg());
		}
		List<MatchBasketBallBetPlayDTO> matchBetPlays = param.getMatchBetPlays();
		if(matchBetPlays == null || matchBetPlays.size() < 1) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_EMPTY.getCode(), LotteryResultEnum.BET_CELL_EMPTY.getMsg());
		}else if(matchBetPlays.size() > 8) {

		}

		//设置投注倍数
		Integer times = param.getTimes();
		if(null == times || times < 1) {
			param.setTimes(1);
		}
		if(param.getTimes() >= 99999) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIMES_LIMIT.getCode(), LotteryResultEnum.BET_TIMES_LIMIT.getMsg());
		}
		//playType校验
		String playType = param.getPlayType();
		if(StringUtils.isBlank(playType)) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
		}
		try {
			int parseInt = Integer.parseInt(playType);
			if(parseInt < 1 || parseInt > 6 || parseInt == 5) {
				return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
			}
		} catch (NumberFormatException e) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
		}
		//校验赛事投注时间
		MatchBasketBallBetPlayDTO min = matchBetPlays.get(0);
		if(matchBetPlays.size() > 1) {
			min = matchBetPlays.stream().min((cell1,cell2)->cell1.getMatchTime()-cell2.getMatchTime()).get();
		}
		
		int betEndTime = dlMatchBasketballService.getBetEndTimeNew(min.getMatchTime());		
		Instant betEndInstant = Instant.ofEpochSecond(betEndTime);
		LocalDateTime betEndDateTime = LocalDateTime.ofInstant(betEndInstant, ZoneId.systemDefault());
		Boolean betEndTimeCanBz = lotteryMatchService.canBetByTime(betEndDateTime.getDayOfWeek().getValue(), betEndDateTime.getHour());
		
		Integer curTime = DateUtil.getCurrentTimeLong();
		Instant instant = Instant.ofEpochSecond(curTime);
		LocalDateTime curDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		Boolean curTimeCanBz = lotteryMatchService.canBetByTime(curDateTime.getDayOfWeek().getValue(), curDateTime.getHour());		
		if(curTime - betEndTime >= 0) {//投注截止时间不能大于当前时间
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIME_LIMIT.getCode(), LotteryResultEnum.BET_TIME_LIMIT.getMsg());
		}
		if(curTimeCanBz && betEndTimeCanBz) {//投注截止时间或当前时间都不在足彩的售卖时间内
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIME_LIMIT.getCode(), LotteryResultEnum.BET_TIME_LIMIT.getMsg());
		}
		
		//校验篮彩的串关
		String betTypeStr = param.getBetType();
		//校验投注选项，主要查看各个cell值是否为空
		boolean isCellError = false;
		boolean isAllSingle = true;
		for(MatchBasketBallBetPlayDTO betPlay : matchBetPlays){
			List<MatchBasketBallBetCellDTO> matchBetCells = betPlay.getMatchBetCells();
			if(CollectionUtils.isEmpty(matchBetCells)) {
				isCellError = true;
				break;
			}
			for(MatchBasketBallBetCellDTO betCell: matchBetCells){
				List<DlJcLqMatchCellDTO> betCells = betCell.getBetCells();
				if(CollectionUtils.isEmpty(betCells)) {
					isCellError = true;
					break;
				}
				for(DlJcLqMatchCellDTO dto: betCells) {
					String cellCode = dto.getCellCode();
					String cellName = dto.getCellName();
					String cellOdds = dto.getCellOdds();
					if(StringUtils.isBlank(cellCode) || StringUtils.isBlank(cellName) || StringUtils.isBlank(cellOdds)) {
						isCellError = true;
						break;
					}
				}
				Integer single = betCell.getSingle();
				if(single == null || single.equals(0)) {
					isAllSingle = false;
				}
				if(isCellError) {
					break;
				}
			}
			if(isCellError) {
				break;
			}
		}
		if(isCellError) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_HAS_NULL.getCode(), LotteryResultEnum.BET_CELL_HAS_NULL.getMsg());
		}
		if(betTypeStr.contains("11")) {
			if(!isAllSingle) {
				return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_NO_SINGLE.getCode(), LotteryResultEnum.BET_CELL_NO_SINGLE.getMsg());
			}
		}
		
		String[] betTypes = betTypeStr.split(",");
		boolean isCheckedBetType = true;
		int minBetNum = 9;
		try {
			int maxBetNum = 1;
			for(String betType: betTypes) {
				char[] charArray = betType.toCharArray();
				if(charArray.length == 2 && charArray[1] == '1') {
					int num = Integer.valueOf(String.valueOf(charArray[0]));
					if(num > maxBetNum) {
						maxBetNum = num;
					}
					if(minBetNum > num) {
						minBetNum = num;
					}
					if("3".equals(playType)) {
						if(num > 4) {
							isCheckedBetType = false;
						}
					}else if("4".equals(playType)) {
						if(num > 6) {
							isCheckedBetType = false;
						}
					}else if("6".equals(playType)) {
						if(num == 1) {
							isCheckedBetType = false;
						}
					}
					if(num < 1 || num > 8) {
						isCheckedBetType = false;
					}
				}
			}
			if(maxBetNum > matchBetPlays.size()) {
				isCheckedBetType = false;
			}
		} catch (NumberFormatException e) {
		}
		
		if(!isCheckedBetType) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getMsg());
		}
		//校验胆的个数设置
		int danEnableNum = minBetNum -1;
		if(minBetNum == matchBetPlays.size()) {
			danEnableNum = 0;
		}
		long danNum = matchBetPlays.stream().filter(dto->dto.getIsDan() == 1).count();
		if(danNum > danEnableNum) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_DAN_ERR.getCode(), LotteryResultEnum.BET_CELL_DAN_ERR.getMsg());
		}
		DLLQBetInfoDTO betInfo = dlMatchBasketballService.getBetInfo1(param);
		if(Double.valueOf(betInfo.getMaxLotteryMoney()) >= 20000) {
			return ResultGenerator.genSuccessResult(LotteryResultEnum.BET_MONEY_LIMIT.getMsg(), betInfo);
		}
		int betNum = betInfo.getBetNum();

		if(betNum >= 10000 || betNum < 0) {
			return ResultGenerator.genSuccessResult(LotteryResultEnum.BET_NUMBER_LIMIT.getMsg(), betInfo);
		}
		String betMoney = betInfo.getMoney();
		Double orderMoney = Double.valueOf(betMoney);
		Double minBetMoney = lotteryMatchService.getMinBetMoney();
		if(orderMoney < minBetMoney) {
			return ResultGenerator.genSuccessResult("最低投注"+minBetMoney.intValue()+"元!", betInfo);
		}

		return ResultGenerator.genSuccessResult("", betInfo);
	}

	@ApiOperation(value = "计算投注信息", notes = "计算投注信息,times默认值为1，betType默认值为11")
	@PostMapping("/getBetInfo")
	public BaseResult<DLZQBetInfoDTO> getBetInfo(@Valid @RequestBody DlJcZqMatchBetParam param) {
		if(lotteryMatchService.isShutDownBet()) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(), LotteryResultEnum.BET_MATCH_STOP.getMsg());
		}
		List<MatchBetPlayDTO> matchBetPlays = param.getMatchBetPlays();
		if(matchBetPlays == null || matchBetPlays.size() < 1) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_EMPTY.getCode(), LotteryResultEnum.BET_CELL_EMPTY.getMsg());
		}else if(matchBetPlays.size() > 8){
			return ResultGenerator.genResult(LotteryResultEnum.MATCH_BEYOND_EIGHT.getCode(), LotteryResultEnum.MATCH_BEYOND_EIGHT.getMsg());
		}

		//设置投注倍数
		Integer times = param.getTimes();
		if(null == times || times < 1) {
			param.setTimes(1);
		}
		if(param.getTimes() >= 99999) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIMES_LIMIT.getCode(), LotteryResultEnum.BET_TIMES_LIMIT.getMsg());
		}
		//
		String playType = param.getPlayType();
		if(StringUtils.isBlank(playType)) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
		}
		try {
			int parseInt = Integer.parseInt(playType);
			if(parseInt < 1 || parseInt > 7) {
				return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
			}
		} catch (NumberFormatException e) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
		}

		//校验赛事投注时间
		MatchBetPlayDTO min = matchBetPlays.get(0);
		if(matchBetPlays.size() > 1) {
			min = matchBetPlays.stream().min((cell1,cell2)->cell1.getMatchTime()-cell2.getMatchTime()).get();
		}
		int betEndTime = lotteryMatchService.getBetEndTime(min.getMatchTime());
		Date now = new Date();
		int nowTime = Long.valueOf(now.toInstant().getEpochSecond()).intValue();
		if(nowTime - betEndTime > 0) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIME_LIMIT.getCode(), LotteryResultEnum.BET_TIME_LIMIT.getMsg());
		}
		boolean hideMatch = lotteryMatchService.isHideMatch(betEndTime, min.getMatchTime());
		if(hideMatch) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIME_LIMIT.getCode(), LotteryResultEnum.BET_TIME_LIMIT.getMsg());
		}
		//校验串关
		String betTypeStr = param.getBetType();
		if(StringUtils.isBlank(betTypeStr)) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getMsg());
		}

		boolean isCellError = false;
		boolean isAllSingle = true;
		for(MatchBetPlayDTO betPlay : matchBetPlays){
			List<MatchBetCellDTO> matchBetCells = betPlay.getMatchBetCells();
			if(CollectionUtils.isEmpty(matchBetCells)) {
				isCellError = true;
				break;
			}
			for(MatchBetCellDTO betCell: matchBetCells){
				List<DlJcZqMatchCellDTO> betCells = betCell.getBetCells();
				if(CollectionUtils.isEmpty(betCells)) {
					isCellError = true;
					break;
				}
				for(DlJcZqMatchCellDTO dto: betCells) {
					String cellCode = dto.getCellCode();
					String cellName = dto.getCellName();
					String cellOdds = dto.getCellOdds();
					if(StringUtils.isBlank(cellCode) || StringUtils.isBlank(cellName) || StringUtils.isBlank(cellOdds)) {
						isCellError = true;
						break;
					}
				}
				Integer single = betCell.getSingle();
				if(single == null || single.equals(0)) {
					isAllSingle = false;
				}
				if(isCellError) {
					break;
				}
			}
			if(isCellError) {
				break;
			}
		}
		//校验投注选项
		if(isCellError) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_HAS_NULL.getCode(), LotteryResultEnum.BET_CELL_HAS_NULL.getMsg());
		}
		if(betTypeStr.contains("11")) {
			if(!isAllSingle) {
				return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_NO_SINGLE.getCode(), LotteryResultEnum.BET_CELL_NO_SINGLE.getMsg());
			}
		}
		String[] betTypes = betTypeStr.split(",");
		boolean isCheckedBetType = true;
		int minBetNum = 9;
		try {
			int maxBetNum = 1;
			for(String betType: betTypes) {
				char[] charArray = betType.toCharArray();
				if(charArray.length == 2 && charArray[1] == '1') {
					int num = Integer.valueOf(String.valueOf(charArray[0]));
					if(num > maxBetNum) {
						maxBetNum = num;
					}
					if(minBetNum > num) {
						minBetNum = num;
					}
					if(num < 1 || num > 8) {
						isCheckedBetType = false;
					}
				}
			}
			if(maxBetNum > matchBetPlays.size()) {
				isCheckedBetType = false;
			}
		} catch (NumberFormatException e) {
		}
		if(!isCheckedBetType) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getMsg());
		}
		//校验胆的个数设置
		int danEnableNum = minBetNum -1;
		if(minBetNum == matchBetPlays.size()) {
			danEnableNum = 0;
		}
		long danNum = matchBetPlays.stream().filter(dto->dto.getIsDan() == 1).count();
		if(danNum > danEnableNum) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_DAN_ERR.getCode(), LotteryResultEnum.BET_CELL_DAN_ERR.getMsg());
		}
		
		DLZQBetInfoDTO betInfo = lotteryMatchService.getBetInfo1(param);
		if(Double.valueOf(betInfo.getMaxLotteryMoney()) >= 20000) {
//			return ResultGenerator.genResult(LotteryResultEnum.BET_MONEY_LIMIT.getCode(), LotteryResultEnum.BET_MONEY_LIMIT.getMsg());
			return ResultGenerator.genSuccessResult(LotteryResultEnum.BET_MONEY_LIMIT.getMsg(), betInfo);
		}
		int betNum = betInfo.getBetNum();
		if(betNum >= 10000 || betNum < 0) {
//			return ResultGenerator.genResult(LotteryResultEnum.BET_NUMBER_LIMIT.getCode(), LotteryResultEnum.BET_NUMBER_LIMIT.getMsg());
			return ResultGenerator.genSuccessResult(LotteryResultEnum.BET_NUMBER_LIMIT.getMsg(), betInfo);
		}
		String betMoney = betInfo.getMoney();
		Double orderMoney = Double.valueOf(betMoney);
		Double minBetMoney = lotteryMatchService.getMinBetMoney();
		if(orderMoney < minBetMoney) {
			return ResultGenerator.genSuccessResult("最低投注"+minBetMoney.intValue()+"元!", betInfo);
		}
		int canBetMoney = lotteryMatchService.canBetMoney();
		if(orderMoney > canBetMoney) {
			return ResultGenerator.genSuccessResult(LotteryResultEnum.BET_MATCH_STOP.getMsg(), betInfo);
		}
//		问题六：预测奖金后面需要添加单位：元；
		betInfo.setMaxBonus(betInfo.getMaxBonus() + "元");
		
		return ResultGenerator.genSuccessResult("", betInfo);
	}
	
	@ApiOperation(value = "保存投注信息", notes = "保存投注信息")
	@PostMapping("/saveBetInfo")
	public BaseResult<BetPayInfoDTO> saveBetInfo(@Valid @RequestBody DlJcZqMatchBetParam param) {
		if(lotteryMatchService.isShutDownBet()) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(), LotteryResultEnum.BET_MATCH_STOP.getMsg());
		}
		List<MatchBetPlayDTO> matchBetPlays = param.getMatchBetPlays();
		if(matchBetPlays == null || matchBetPlays.size() < 1) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_EMPTY.getCode(), LotteryResultEnum.BET_CELL_EMPTY.getMsg());
		}
		//设置投注倍数
		Integer times = param.getTimes();
		if(null == times || times < 1) {
			param.setTimes(1);
		}
		if(param.getTimes() >= 99999) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIMES_LIMIT.getCode(), LotteryResultEnum.BET_TIMES_LIMIT.getMsg());
		}
		String playType = param.getPlayType();
		if(StringUtils.isBlank(playType)) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
		}
		try {
			int parseInt = Integer.parseInt(playType);
			if(parseInt < 1 || parseInt > 7) {
				return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
			}
		} catch (NumberFormatException e) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
		}
		//2 1
		if(Integer.valueOf(playType).equals(MatchPlayTypeEnum.PLAY_TYPE_TSO.getcode())) {
			return ResultGenerator.genFailResult("暂不支持该玩法！", null);
		}
		//校验赛事投注时间
		MatchBetPlayDTO min = matchBetPlays.get(0);
		if(matchBetPlays.size() > 1) {
			min = matchBetPlays.stream().min((cell1,cell2)->cell1.getMatchTime()-cell2.getMatchTime()).get();
		}

		int betEndTime = dlMatchBasketballService.getBetEndTimeNew(min.getMatchTime());		
		Instant betEndInstant = Instant.ofEpochSecond(betEndTime);
		LocalDateTime betEndDateTime = LocalDateTime.ofInstant(betEndInstant, ZoneId.systemDefault());
		Boolean betEndTimeCanBz = lotteryMatchService.canBetByTime(betEndDateTime.getDayOfWeek().getValue(), betEndDateTime.getHour());
		
		Integer curTime = DateUtil.getCurrentTimeLong();
		Instant instant = Instant.ofEpochSecond(curTime);
		LocalDateTime curDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		Boolean curTimeCanBz = lotteryMatchService.canBetByTime(curDateTime.getDayOfWeek().getValue(), curDateTime.getHour());		
		if(curTime - betEndTime >= 0) {//投注截止时间不能大于当前时间
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIME_LIMIT.getCode(), LotteryResultEnum.BET_TIME_LIMIT.getMsg());
		}
		if(curTimeCanBz && betEndTimeCanBz) {//投注截止时间或当前时间都不在足彩的售卖时间内
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIME_LIMIT.getCode(), LotteryResultEnum.BET_TIME_LIMIT.getMsg());
		}
		
		//校验串关
		String betTypeStr = param.getBetType();
		if(StringUtils.isBlank(betTypeStr)) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getMsg());
		}
		
		boolean isCellError = false;
		boolean isAllSingle = true;
		for(MatchBetPlayDTO betPlay : matchBetPlays){
			List<MatchBetCellDTO> matchBetCells = betPlay.getMatchBetCells();
			if(CollectionUtils.isEmpty(matchBetCells)) {
				isCellError = true;
				break;
			}
			for(MatchBetCellDTO betCell: matchBetCells){
				List<DlJcZqMatchCellDTO> betCells = betCell.getBetCells();
				if(CollectionUtils.isEmpty(betCells)) {
					isCellError = true;
					break;
				}
				for(DlJcZqMatchCellDTO dto: betCells) {
					String cellCode = dto.getCellCode();
					String cellName = dto.getCellName();
					String cellOdds = dto.getCellOdds();
					if(StringUtils.isBlank(cellCode) || StringUtils.isBlank(cellName) || StringUtils.isBlank(cellOdds)) {
						isCellError = true;
						break;
					}
				}
				Integer single = betCell.getSingle();
				if(single == null || single.equals(0)) {
					isAllSingle = false;
				}
				if(isCellError) {
					break;
				}
			}
			if(isCellError) {
				break;
			}
		}
		//校验投注选项
		if(isCellError) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_HAS_NULL.getCode(), LotteryResultEnum.BET_CELL_HAS_NULL.getMsg());
		}
		if(betTypeStr.contains("11")) {
			if(!isAllSingle) {
				return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_NO_SINGLE.getCode(), LotteryResultEnum.BET_CELL_NO_SINGLE.getMsg());
			}
		}
		String[] betTypes = betTypeStr.split(",");
		boolean isCheckedBetType = true;
		int minBetNum = 9;
		try {
			int maxBetNum = 1;
			for(String betType: betTypes) {
				char[] charArray = betType.toCharArray();
				if(charArray.length == 2 && charArray[1] == '1') {
					int num = Integer.valueOf(String.valueOf(charArray[0]));
					if(num > maxBetNum) {
						maxBetNum = num;
					}
					if(minBetNum > num) {
						minBetNum = num;
					}
					if(num < 1 || num > 8) {
						isCheckedBetType = false;
					}
				}
			}
			if(maxBetNum > matchBetPlays.size()) {
				isCheckedBetType = false;
			}
		} catch (NumberFormatException e) {
		}
		if(!isCheckedBetType) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getMsg());
		}
		//校验胆的个数设置
		int danEnableNum = minBetNum -1;
		if(minBetNum == matchBetPlays.size()) {
			danEnableNum = 0;
		}
		long danNum = matchBetPlays.stream().filter(dto->dto.getIsDan() == 1).count();
		if(danNum > danEnableNum) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_DAN_ERR.getCode(), LotteryResultEnum.BET_CELL_DAN_ERR.getMsg());
		}
		//用户信息
		StrParam strParam = new StrParam();
		BaseResult<UserDTO> userInfoExceptPassRst = userService.userInfoExceptPass(strParam);
		if(userInfoExceptPassRst.getCode() != 0 || null == userInfoExceptPassRst.getData()) {
			return ResultGenerator.genResult(LotteryResultEnum.OPTION_ERROR.getCode(), LotteryResultEnum.OPTION_ERROR.getMsg());
		}

		DLZQBetInfoDTO betInfo = lotteryMatchService.getBetInfo1(param);
		if(Double.valueOf(betInfo.getMaxLotteryMoney()) >= 20000) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MONEY_LIMIT.getCode(), LotteryResultEnum.BET_MONEY_LIMIT.getMsg());
		}
		int betNum = betInfo.getBetNum();
		if(betNum >= 10000 || betNum < 0) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_NUMBER_LIMIT.getCode(), LotteryResultEnum.BET_NUMBER_LIMIT.getMsg());
		}
		String betMoney = betInfo.getMoney();
		Double orderMoney = Double.valueOf(betMoney);
		Double minBetMoney = lotteryMatchService.getMinBetMoney();
		if(orderMoney < minBetMoney) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_WC.getCode(), "最低投注"+minBetMoney.intValue()+"元!");
		}
		int canBetMoney = lotteryMatchService.canBetMoney();
		if(orderMoney > canBetMoney) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(), LotteryResultEnum.BET_MATCH_STOP.getMsg());
		}
		String totalMoney = userInfoExceptPassRst.getData().getTotalMoney();
		Double userTotalMoney = Double.valueOf(totalMoney);
		
		//红包包
		BonusLimitConditionParam bonusLimitConditionParam = new BonusLimitConditionParam();
		bonusLimitConditionParam.setOrderMoneyPaid(BigDecimal.valueOf(orderMoney));
		BaseResult<List<UserBonusDTO>> userBonusListRst = userBonusService.queryValidBonusList(bonusLimitConditionParam);
		if(userBonusListRst.getCode() != 0) {
			return ResultGenerator.genResult(LotteryResultEnum.OPTION_ERROR.getCode(), LotteryResultEnum.OPTION_ERROR.getMsg());
		}
		
		List<UserBonusDTO> userBonusList = userBonusListRst.getData();
		UserBonusDTO userBonusDto = null;
		if(!CollectionUtils.isEmpty(userBonusList)) {
			if(param.getBonusId() != null && param.getBonusId().intValue() != 0) {//有红包id
				if(param.getBonusId().intValue() != -1) {
					Optional<UserBonusDTO> findFirst = userBonusList.stream().filter(dto->dto.getUserBonusId().equals(param.getBonusId())).findFirst();
					userBonusDto = findFirst.isPresent()?findFirst.get():null;
				}
			}else {//没有传红包id
				List<UserBonusDTO> userBonuses = userBonusList.stream().filter(dto->{
					double minGoodsAmount = dto.getBonusPrice().doubleValue();
					return orderMoney < minGoodsAmount ? false : true;
				}).sorted((n1,n2)->n2.getBonusPrice().compareTo(n1.getBonusPrice()))
						.collect(Collectors.toList());
				if(userBonuses.size() > 0) {
					/*if(null != param.getBonusId()) {
						Optional<UserBonusDTO> findFirst = userBonusList.stream().filter(dto->dto.getBonusId()==param.getBonusId()).findFirst();
						userBonusDto = findFirst.isPresent()?findFirst.get():null;
					}*/
					userBonusDto = userBonuses.get(0);//userBonusDto == null?userBonuses.get(0):userBonusDto;
				}
			}
		}
		String bonusId = userBonusDto != null?userBonusDto.getUserBonusId().toString():null;
		Double bonusAmount = userBonusDto!=null?userBonusDto.getBonusPrice().doubleValue():0.0;
		Double amountTemp = orderMoney - bonusAmount;//红包扣款后的金额
		Double surplus = 0.0;
		Double thirdPartyPaid = 0.0;
		if(amountTemp < 0) {//红包大于订单金额
			bonusAmount = orderMoney;
		}else {
			surplus = userTotalMoney>amountTemp?amountTemp:userTotalMoney;
			thirdPartyPaid = amountTemp - surplus;
		}
		
		//缓存订单支付信息
		DIZQUserBetInfoDTO dto = new DIZQUserBetInfoDTO(param);
		List<DIZQUserBetCellInfoDTO>  userBetCellInfos = new ArrayList<DIZQUserBetCellInfoDTO>(matchBetPlays.size());
		for(MatchBetPlayDTO matchCell: matchBetPlays) {
			DIZQUserBetCellInfoDTO dizqUserBetCellInfoDTO = new DIZQUserBetCellInfoDTO(matchCell);
			Optional<MatchBetCellDTO> findFirst = matchCell.getMatchBetCells().stream().filter(item->Integer.valueOf(item.getPlayType()).equals(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode())).findFirst();
			if(findFirst.isPresent()) {
				String fixOdds = findFirst.get().getFixedOdds();
				logger.info("**************************fixOdds="+fixOdds);
				dizqUserBetCellInfoDTO.setFixedodds(fixOdds);
			}
			userBetCellInfos.add(dizqUserBetCellInfoDTO);
		}
		dto.setUserBetCellInfos(userBetCellInfos);
		dto.setBetNum(betNum);
		dto.setTicketNum(betInfo.getTicketNum());
		dto.setMoney(orderMoney);
		dto.setBonusAmount(bonusAmount);
		dto.setBonusId(bonusId);
		dto.setSurplus(surplus);
		String forecastMoney = betInfo.getMinBonus() + "~" + betInfo.getMaxBonus();
		dto.setForecastMoney(forecastMoney);
		dto.setThirdPartyPaid(thirdPartyPaid);
		String requestFrom = "0";
		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		if(userDevice != null) {
			requestFrom = userDevice.getPlat();
		}
		dto.setRequestFrom(requestFrom);
		dto.setUserId(SessionUtil.getUserId());
		dto.setIssue(betInfo.getIssue());
		String dtoJson = JSONHelper.bean2json(dto);
		String keyStr = "bet_info_" + SessionUtil.getUserId() +"_"+ System.currentTimeMillis();
		String key = MD5.crypt(keyStr);
		stringRedisTemplate.opsForValue().set(key, dtoJson, ProjectConstant.BET_INFO_EXPIRE_TIME, TimeUnit.MINUTES);
		//返回页面信息
		BetPayInfoDTO betPlayInfoDTO = new BetPayInfoDTO();
		betPlayInfoDTO.setPayToken(key);
		betPlayInfoDTO.setBonusAmount(String.format("%.2f", bonusAmount));
		betPlayInfoDTO.setBonusId(bonusId);
		betPlayInfoDTO.setBonusList(userBonusList);
		betPlayInfoDTO.setOrderMoney(betMoney);
		betPlayInfoDTO.setSurplus(String.format("%.2f", surplus));
		betPlayInfoDTO.setThirdPartyPaid(String.format("%.2f", thirdPartyPaid));
		return ResultGenerator.genSuccessResult("success", betPlayInfoDTO);
	}
		
	@ApiOperation(value = "保存投注信息", notes = "保存投注信息")
	@PostMapping("/nSaveBetInfo")
	public BaseResult<String> nSaveBetInfo(@Valid @RequestBody DlJcZqMatchBetParam param) {
		if(lotteryMatchService.isShutDownBet()) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(), LotteryResultEnum.BET_MATCH_STOP.getMsg());
		}
		List<MatchBetPlayDTO> matchBetPlays = param.getMatchBetPlays();
		if(matchBetPlays == null || matchBetPlays.size() < 1) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_EMPTY.getCode(), LotteryResultEnum.BET_CELL_EMPTY.getMsg());
		}
		//设置投注倍数
		Integer times = param.getTimes();
		if(null == times || times < 1) {
			param.setTimes(1);
		}
		if(param.getTimes() >= 99999) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIMES_LIMIT.getCode(), LotteryResultEnum.BET_TIMES_LIMIT.getMsg());
		}
		String playType = param.getPlayType();
		if(StringUtils.isBlank(playType)) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
		}
		try {
			int parseInt = Integer.parseInt(playType);
			if(parseInt < 1 || parseInt > 7) {
				return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
			}
		} catch (NumberFormatException e) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
		}
		//2 1
		if(Integer.valueOf(playType).equals(MatchPlayTypeEnum.PLAY_TYPE_TSO.getcode())) {
			return ResultGenerator.genFailResult("暂不支持该玩法！", null);
		}
		//校验赛事投注时间
		MatchBetPlayDTO min = matchBetPlays.get(0);
		if(matchBetPlays.size() > 1) {
			min = matchBetPlays.stream().min((cell1,cell2)->cell1.getMatchTime()-cell2.getMatchTime()).get();
		}

		int betEndTime = lotteryMatchService.getBetEndTime(min.getMatchTime());
		Date now = new Date();
		int nowTime = Long.valueOf(now.toInstant().getEpochSecond()).intValue();
		if(nowTime - betEndTime > 0) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIME_LIMIT.getCode(), LotteryResultEnum.BET_TIME_LIMIT.getMsg());
		}
		boolean hideMatch = lotteryMatchService.isHideMatch(betEndTime, min.getMatchTime());
		if(hideMatch) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_TIME_LIMIT.getCode(), LotteryResultEnum.BET_TIME_LIMIT.getMsg());
		}

		//校验串关
		String betTypeStr = param.getBetType();
		if(StringUtils.isBlank(betTypeStr)) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getMsg());
		}
		
		boolean isCellError = false;
		boolean isAllSingle = true;
		boolean isMonyPlay = false;
		boolean mixPlayForbbidon = false;//混合投注每场比赛禁止多种选项的投注
		boolean mixPlayTurnOn = true;//混合投注每场比赛禁止多种选项的投注 开关
		Set<String> playTypeDetail = new HashSet<>();
		//混合投注每场比赛禁止多种选项的投注
		SysConfigParam sysConfigParam = new SysConfigParam();
		sysConfigParam.setBusinessId(61);
		BaseResult<SysConfigDTO> sysConfigDTOBaseResult = iSysConfigService.querySysConfig(sysConfigParam);
		if(sysConfigDTOBaseResult.getCode() == 0){
			BigDecimal value = sysConfigDTOBaseResult.getData().getValue();
			mixPlayTurnOn = value.equals(BigDecimal.ZERO)?false:true;
		}

		for(MatchBetPlayDTO betPlay : matchBetPlays){
			List<MatchBetCellDTO> matchBetCells = betPlay.getMatchBetCells();
			if(CollectionUtils.isEmpty(matchBetCells)) {
				isCellError = true;
				break;
			}
			if(matchBetCells.size()>1) {
				isMonyPlay = true;
			}

			Integer lotteryPlayClassifyId = betPlay.getLotteryPlayClassifyId();
			if(mixPlayTurnOn == true && lotteryPlayClassifyId == 6 && matchBetCells.size() > 1){//混合投注每场比赛禁止多种选项的投注
				mixPlayForbbidon = true;
			}

			for(MatchBetCellDTO betCell: matchBetCells){
				playTypeDetail.add(betCell.getPlayType());
				List<DlJcZqMatchCellDTO> betCells = betCell.getBetCells();
				if(CollectionUtils.isEmpty(betCells)) {
					isCellError = true;
					break;
				}
				for(DlJcZqMatchCellDTO dto: betCells) {
					String cellCode = dto.getCellCode();
					String cellName = dto.getCellName();
					String cellOdds = dto.getCellOdds();

					if(StringUtils.isBlank(cellCode) || StringUtils.isBlank(cellName) || StringUtils.isBlank(cellOdds)) {
						isCellError = true;
						break;
					}
				}
				Integer single = betCell.getSingle();
				if(single == null || single.equals(0)) {
					isAllSingle = false;
				}
				if(isCellError) {
					break;
				}
			}
			if(isCellError) {
				break;
			}
		}

		//混合投注每场比赛禁止多种选项的投注
		if(mixPlayForbbidon){
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_NOT_MONY.getCode(), LotteryResultEnum.BET_PLAY_NOT_MONY.getMsg());
		}

		//校验投注选项
		if(isCellError) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_HAS_NULL.getCode(), LotteryResultEnum.BET_CELL_HAS_NULL.getMsg());
		}
		if(betTypeStr.contains("11")) {
			if(!isAllSingle) {
				return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_NO_SINGLE.getCode(), LotteryResultEnum.BET_CELL_NO_SINGLE.getMsg());
			}
		}

		String[] betTypes = betTypeStr.split(",");
		boolean isCheckedBetType = true;
		int minBetNum = 9;
		try {
			int maxBetNum = 1;
			for(String betType: betTypes) {
				char[] charArray = betType.toCharArray();
				if(charArray.length == 2 && charArray[1] == '1') {
					int num = Integer.valueOf(String.valueOf(charArray[0]));
					if(num > maxBetNum) {
						maxBetNum = num;
					}
					if(minBetNum > num) {
						minBetNum = num;
					}
					if(num < 1 || num > 8) {
						isCheckedBetType = false;
					}
				}
			}
			if(maxBetNum > matchBetPlays.size()) {
				isCheckedBetType = false;
			}
		} catch (NumberFormatException e) {
		}
		if(!isCheckedBetType) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_TYPE_ENABLE.getMsg());
		}
		//校验胆的个数设置
		int danEnableNum = minBetNum -1;
		if(minBetNum == matchBetPlays.size()) {
			danEnableNum = 0;
		}
		long danNum = matchBetPlays.stream().filter(dto->dto.getIsDan() == 1).count();
		if(danNum > danEnableNum) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_DAN_ERR.getCode(), LotteryResultEnum.BET_CELL_DAN_ERR.getMsg());
		}
		//投注计算
		DLZQBetInfoDTO betInfo = lotteryMatchService.getBetInfo1(param);
		if(Double.valueOf(betInfo.getMaxLotteryMoney()) >= 20000) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MONEY_LIMIT.getCode(), LotteryResultEnum.BET_MONEY_LIMIT.getMsg());
		}
		int betNum = betInfo.getBetNum();
		if(betNum >= 10000 || betNum < 0) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_NUMBER_LIMIT.getCode(), LotteryResultEnum.BET_NUMBER_LIMIT.getMsg());
		}
		String betMoney = betInfo.getMoney();
		Double orderMoney = Double.valueOf(betMoney);
		Double minBetMoney = lotteryMatchService.getMinBetMoney();
		if(orderMoney < minBetMoney) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_WC.getCode(), "最低投注"+minBetMoney.intValue()+"元!");
		}
//		int canBetMoney = lotteryMatchService.canBetMoney();
//		if(orderMoney > canBetMoney) {
//			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(), LotteryResultEnum.BET_MATCH_STOP.getMsg());
//		}

		if(orderMoney > 200000 ){
			return ResultGenerator.genResult(LotteryResultEnum.BET_ORDER_MONEY_LIMIT.getCode(), LotteryResultEnum.BET_ORDER_MONEY_LIMIT.getMsg());
		}
		
		//缓存订单支付信息
		UserBetPayInfoDTO dto = new UserBetPayInfoDTO();
		List<UserBetDetailInfoDTO> betDetailInfos = new ArrayList<UserBetDetailInfoDTO>(matchBetPlays.size());
		for(MatchBetPlayDTO matchCell: matchBetPlays) {
			UserBetDetailInfoDTO dizqUserBetCellInfoDTO = new UserBetDetailInfoDTO();
			dizqUserBetCellInfoDTO.setMatchId(matchCell.getMatchId());
			dizqUserBetCellInfoDTO.setChangci(matchCell.getChangci());
			dizqUserBetCellInfoDTO.setIsDan(matchCell.getIsDan());
			dizqUserBetCellInfoDTO.setLotteryClassifyId(matchCell.getLotteryClassifyId());
			dizqUserBetCellInfoDTO.setLotteryPlayClassifyId(matchCell.getLotteryPlayClassifyId());
			dizqUserBetCellInfoDTO.setMatchTeam(matchCell.getMatchTeam());
			dizqUserBetCellInfoDTO.setMatchTime(matchCell.getMatchTime());
			String playCode = matchCell.getPlayCode();
			dizqUserBetCellInfoDTO.setPlayCode(playCode);
			List<MatchBetCellDTO> matchBetCells = matchCell.getMatchBetCells();
			String ticketData = matchBetCells.stream().map(betCell->{
				String ticketData1 = "0" + betCell.getPlayType() + "|" + playCode + "|";
				return ticketData1 + betCell.getBetCells().stream().map(cell->cell.getCellCode()+"@"+cell.getCellOdds())
						.collect(Collectors.joining(","));
			}).collect(Collectors.joining(";"));
			dizqUserBetCellInfoDTO.setTicketData(ticketData);;
			Optional<MatchBetCellDTO> findFirst = matchCell.getMatchBetCells().stream().filter(item->Integer.valueOf(item.getPlayType()).equals(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode())).findFirst();
			if(findFirst.isPresent()) {
				String fixOdds = findFirst.get().getFixedOdds();
				logger.info("**************************fixOdds="+fixOdds);
				dizqUserBetCellInfoDTO.setFixedodds(fixOdds);
			}
			betDetailInfos.add(dizqUserBetCellInfoDTO);
		}
		dto.setTimes(param.getTimes());
		dto.setBetType(param.getBetType());
		dto.setPlayType(param.getPlayType());
		String playTypeDetailStr = StringUtils.join(playTypeDetail.toArray(), ",");
		dto.setPlayTypeDetailStr(playTypeDetailStr);
		dto.setLotteryClassifyId(param.getLotteryClassifyId());
		dto.setLotteryPlayClassifyId(param.getLotteryPlayClassifyId());
		dto.setBetDetailInfos(betDetailInfos);
		dto.setBetNum(betNum);
		dto.setTicketNum(betInfo.getTicketNum());
		dto.setOrderMoney(orderMoney);
//		dto.setBonusAmount(bonusAmount);
//		dto.setBonusId(bonusId);
//		dto.setSurplus(surplus);
		String forecastMoney = betInfo.getMinBonus() + "~" + betInfo.getMaxBonus();
		dto.setForecastMoney(forecastMoney);
//		dto.setThirdPartyPaid(thirdPartyPaid);
		String requestFrom = "0";
		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		if(userDevice != null) {
			requestFrom = userDevice.getPlat();
		}
		dto.setRequestFrom(requestFrom);
		dto.setUserId(SessionUtil.getUserId());
		dto.setIssue(betInfo.getIssue());
		String dtoJson = JSONHelper.bean2json(dto);
		String keyStr = "bet_info_" + SessionUtil.getUserId() +"_"+ System.currentTimeMillis();
		String payToken = MD5.crypt(keyStr);
		stringRedisTemplate.opsForValue().set(payToken, dtoJson, ProjectConstant.BET_INFO_EXPIRE_TIME, TimeUnit.MINUTES);
		return ResultGenerator.genSuccessResult("success", payToken);
	}
	
	@ApiOperation(value = "模拟生成订单", notes = "模拟生成订单")
	@PostMapping("/createOrderBySimulate")
	public BaseResult<OrderIdDTO> createOrderBySimulate(@Valid @RequestBody DlJcZqMatchBetParam param){
		//开关是否关闭
		SysConfigParam sysCfgParams = new SysConfigParam();
		sysCfgParams.setBusinessId(1);
		BaseResult<SysConfigDTO> baseR = iSysConfigService.querySysConfig(sysCfgParams);
		if(baseR != null && baseR.isSuccess()) {
			SysConfigDTO sysCfgDTO = baseR.getData();
			if(sysCfgDTO.getValue().intValue() == 1) {//足彩是否停售
				return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(),LotteryResultEnum.BET_MATCH_STOP.getMsg());
			}
		}
		BaseResult<String> rst = this.nSaveBetInfo(param);
		if(rst.getCode()!=0) {
			return ResultGenerator.genResult(rst.getCode(), rst.getMsg());
		}
		
		String payToken = rst.getData();
		if (StringUtils.isBlank(payToken)) {
			logger.info("payToken值为空！");
			return ResultGenerator.genResult(PayEnums.PAY_TOKEN_EMPTY.getcode(), PayEnums.PAY_TOKEN_EMPTY.getMsg());
		}
		// 校验payToken的有效性
		String jsonData = stringRedisTemplate.opsForValue().get(payToken);
		if (StringUtils.isBlank(jsonData)) {
			logger.info( "支付信息获取为空！");
			return ResultGenerator.genResult(PayEnums.PAY_TOKEN_EXPRIED.getcode(), PayEnums.PAY_TOKEN_EXPRIED.getMsg());
		}
		// 清除payToken
		stringRedisTemplate.delete(payToken);

		UserBetPayInfoDTO dto = null;
		try {
			dto = JSONHelper.getSingleBean(jsonData, UserBetPayInfoDTO.class);
		} catch (Exception e1) {
			logger.error("支付信息转DIZQUserBetInfoDTO对象失败！", e1);
			return ResultGenerator.genFailResult("模拟支付信息异常，模拟支付失败！");
		}
		if (null == dto) {
			return ResultGenerator.genFailResult("模拟支付信息异常，模拟支付失败！");
		}

		Integer userId = dto.getUserId();
		Integer currentId = SessionUtil.getUserId();
		logger.info("[createOrderBySimulate]" + " userId:" + userId + " curUserId:" + currentId);
		if (!userId.equals(currentId)) {
			logger.info("支付信息不是当前用户的待支付彩票！");
			return ResultGenerator.genFailResult("模拟支付信息异常，支付失败！");
		}
		Double orderMoney = dto.getOrderMoney();
		Integer userBonusId = StringUtils.isBlank(dto.getBonusId()) ? 0 : Integer.valueOf(dto.getBonusId());// form paytoken
		BigDecimal ticketAmount = BigDecimal.valueOf(orderMoney);// from paytoken
		BigDecimal bonusAmount = BigDecimal.ZERO;//BigDecimal.valueOf(dto.getBonusAmount());// from  paytoken
		BigDecimal moneyPaid = BigDecimal.valueOf(orderMoney);// from paytoken
		BigDecimal surplus =  BigDecimal.ZERO;//BigDecimal.valueOf(dto.getSurplus());// from paytoken
		BigDecimal thirdPartyPaid =  BigDecimal.ZERO;//BigDecimal.valueOf(dto.getThirdPartyPaid());
		List<UserBetDetailInfoDTO> userBetCellInfos = dto.getBetDetailInfos();
		List<TicketDetail> ticketDetails = userBetCellInfos.stream().map(betCell -> {
			TicketDetail ticketDetail = new TicketDetail();
			ticketDetail.setMatch_id(betCell.getMatchId());
			ticketDetail.setChangci(betCell.getChangci());
			int matchTime = betCell.getMatchTime();
			if (matchTime > 0) {
				ticketDetail.setMatchTime(Date.from(Instant.ofEpochSecond(matchTime)));
			}
			ticketDetail.setMatchTeam(betCell.getMatchTeam());
			ticketDetail.setLotteryClassifyId(betCell.getLotteryClassifyId());
			ticketDetail.setLotteryPlayClassifyId(betCell.getLotteryPlayClassifyId());
			ticketDetail.setTicketData(betCell.getTicketData());
			ticketDetail.setIsDan(betCell.getIsDan());
			ticketDetail.setIssue(betCell.getPlayCode());
			ticketDetail.setFixedodds(betCell.getFixedodds());
			ticketDetail.setBetType(betCell.getBetType());
			return ticketDetail;
		}).collect(Collectors.toList());

		// order生成
		SubmitOrderParam submitOrderParam = new SubmitOrderParam();
		submitOrderParam.setTicketNum(dto.getTicketNum());
		submitOrderParam.setMoneyPaid(moneyPaid);
		submitOrderParam.setTicketAmount(ticketAmount);
		submitOrderParam.setSurplus(surplus);
		submitOrderParam.setThirdPartyPaid(thirdPartyPaid);
		submitOrderParam.setPayName("");
		submitOrderParam.setUserBonusId(userBonusId);
		submitOrderParam.setBonusAmount(bonusAmount);
		submitOrderParam.setOrderFrom(dto.getRequestFrom());
		int lotteryClassifyId = dto.getLotteryClassifyId();
		submitOrderParam.setLotteryClassifyId(lotteryClassifyId);
		int lotteryPlayClassifyId = dto.getLotteryPlayClassifyId();
		submitOrderParam.setLotteryPlayClassifyId(lotteryPlayClassifyId);
		submitOrderParam.setPassType(dto.getBetType());
		submitOrderParam.setPlayType("0" + dto.getPlayType());
		submitOrderParam.setBetNum(dto.getBetNum());
		submitOrderParam.setCathectic(dto.getTimes());
		if (null!= param.getStoreId()) {
			submitOrderParam.setStoreId(Integer.parseInt(param.getStoreId()));
		}
		if (lotteryPlayClassifyId != 8 && lotteryClassifyId == 1) {
			if (ticketDetails.size() > 1) {
				Optional<TicketDetail> max = ticketDetails.stream().max((detail1, detail2) -> detail1.getMatchTime().compareTo(detail2.getMatchTime()));
				submitOrderParam.setMatchTime(max.get().getMatchTime());
			} else {
				submitOrderParam.setMatchTime(ticketDetails.get(0).getMatchTime());
			}
		}
		submitOrderParam.setForecastMoney(dto.getForecastMoney());
		submitOrderParam.setIssue(dto.getIssue());
		submitOrderParam.setTicketDetails(ticketDetails);
		logger.info("订单提交信息==========="+submitOrderParam);
		BaseResult<OrderDTO> createOrder = orderService.createOrder(submitOrderParam);
		if (createOrder.getCode() != 0) {
			logger.info("订单创建失败！");
			return ResultGenerator.genFailResult("模拟支付失败！");
		}
		String orderId = createOrder.getData().getOrderId().toString();
		String orderSn = createOrder.getData().getOrderSn();
		OrderIdDTO orderDto = new OrderIdDTO();
		orderDto.setOrderId(orderId);
		orderDto.setOrderSn(orderSn);
		return ResultGenerator.genSuccessResult("success", orderDto);
	}
	
	
//	@Transactional
	@ApiOperation(value = "模拟生成订单", notes = "模拟生成订单")
	@PostMapping("/createOrder")
	public synchronized BaseResult<OrderIdDTO> createOrder(@Valid @RequestBody DlJcZqMatchBetParam2 param, HttpServletRequest req){
		// 检验签名
		/**
		使用MD5进行签名,签名对象为merchant+merchantPassword+json
		json为请求信息中的json字符串,merchant为代理商编号，merchantPassword为代理商秘钥
		签名之前需将签名数据以UTF-8编码方式编码
		在Http请求中增加Authorization的Header来包含签名信息
		 */
		String ss = req.getHeader("Authorization")+"";
		logger.info("请求头签名："+ss.toUpperCase());
		boolean authFlag = true;
		if(!StringUtils.isEmpty(param.getMerchantOrderSn())) {//如果MerchantOrderSn不等于空  则为商户订单
			UserIdParam up = new UserIdParam();
			up.setUserId(1000000000);	//1000000000  久幺
			UserDTO user = iUserService.queryUserInfo(up)!=null?iUserService.queryUserInfo(up).getData():null;
			if(user!=null) {
//				String strjson = JSONHelper.bean2json(param);
				String strSign = user.getMerchantNo()+user.getMerchantPass()+param.getTimestamp()+param.getMerchantOrderSn();
				String sign = MD5.getSign(strSign);
				logger.info("createOrder(this)签名前="+strSign+"************签名后="+sign);
				if(!ss.equalsIgnoreCase(sign)) { //签名不一致
					authFlag = false;
				}
			}
		}
		if (!authFlag) {
			return ResultGenerator.genFailResult("签名不合法");
		}
		
		//==============================
		Integer userId = null;
//		userId = dto.getUserId();   
		userId = 1000000000;
//		Integer userIdNew = SessionUtil.getUserId();
		BaseContextHandler.setUserID(userId.toString());
		//=====================================
		
		//开关是否关闭
		SysConfigParam sysCfgParams = new SysConfigParam();
		sysCfgParams.setBusinessId(1);
		BaseResult<SysConfigDTO> baseR = iSysConfigService.querySysConfig(sysCfgParams);
		if(baseR != null && baseR.isSuccess()) {
			SysConfigDTO sysCfgDTO = baseR.getData();
			if(sysCfgDTO.getValue().intValue() == 1) {//足彩是否停售
				return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(),LotteryResultEnum.BET_MATCH_STOP.getMsg());
			}
		}
		BaseResult<String> rst = this.nSaveBetInfo(param);
		if(rst.getCode()!=0) {
			return ResultGenerator.genResult(rst.getCode(), rst.getMsg());
		}
		
		String payToken = rst.getData();
		if (StringUtils.isBlank(payToken)) {
			logger.info("payToken值为空！");
			return ResultGenerator.genResult(PayEnums.PAY_TOKEN_EMPTY.getcode(), PayEnums.PAY_TOKEN_EMPTY.getMsg());
		}
		// 校验payToken的有效性
		String jsonData = stringRedisTemplate.opsForValue().get(payToken);
		if (StringUtils.isBlank(jsonData)) {
			logger.info( "支付信息获取为空！");
			return ResultGenerator.genResult(PayEnums.PAY_TOKEN_EXPRIED.getcode(), PayEnums.PAY_TOKEN_EXPRIED.getMsg());
		}
		// 清除payToken
		stringRedisTemplate.delete(payToken);

		UserBetPayInfoDTO dto = null;
		try {
			dto = JSONHelper.getSingleBean(jsonData, UserBetPayInfoDTO.class);
		} catch (Exception e1) {
			logger.error("支付信息转DIZQUserBetInfoDTO对象失败！", e1);
			return ResultGenerator.genFailResult("模拟支付信息异常，模拟支付失败！");
		}
		if (null == dto) {
			return ResultGenerator.genFailResult("模拟支付信息异常，模拟支付失败！");
		}

		userId = dto.getUserId();
		Integer currentId = SessionUtil.getUserId();
		logger.info("[createOrderBySimulate]" + " userId:" + userId + " curUserId:" + currentId);
		if (!userId.equals(currentId)) {
			logger.info("支付信息不是当前用户的待支付彩票！");
			return ResultGenerator.genFailResult("模拟支付信息异常，支付失败！");
		}
		Double orderMoney = dto.getOrderMoney();
		Integer userBonusId = StringUtils.isBlank(dto.getBonusId()) ? 0 : Integer.valueOf(dto.getBonusId());// form paytoken
		BigDecimal ticketAmount = BigDecimal.valueOf(orderMoney);// from paytoken
		BigDecimal bonusAmount = BigDecimal.ZERO;//BigDecimal.valueOf(dto.getBonusAmount());// from  paytoken
		BigDecimal moneyPaid = BigDecimal.valueOf(orderMoney);// from paytoken
		BigDecimal surplus =  BigDecimal.ZERO;//BigDecimal.valueOf(dto.getSurplus());// from paytoken
		BigDecimal thirdPartyPaid =  BigDecimal.ZERO;//BigDecimal.valueOf(dto.getThirdPartyPaid());
		List<UserBetDetailInfoDTO> userBetCellInfos = dto.getBetDetailInfos();
		List<TicketDetail> ticketDetails = userBetCellInfos.stream().map(betCell -> {
			TicketDetail ticketDetail = new TicketDetail();
			ticketDetail.setMatch_id(betCell.getMatchId());
			ticketDetail.setChangci(betCell.getChangci());
			int matchTime = betCell.getMatchTime();
			if (matchTime > 0) {
				ticketDetail.setMatchTime(Date.from(Instant.ofEpochSecond(matchTime)));
			}
			ticketDetail.setMatchTeam(betCell.getMatchTeam());
			ticketDetail.setLotteryClassifyId(betCell.getLotteryClassifyId());
			ticketDetail.setLotteryPlayClassifyId(betCell.getLotteryPlayClassifyId());
			ticketDetail.setTicketData(betCell.getTicketData());
			ticketDetail.setIsDan(betCell.getIsDan());
			ticketDetail.setIssue(betCell.getPlayCode());
			ticketDetail.setFixedodds(betCell.getFixedodds());
			ticketDetail.setBetType(betCell.getBetType());
			return ticketDetail;
		}).collect(Collectors.toList());
		
//		检查商户余额是否充足
		UserIdRealParam userIdParam = new UserIdRealParam();
		userIdParam.setUserId(userId);
		BaseResult<UserDTO> userDTO = iUserService.queryUserInfoReal(userIdParam);
		if(userDTO == null) {
			return ResultGenerator.genFailResult("该用户不存在");
		}
		String totalStr = userDTO.getData()
//				.getTotalMoney()
				.getUserMoneyLimit()
				;
		String mobile = userDTO.getData().getMobile();
		String passWord = userDTO.getData().getPassword();
		
		logger.info("[checkMerchantAccount]" +" userId:"  + userId +" totalAmt:" +totalStr);
		BigDecimal userMoneyLimit = new BigDecimal(userDTO.getData().getUserMoneyLimit());
		if(userMoneyLimit.subtract(ticketAmount).doubleValue() >= 0) {
//			return ResultGenerator.genSuccessResult();
		}else {
			return ResultGenerator.genFailResult("商户余额不足");
		}
		
		//扣钱
		BigDecimal _userMoneyLimit = userMoneyLimit.subtract(ticketAmount);
		UserParam _user = new UserParam();
		_user.setUserId(userId + "");
		_user.setUserMoneyLimit(_userMoneyLimit);
		_user.setMobile(mobile);
		_user.setPassWord(passWord);
		BaseResult<Integer> flag1 = this.iUserAccountService.updateUserMoneyAndUserMoneyLimit(_user);
		
		// 生成订单号
		String orderSn = SNGenerator.nextSN(SNBusinessCodeEnum.ORDER_SN.getCode());
		
		//记流水
		UserAccountParam userAccountParam = new UserAccountParam();
		String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
		userAccountParam.setAccountSn(accountSn);
		userAccountParam.setUserId(userId);
//		userAccountParam.setAdminUser(null);		
		userAccountParam.setAmount(ticketAmount);
		userAccountParam.setCurBalance(_userMoneyLimit);
		Integer currentTimeLong = DateUtil.getCurrentTimeLong();
		userAccountParam.setAddTime(currentTimeLong);
		userAccountParam.setLastTime(currentTimeLong);
		String note = "商户支付" + ticketAmount + "元";
		userAccountParam.setNote(note);
		userAccountParam.setProcessType(Integer.valueOf(3));
		userAccountParam.setOrderSn(orderSn);
//		userAccountParam.setParentSn("");
		userAccountParam.setPayId("");
		userAccountParam.setPaymentName("2");
		userAccountParam.setThirdPartName("");
		userAccountParam.setUserSurplusLimit(new BigDecimal(0.00));
		userAccountParam.setBonusPrice(null);
		userAccountParam.setStatus(1);
		BaseResult<Integer> flag2 = iUserAccountService.insertUserAccountBySelective(userAccountParam);
		
		// order生成
		SubmitOrderParam submitOrderParam = new SubmitOrderParam();
		submitOrderParam.set_orderSn(orderSn);
		submitOrderParam.set_userId(userId + "");
//		order_status 
//		print_lottery_status 
//		print_lottery_refund_amount
//		pay_status 
//		pay_status 
//		pay_id
//		pay_code 
//		pay_name 
//		pay_sn
//		money_paid
		submitOrderParam.setMoneyPaid(moneyPaid);
		submitOrderParam.setTicketAmount(ticketAmount);
		submitOrderParam.setSurplus(surplus);
//		user_surplus 
//		user_surplus_limit   ///
		submitOrderParam.setTicketNum(dto.getTicketNum());
		submitOrderParam.setThirdPartyPaid(thirdPartyPaid);
		submitOrderParam.setPayName("");
		submitOrderParam.setUserBonusId(userBonusId);
		submitOrderParam.setBonusAmount(bonusAmount);
		submitOrderParam.setOrderFrom(dto.getRequestFrom());
		int lotteryClassifyId = dto.getLotteryClassifyId();
		submitOrderParam.setLotteryClassifyId(lotteryClassifyId);
		int lotteryPlayClassifyId = dto.getLotteryPlayClassifyId();
		submitOrderParam.setLotteryPlayClassifyId(lotteryPlayClassifyId);
		submitOrderParam.setPassType(dto.getBetType());
//		submitOrderParam.setPlayType("0" + dto.getPlayType());
		submitOrderParam.setPlayType("1");
		submitOrderParam.setBetNum(dto.getBetNum());
		submitOrderParam.setCathectic(dto.getTimes());
		if (null!= param.getStoreId()) {
			submitOrderParam.setStoreId(Integer.parseInt(param.getStoreId()));
		}
		if (lotteryPlayClassifyId != 8 && lotteryClassifyId == 1) {
			if (ticketDetails.size() > 1) {
				Optional<TicketDetail> max = ticketDetails.stream().max((detail1, detail2) -> detail1.getMatchTime().compareTo(detail2.getMatchTime()));
				submitOrderParam.setMatchTime(max.get().getMatchTime());
			} else {
				submitOrderParam.setMatchTime(ticketDetails.get(0).getMatchTime());
			}
		}
		submitOrderParam.setForecastMoney(dto.getForecastMoney());
		submitOrderParam.setIssue(dto.getIssue());
		submitOrderParam.setTicketDetails(ticketDetails);
		submitOrderParam.setMerchantNo(param.getMerchant());
		submitOrderParam.setMerchantOrderSn(param.getMerchantOrderSn());
		
		logger.info("订单提交信息==========="+submitOrderParam);
		BaseResult<OrderDTO> createOrder = orderService.createOrder(submitOrderParam);
		if (createOrder.getCode() != 0) {
			logger.info("订单创建失败！");
			return ResultGenerator.genFailResult("模拟支付失败！");
		}
		
		
		if(!StringUtils.isEmpty(param.getMerchantOrderSn())) {//如果MerchantOrderSn不等于空  则为商户订单
			UpdateOrderInfoParam updateOrderInfoParam = new UpdateOrderInfoParam();
			updateOrderInfoParam.setOrderSn(orderSn);
			updateOrderInfoParam.setOrderStatus(3);
			updateOrderInfoParam.setPayStatus(1);
			updateOrderInfoParam.setPayTime(DateUtil.getCurrentTimeLong());
			orderService.updateOrderInfoStatus(updateOrderInfoParam);///修改为支付状态
		}
		
		String orderId = createOrder.getData().getOrderId().toString();
		OrderIdDTO orderDto = new OrderIdDTO();
		orderDto.setOrderId(orderId);
		orderDto.setOrderSn(orderSn);
		return ResultGenerator.genSuccessResult("success", orderDto);
	}
	
//	@ApiOperation(value = "模拟商户下单", notes = "模拟商户下单")
//	@PostMapping("/createOrder1")
//	public BaseResult<OrderIdDTO> createOrder1(@Valid @RequestBody DlJcZqMatchBetParam2 param,@Valid @RequestBody HttpServletRequest request){
//		
//		// 检验签名
//		BaseResult<?> authResult = merchantService.authSignInfo(param,request);
//		if(!authResult.isSuccess()) {
//			return ResultGenerator.genFailResult(authResult.getMsg());
//		}
//		//==============================
//		Integer userId = null;
////		userId = dto.getUserId();   
//		userId = 444912;
//		AuthInfoDTO authInfoDTO = new AuthInfoDTO();
//		authInfoDTO.setUserId(userId);
//		authInfoDTO.setLoginType(1);
//		BaseResult<String> result = authService.genToken(authInfoDTO);
//		if (!result.isSuccess()) {
//		    throw new ServiceException(result);
//		}
////		Integer userIdNew = SessionUtil.getUserId();
//		BaseContextHandler.setUserID(userId.toString());
//		//=====================================
//		
//		//检查赛事信息
//		BaseResult<?> betInfoResult = merchantService.checkBetInfo();
//		if(!betInfoResult.isSuccess()) {
//			return ResultGenerator.genFailResult(betInfoResult.getMsg());
//		}
//		//获取订单金额
//		BaseResult<DLZQBetInfoDTO> resultBetInfo = merchantService.calculateBetAmt(null);
//		if(!resultBetInfo.isSuccess()) {
//			return ResultGenerator.genFailResult(resultBetInfo.getMsg());
//		}
//		BigDecimal orderAmt = new BigDecimal(resultBetInfo.getData().getMoney());
//		// 判断商户余额信息
//		BaseResult<?> checkMerchantAcc = merchantService.checkMerchantAccount(userId,orderAmt);
//		if(!checkMerchantAcc.isSuccess()) {
//			return ResultGenerator.genFailResult(checkMerchantAcc.getMsg());
//		}
//		//扣除余额 记录流水  
//		
//		
//		
//
//		//开关是否关闭
//		SysConfigParam sysCfgParams = new SysConfigParam();
//		sysCfgParams.setBusinessId(1);
//		BaseResult<SysConfigDTO> baseR = iSysConfigService.querySysConfig(sysCfgParams);
//		if(baseR != null && baseR.isSuccess()) {
//			SysConfigDTO sysCfgDTO = baseR.getData();
//			if(sysCfgDTO.getValue().intValue() == 1) {//足彩是否停售
//				return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(),LotteryResultEnum.BET_MATCH_STOP.getMsg());
//			}
//		}
//		BaseResult<String> rst = this.nSaveBetInfo(param);
//		if(rst.getCode()!=0) {
//			return ResultGenerator.genResult(rst.getCode(), rst.getMsg());
//		}
//		
//		String payToken = rst.getData();
//		if (StringUtils.isBlank(payToken)) {
//			logger.info("payToken值为空！");
//			return ResultGenerator.genResult(PayEnums.PAY_TOKEN_EMPTY.getcode(), PayEnums.PAY_TOKEN_EMPTY.getMsg());
//		}
//		// 校验payToken的有效性
//		String jsonData = stringRedisTemplate.opsForValue().get(payToken);
//		if (StringUtils.isBlank(jsonData)) {
//			logger.info( "支付信息获取为空！");
//			return ResultGenerator.genResult(PayEnums.PAY_TOKEN_EXPRIED.getcode(), PayEnums.PAY_TOKEN_EXPRIED.getMsg());
//		}
//		// 清除payToken
//		stringRedisTemplate.delete(payToken);
//
//		UserBetPayInfoDTO dto = null;
//		try {
//			dto = JSONHelper.getSingleBean(jsonData, UserBetPayInfoDTO.class);
//		} catch (Exception e1) {
//			logger.error("支付信息转DIZQUserBetInfoDTO对象失败！", e1);
//			return ResultGenerator.genFailResult("模拟支付信息异常，模拟支付失败！");
//		}
//		if (null == dto) {
//			return ResultGenerator.genFailResult("模拟支付信息异常，模拟支付失败！");
//		}
//
//		//// ******************************
//////		Integer userId = null;
//////		userId = dto.getUserId();   
//////		userId = 444912;
//////		dto.setUserId(userId);	
////        AuthInfoDTO authInfoDTO = new AuthInfoDTO();
////        authInfoDTO.setUserId(userId);
////        authInfoDTO.setLoginType(0);
////        BaseResult<String> result = authService.genToken(authInfoDTO);
////        if (!result.isSuccess()) {
////            throw new ServiceException(result);
////        }
//////        BaseContextHandler.setUserID(userId.toString());
//        
//		Integer currentId = SessionUtil.getUserId();
//		logger.info("[createOrderBySimulate]" + " userId:" + userId + " curUserId:" + currentId);
//		if (!userId.equals(currentId)) {
//			logger.info("支付信息不是当前用户的待支付彩票！");
//			return ResultGenerator.genFailResult("模拟支付信息异常，支付失败！");
//		}
//		
//		Double orderMoney = dto.getOrderMoney();
//		Integer userBonusId = StringUtils.isBlank(dto.getBonusId()) ? 0 : Integer.valueOf(dto.getBonusId());// form paytoken
//		BigDecimal ticketAmount = BigDecimal.valueOf(orderMoney);// from paytoken
//		BigDecimal bonusAmount = BigDecimal.ZERO;//BigDecimal.valueOf(dto.getBonusAmount());// from  paytoken
//		BigDecimal moneyPaid = BigDecimal.valueOf(orderMoney);// from paytoken
//		BigDecimal surplus =  BigDecimal.ZERO;//BigDecimal.valueOf(dto.getSurplus());// from paytoken
//		BigDecimal thirdPartyPaid =  BigDecimal.ZERO;//BigDecimal.valueOf(dto.getThirdPartyPaid());
//		List<UserBetDetailInfoDTO> userBetCellInfos = dto.getBetDetailInfos();
//		List<TicketDetail> ticketDetails = userBetCellInfos.stream().map(betCell -> {
//			TicketDetail ticketDetail = new TicketDetail();
//			ticketDetail.setMatch_id(betCell.getMatchId());
//			ticketDetail.setChangci(betCell.getChangci());
//			int matchTime = betCell.getMatchTime();
//			if (matchTime > 0) {
//				ticketDetail.setMatchTime(Date.from(Instant.ofEpochSecond(matchTime)));
//			}
//			ticketDetail.setMatchTeam(betCell.getMatchTeam());
//			ticketDetail.setLotteryClassifyId(betCell.getLotteryClassifyId());
//			ticketDetail.setLotteryPlayClassifyId(betCell.getLotteryPlayClassifyId());
//			ticketDetail.setTicketData(betCell.getTicketData());
//			ticketDetail.setIsDan(betCell.getIsDan());
//			ticketDetail.setIssue(betCell.getPlayCode());
//			ticketDetail.setFixedodds(betCell.getFixedodds());
//			ticketDetail.setBetType(betCell.getBetType());
//			return ticketDetail;
//		}).collect(Collectors.toList());
//
//		// order生成
//		SubmitOrderParam submitOrderParam = new SubmitOrderParam();
//		submitOrderParam.setTicketNum(dto.getTicketNum());
//		submitOrderParam.setMoneyPaid(moneyPaid);
//		submitOrderParam.setTicketAmount(ticketAmount);
//		submitOrderParam.setSurplus(surplus);
//		submitOrderParam.setThirdPartyPaid(thirdPartyPaid);
//		submitOrderParam.setPayName("");
//		submitOrderParam.setUserBonusId(userBonusId);
//		submitOrderParam.setBonusAmount(bonusAmount);
//		submitOrderParam.setOrderFrom(dto.getRequestFrom());
//		int lotteryClassifyId = dto.getLotteryClassifyId();
//		submitOrderParam.setLotteryClassifyId(lotteryClassifyId);
//		int lotteryPlayClassifyId = dto.getLotteryPlayClassifyId();
//		submitOrderParam.setLotteryPlayClassifyId(lotteryPlayClassifyId);
//		submitOrderParam.setPassType(dto.getBetType());
//		submitOrderParam.setPlayType("0" + dto.getPlayType());
//		submitOrderParam.setBetNum(dto.getBetNum());
//		submitOrderParam.setCathectic(dto.getTimes());
//		if (null!= param.getStoreId()) {
//			submitOrderParam.setStoreId(Integer.parseInt(param.getStoreId()));
//		}
//		if (lotteryPlayClassifyId != 8 && lotteryClassifyId == 1) {
//			if (ticketDetails.size() > 1) {
//				Optional<TicketDetail> max = ticketDetails.stream().max((detail1, detail2) -> detail1.getMatchTime().compareTo(detail2.getMatchTime()));
//				submitOrderParam.setMatchTime(max.get().getMatchTime());
//			} else {
//				submitOrderParam.setMatchTime(ticketDetails.get(0).getMatchTime());
//			}
//		}
//		submitOrderParam.setForecastMoney(dto.getForecastMoney());
//		submitOrderParam.setIssue(dto.getIssue());
//		submitOrderParam.setTicketDetails(ticketDetails);
//		
//		if (!StringUtil.isBlank(param.getMerchant())) {
//			submitOrderParam.setMerchantNo(param.getMerchant());
//		}
//		if (!StringUtil.isBlank(param.getMerchantOrderSn())) {
//			submitOrderParam.setMerchantOrderSn(param.getMerchantOrderSn());
//		}
//		
//		logger.info("订单提交信息==========="+submitOrderParam);
//		submitOrderParam.set_userId(userId + "");
//		
//		BaseResult<OrderDTO> createOrder = orderService.createOrder(submitOrderParam);
//		if (createOrder.getCode() != 0) {
//			logger.info("订单创建失败！");
//			return ResultGenerator.genFailResult("模拟支付失败！");
//		}
//		String orderId = createOrder.getData().getOrderId().toString();
//		String orderSn = createOrder.getData().getOrderSn();
//		OrderIdDTO orderDto = new OrderIdDTO();
//		orderDto.setOrderId(orderId);
//		orderDto.setOrderSn(orderSn);
//		
////		// 扣费
////		
////		// 流水
//		
//		return ResultGenerator.genSuccessResult("success", orderDto);
//	}
    
	@ApiOperation(value = "抓取赛事列表保存", notes = "抓取赛事列表保存")
    @PostMapping("/saveMatchList")
    public BaseResult<String> saveMatchList() {
		lotteryMatchService.saveMatchList();
    	return ResultGenerator.genSuccessResult("抓取赛事列表保存成功");
    }
	
	@ApiOperation(value = "根据条件查询比赛结果", notes = "根据条件查询比赛结果")
    @PostMapping("/queryMatchResult")
    public BaseResult<List<LotteryMatchDTO>> queryMatchResult(@RequestBody QueryMatchParam dateStrParam) {
    	return lotteryMatchService.queryMatchResult(dateStrParam);
    }
	
	@ApiOperation(value = "后台提供的比赛比分的20天的日期筛选条件（:2018-07-06 新接口", notes = "根据条件查询比赛结果新:2018-07-06 新接口")
    @PostMapping("/giveMatchChooseDate")
    public BaseResult<List<MatchDateDTO>> giveMatchChooseDate(@RequestBody EmptyParam emptyParam) {
		List<MatchDateDTO> list = lotteryMatchService.giveMatchChooseDate();
		return ResultGenerator.genSuccessResult("success", list);
    }

	@ApiOperation(value = "根据条件查询比赛比分新:2018-07-06 新接口", notes = "根据条件查询比赛结果新:2018-07-06 新接口")
    @PostMapping("/queryMatchResultNew")
    public BaseResult<QueryMatchResultDTO> queryMatchResultNew(@RequestBody QueryMatchParamByType dateStrParamByType) {
		UserDeviceInfo uinfo = SessionUtil.getUserDevice();
		if(uinfo!=null && "11".equals(uinfo.getAppCodeName())) {
			return lotteryMatchService.queryMatchResultNewQdd(dateStrParamByType);
		}
    	return lotteryMatchService.queryMatchResultNew(dateStrParamByType);
    }
	
	@ApiOperation(value = "查询大乐透比赛结果", notes = "查询大乐透比赛结果")
    @PostMapping("/getBetInfoByLotto")
    public BaseResult<List<DLBetLottoInfoDTO>> getBetInfoByLotto(@RequestBody GetBetInfoByOrderSn param) {
		if(StringUtils.isBlank(param.getOrderSn())) {
			return ResultGenerator.genFailResult();
		}
		List<DLBetLottoInfoDTO> dtoList = lotteryMatchService.getBetInfoByLottoInfo( param.getOrderSn());
    	return ResultGenerator.genSuccessResult("success",dtoList);
    }
	
	@ApiOperation(value = "查询比赛结果", notes = "查询比赛结果")
    @PostMapping("/getBetInfoByOrderSn")
    public BaseResult<DLZQBetInfoDTO> getBetInfoByOrderSn(@RequestBody GetBetInfoByOrderSn param) {
		if(StringUtils.isBlank(param.getOrderSn())) {
			return ResultGenerator.genFailResult();
		}
		/*OrderSnParam orderSnParam = new OrderSnParam();
		orderSnParam.setOrderSn(param.getOrderSn());
		BaseResult<OrderInfoAndDetailDTO> orderWithDetailByOrderSn = orderService.getOrderWithDetailByOrderSn(orderSnParam);
		if(orderWithDetailByOrderSn.getCode() != 0) {
			return ResultGenerator.genFailResult();
		}
		DLZQBetInfoDTO dto = lotteryMatchService.getBetInfoByOrderInfo(orderWithDetailByOrderSn.getData(), param.getOrderSn());*/
		DLZQBetInfoDTO dto = lotteryMatchService.getBetInfoByOrderInfo1( param.getOrderSn());
    	return ResultGenerator.genSuccessResult("success",dto);
    }
	
	@ApiOperation(value = "查询篮球比赛结果", notes = "查询篮球比赛结果")
    @PostMapping("/getBasketBallBetInfoByOrderSn")
    public BaseResult<DLLQBetInfoDTO> getBasketBetInfoByOrderSn(@RequestBody GetBetInfoByOrderSn param) {
		if(StringUtils.isBlank(param.getOrderSn())) {
			return ResultGenerator.genFailResult();
		}
		/*OrderSnParam orderSnParam = new OrderSnParam();
		orderSnParam.setOrderSn(param.getOrderSn());
		BaseResult<OrderInfoAndDetailDTO> orderWithDetailByOrderSn = orderService.getOrderWithDetailByOrderSn(orderSnParam);
		if(orderWithDetailByOrderSn.getCode() != 0) {
			return ResultGenerator.genFailResult();
		}
		DLZQBetInfoDTO dto = lotteryMatchService.getBetInfoByOrderInfo(orderWithDetailByOrderSn.getData(), param.getOrderSn());*/
		DLLQBetInfoDTO dto = lotteryMatchService.getBasketBetInfoByOrderInfo1( param.getOrderSn());
    	return ResultGenerator.genSuccessResult("success",dto);
    }
	
	@ApiOperation(value = "球队分析信息", notes = "球队分析信息")
    @PostMapping("/matchTeamInfosSum")
    public BaseResult<MatchTeamInfosSumDTO> matchTeamInfosSum(@Valid @RequestBody MatchTeamInfosParam param) {
		LotteryMatch lotteryMatch = lotteryMatchService.findById(param.getMatchId());
		if(null == lotteryMatch) {
			return ResultGenerator.genFailResult("数据读取失败！", null);
		}
		
		MatchTeamInfosDTO matchTeamInfo = lotteryMatchService.matchTeamInfos(lotteryMatch);
		MatchInfoForTeamDTO lotteryMatchForTeam = lotteryMatchService.LotteryMatchForTeam(lotteryMatch);
		TeamSupportDTO hadTeamSupport = dlMatchSupportService.matchSupports(lotteryMatch, MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode());
		TeamSupportDTO hhadTeamSupport = dlMatchSupportService.matchSupports(lotteryMatch, MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode());
		String fixedOdds = lotteryMatchPlayService.fixedOddsByChangciId(lotteryMatch.getChangciId());
		hhadTeamSupport.setFixedOdds(fixedOdds);
		MatchTeamInfosSumDTO dto = new MatchTeamInfosSumDTO();
		dto.setHhMatchTeamInfo(matchTeamInfo.getHhMatchTeamInfo());
		dto.setHMatchTeamInfo(matchTeamInfo.getHMatchTeamInfo());
		dto.setHvMatchTeamInfo(matchTeamInfo.getHvMatchTeamInfo());
		dto.setMatchInfo(lotteryMatchForTeam);
		dto.setVMatchTeamInfo(matchTeamInfo.getVMatchTeamInfo());
		dto.setVvMatchTeamInfo(matchTeamInfo.getVvMatchTeamInfo());
		dto.setHadTeamSupport(hadTeamSupport);
		dto.setHhadTeamSupport(hhadTeamSupport);
		return ResultGenerator.genSuccessResult("success", dto);
    }
	@ApiOperation(value = "球队分析详情信息", notes = "球队分析详情信息")
	@PostMapping("/matchTeamInfos")
	public BaseResult<MatchTeamInfosDTO> matchTeamInfos(@Valid @RequestBody MatchTeamInfosParam param) {
		LotteryMatch lotteryMatch = lotteryMatchService.findById(param.getMatchId());
		if(null == lotteryMatch) {
			return ResultGenerator.genFailResult("数据读取失败！", null);
		}
		MatchInfoForTeamDTO lotteryMatchForTeam = lotteryMatchService.LotteryMatchForTeam(lotteryMatch);
		MatchTeamInfosDTO matchTeamInfo = lotteryMatchService.matchTeamInfos(lotteryMatch);
		matchTeamInfo.setMatchInfo(lotteryMatchForTeam);
		List<LeagueMatchAsiaDTO> leagueMatchAsias = dlLeagueMatchAsiaService.leagueMatchAsias(lotteryMatch.getChangciId());
		matchTeamInfo.setLeagueMatchAsias(leagueMatchAsias);
		List<LeagueMatchEuropeDTO> leagueMatchEuropes = dlLeagueMatchEuropeService.leagueMatchEuropes(lotteryMatch.getChangciId());
		matchTeamInfo.setLeagueMatchEuropes(leagueMatchEuropes);
		List<LeagueMatchDaoXiaoDTO> leagueMatchDaoXiaos = dlLeagueMatchDaoXiaoService.leagueMatchDaoXiaos(lotteryMatch.getChangciId());
		matchTeamInfo.setLeagueMatchDaoxiaos(leagueMatchDaoXiaos);
		
		Integer h_teamId = this.queryTeamIdBySpottyTeamId(lotteryMatch.getHomeTeamId());
		DLLeagueTeamScoreInfoDTO homeTeamScoreInfo = this.teamScoreInfo(h_teamId,lotteryMatch.getLeagueId(), lotteryMatch.getHomeTeamAbbr());
		matchTeamInfo.setHomeTeamScoreInfo(homeTeamScoreInfo);
		Integer v_teamId = this.queryTeamIdBySpottyTeamId(lotteryMatch.getVisitingTeamId());
		DLLeagueTeamScoreInfoDTO visitingTeamScoreInfo = this.teamScoreInfo(v_teamId,lotteryMatch.getLeagueId(),lotteryMatch.getVisitingTeamAbbr());
		matchTeamInfo.setVisitingTeamScoreInfo(visitingTeamScoreInfo);
		
		Integer homeTeamId = dlLeagueTeamMapper.queryTeamId(lotteryMatch.getHomeTeamId());
		List<DLFutureMatchDTO> hFutureMatchInfos = dlFutureMatchService.getFutureMatchInfo(homeTeamId);
		matchTeamInfo.setHFutureMatchInfos(hFutureMatchInfos);
		Integer visitTeamId = dlLeagueTeamMapper.queryTeamId(lotteryMatch.getVisitingTeamId());
		List<DLFutureMatchDTO> vFutureMatchInfos = dlFutureMatchService.getFutureMatchInfo(visitTeamId);
		matchTeamInfo.setVFutureMatchInfos(vFutureMatchInfos);
		return ResultGenerator.genSuccessResult("success", matchTeamInfo);
	}

	private DLLeagueTeamScoreInfoDTO teamScoreInfo(Integer teamId,Integer leagueId ,String teamAbbr) {
		DLLeagueTeamScoreInfoDTO teamScoreInfo = dlMatchTeamScoreService.getTeamScores(teamId,leagueId);
		teamScoreInfo.setTeamId(teamId);
		teamScoreInfo.setTeamName(teamAbbr);
		return teamScoreInfo;
	}
	
	private Integer queryTeamIdBySpottyTeamId(Integer spottyTeamId) {
		Integer teamId = dlLeagueTeamService.queryTeamIdBySpottyTeamId(spottyTeamId);
		return teamId;
	}
	
	@ApiOperation(value = "历史赛事入库", notes = "历史赛事入库")
    @PostMapping("/historyMatchIntoDB")
    public BaseResult<String> historyMatchIntoDB(@Valid @RequestBody StringRemindParam param) {
		return lotteryMatchService.historyMatchIntoDB();
    }
	
	@ApiOperation(value = "筛选当天的比赛联赛信息,给开奖条件用", notes = "筛选当天的比赛联赛信息,给开奖条件用")
    @PostMapping("/getFilterConditionsSomeDay")
    public BaseResult<List<LeagueInfoDTO>> getFilterConditionsSomeDay(@Valid @RequestBody DateStrParam param) {
		List<LeagueInfoDTO> leagueInfos =  lotteryMatchService.getFilterConditionsSomeDay(param.getDateStr());
		return ResultGenerator.genSuccessResult("获取筛选条件列表成功", leagueInfos);
    }
	
	@ApiOperation(value = "筛选当天的比赛联赛信息,给开奖条件用", notes = "筛选当天的比赛联赛信息,给开奖条件用")
	@PostMapping("/getCancelMatches")
	public BaseResult<List<String>> getCancelMatches(@Valid @RequestBody GetCancelMatchesParam param) {
		List<String> leagueInfos =  lotteryMatchService.getCancelMatches(param.getPlayCodes());
		return ResultGenerator.genSuccessResult("success", leagueInfos);
	}
	@ApiOperation(value = "API调用(是否停售)", notes = "API调用(是否停售)")
	@PostMapping("/isShutDownBet")
	public  BaseResult<Boolean>  isShutDownBet(@RequestBody EmptyParam emptyParam) {
		boolean result =  lotteryMatchService.isShutDownBet();
		return ResultGenerator.genSuccessResult("success", result);
	}
	
	@ApiOperation(value = "API调用(获取停售时间)", notes = "API调用(获取停售时间)")
	@PostMapping("/getBetEndTime")
	public  BaseResult<Integer> getBetEndTime(@RequestBody MatchTimePream matchTimePream) {
		Integer result =    lotteryMatchService.getBetEndTime(matchTimePream.getMatchTime());
		return ResultGenerator.genSuccessResult("", result);
	}
	
	@ApiOperation(value = "API调用(比赛是否隐藏)", notes = "API调用(比赛是否隐藏)")
	@PostMapping("/isHideMatch")
	public BaseResult<Boolean>   isHideMatch(@RequestBody IsHideParam isHideParam) {
		boolean result =  lotteryMatchService.isHideMatch(isHideParam.getBetEndTime(),isHideParam.getMatchTime());
		return ResultGenerator.genSuccessResult("success", result);
	}
	
	@ApiOperation(value = "API调用(获取投注信息1)", notes = "API调用(获取投注信息1)")
	@PostMapping("/getBetInfo1")
	public BaseResult<DLZQBetInfoDTO> getBetInfo1(@RequestBody DlJcZqMatchBetParam param){
		DLZQBetInfoDTO betInfoDTO = lotteryMatchService.getBetInfo1(param);
		return ResultGenerator.genSuccessResult("success", betInfoDTO);
	}
	
	@ApiOperation(value = "API调用(获取投注金额)", notes = "API调用(获取投注金额)")
	@PostMapping("/getMinBetMoney")
	public  BaseResult<Double> getMinBetMoney(@RequestBody EmptyParam emptyParam){
		Double result =  lotteryMatchService.getMinBetMoney();
		return ResultGenerator.genSuccessResult("success", result);
	}
	
	@ApiOperation(value = "API调用(获取可投注金额)", notes = "API调用(获取可投注金额)")
	@PostMapping("/canBetMoney")
	public  BaseResult<Integer>  canBetMoney(@RequestBody EmptyParam emptyParam){
		Integer result = lotteryMatchService.canBetMoney();
		return ResultGenerator.genSuccessResult("success", result);
	}
	
	
	@ApiOperation(value = "API调用(获取足球筛选条件列表)", notes = "API调用(获取足球筛选条件列表)")
	@PostMapping("/getMatchByConditions")
	public  BaseResult<List<LeagueInfoDTO>>  getMatchByConditions(@RequestBody EmptyParam emptyParam){
    	List<LeagueInfoDTO> leagueInfos = lotteryMatchService.getFilterConditions();
    	return ResultGenerator.genSuccessResult("获取筛选条件列表成功", leagueInfos);
	}
	
//
//	@ApiOperation(value = "API调用(获取取消的赛事)", notes = "API调用(获取取消的赛事)")
//	@PostMapping("/getCancelMatches")
//	public  BaseResult<List<String>>  getCancelMatches(@RequestBody DlPlayCodeParam param){
//		List<String> ids = lotteryMatchService.getCancelMatches(param);
//		return ResultGenerator.genSuccessResult("获取筛选条件列表成功", ids);
//	}
	
}

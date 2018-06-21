package com.dl.shop.lottery.web;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.enums.MatchPlayTypeEnum;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.JSONHelper;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.BetPayInfoDTO;
import com.dl.lottery.dto.DIZQUserBetCellInfoDTO;
import com.dl.lottery.dto.DIZQUserBetInfoDTO;
import com.dl.lottery.dto.DLLeagueTeamScoreDTO;
import com.dl.lottery.dto.DLLeagueTeamScoreInfoDTO;
import com.dl.lottery.dto.DLZQBetInfoDTO;
import com.dl.lottery.dto.DlJcZqMatchCellDTO;
import com.dl.lottery.dto.DlJcZqMatchListDTO;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.lottery.dto.LeagueMatchAsiaDTO;
import com.dl.lottery.dto.LeagueMatchDaoXiaoDTO;
import com.dl.lottery.dto.LeagueMatchEuropeDTO;
import com.dl.lottery.dto.LotteryMatchDTO;
import com.dl.lottery.dto.MatchBetCellDTO;
import com.dl.lottery.dto.MatchBetPlayDTO;
import com.dl.lottery.dto.MatchInfoForTeamDTO;
import com.dl.lottery.dto.MatchTeamInfosDTO;
import com.dl.lottery.dto.MatchTeamInfosSumDTO;
import com.dl.lottery.dto.TeamSupportDTO;
import com.dl.lottery.enums.LotteryResultEnum;
import com.dl.lottery.param.DateStrParam;
import com.dl.lottery.param.DlJcZqMatchBetParam;
import com.dl.lottery.param.DlJcZqMatchListParam;
import com.dl.lottery.param.GetBetInfoByOrderSn;
import com.dl.lottery.param.GetCancelMatchesParam;
import com.dl.lottery.param.GetFilterConditionsParam;
import com.dl.lottery.param.MatchTeamInfosParam;
import com.dl.lottery.param.QueryMatchParam;
import com.dl.lottery.param.StringRemindParam;
import com.dl.member.api.IUserBonusService;
import com.dl.member.api.IUserService;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.BonusLimitConditionParam;
import com.dl.member.param.StrParam;
import com.dl.order.api.IOrderService;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.service.DlLeagueInfoService;
import com.dl.shop.lottery.service.DlLeagueMatchAsiaService;
import com.dl.shop.lottery.service.DlLeagueMatchDaoXiaoService;
import com.dl.shop.lottery.service.DlLeagueMatchEuropeService;
import com.dl.shop.lottery.service.DlLeagueTeamScoreService;
import com.dl.shop.lottery.service.DlMatchSupportService;
import com.dl.shop.lottery.service.LotteryMatchPlayService;
import com.dl.shop.lottery.service.LotteryMatchService;
import com.dl.shop.lottery.utils.MD5;

import io.swagger.annotations.ApiOperation;

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
    private DlLeagueTeamScoreService dlLeagueTeamScoreService;
    @Resource
    private DlLeagueMatchEuropeService dlLeagueMatchEuropeService;
    @Resource
    private DlLeagueMatchDaoXiaoService dlLeagueMatchDaoXiaoService;
    @Resource
    private DlMatchSupportService dlMatchSupportService;
    @Resource
    private LotteryMatchPlayService lotteryMatchPlayService;
	
    @ApiOperation(value = "获取筛选条件列表", notes = "获取筛选条件列表")
    @PostMapping("/filterConditions")
    public BaseResult<List<LeagueInfoDTO>> getFilterConditions(@Valid @RequestBody GetFilterConditionsParam param) {
    	List<LeagueInfoDTO> leagueInfos = lotteryMatchService.getFilterConditions();
    	return ResultGenerator.genSuccessResult("获取筛选条件列表成功", leagueInfos);
    }
    
	@ApiOperation(value = "获取赛事列表", notes = "获取赛事列表")
    @PostMapping("/getMatchList")
    public BaseResult<DlJcZqMatchListDTO> getMatchList(@Valid @RequestBody DlJcZqMatchListParam param) {
		DlJcZqMatchListDTO dlJcZqMatchListDTO = lotteryMatchService.getMatchList(param);
    	return ResultGenerator.genSuccessResult("获取赛事列表成功", dlJcZqMatchListDTO);
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
		//2 1
		/*if(Integer.valueOf(playType).equals(MatchPlayTypeEnum.PLAY_TYPE_TSO.getcode())) {
			return ResultGenerator.genFailResult("暂不支持该玩法！", null);
		}*/
		//校验赛事投注时间
		MatchBetPlayDTO min = matchBetPlays.get(0);
		if(matchBetPlays.size() > 1) {
			min = matchBetPlays.stream().min((cell1,cell2)->cell1.getMatchTime()-cell2.getMatchTime()).get();
		}
		int betEndTime = min.getMatchTime() - ProjectConstant.BET_PRESET_TIME;
		Date now = new Date();
		int nowTime = Long.valueOf(now.toInstant().getEpochSecond()).intValue();
		if(nowTime - betEndTime > 0) {
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
		if(orderMoney < 100) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_WC.getCode(), LotteryResultEnum.BET_MATCH_WC.getMsg());
		}
		int canBetMoney = lotteryMatchService.canBetMoney();
		if(orderMoney > canBetMoney) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_STOP.getCode(), LotteryResultEnum.BET_MATCH_STOP.getMsg());
		}
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
		int betEndTime = min.getMatchTime() - ProjectConstant.BET_PRESET_TIME;
		Date now = new Date();
		int nowTime = Long.valueOf(now.toInstant().getEpochSecond()).intValue();
		if(nowTime - betEndTime > 0) {
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
		if(orderMoney < 100) {
			return ResultGenerator.genResult(LotteryResultEnum.BET_MATCH_WC.getCode(), LotteryResultEnum.BET_MATCH_WC.getMsg());
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
		DLLeagueTeamScoreInfoDTO homeTeamScoreInfo = this.teamScoreInfo(lotteryMatch.getHomeTeamId(), lotteryMatch.getHomeTeamAbbr());
		matchTeamInfo.setHomeTeamScoreInfo(homeTeamScoreInfo);
		DLLeagueTeamScoreInfoDTO visitingTeamScoreInfo = this.teamScoreInfo(lotteryMatch.getVisitingTeamId(), lotteryMatch.getVisitingTeamAbbr());
		matchTeamInfo.setVisitingTeamScoreInfo(visitingTeamScoreInfo);
		return ResultGenerator.genSuccessResult("success", matchTeamInfo);
	}

	private DLLeagueTeamScoreInfoDTO teamScoreInfo(Integer teamId, String teamAbbr) {
		DLLeagueTeamScoreDTO tteamScore = dlLeagueTeamScoreService.getTeamScores(teamId, 0);
		DLLeagueTeamScoreDTO hteamScore = dlLeagueTeamScoreService.getTeamScores(teamId, 1);
		DLLeagueTeamScoreDTO lteamScore = dlLeagueTeamScoreService.getTeamScores(teamId, 2);
		DLLeagueTeamScoreInfoDTO teamScoreInfo = new DLLeagueTeamScoreInfoDTO();
		teamScoreInfo.setTeamId(teamId);
		teamScoreInfo.setTeamName(teamAbbr);
		teamScoreInfo.setHteamScore(hteamScore);
		teamScoreInfo.setTteamScore(tteamScore);
		teamScoreInfo.setHteamScore(hteamScore);
		return teamScoreInfo;
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
}

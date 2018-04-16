package com.dl.shop.lottery.web;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.JSONHelper;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.BetPayInfoDTO;
import com.dl.lottery.dto.DIZQUserBetCellInfoDTO;
import com.dl.lottery.dto.DIZQUserBetInfoDTO;
import com.dl.lottery.dto.DLZQBetInfoDTO;
import com.dl.lottery.dto.DlJcZqMatchListDTO;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.lottery.dto.LotteryMatchDTO;
import com.dl.lottery.dto.MatchBetCellDTO;
import com.dl.lottery.dto.MatchBetPlayDTO;
import com.dl.lottery.dto.MatchTeamInfosDTO;
import com.dl.lottery.param.DlJcZqMatchBetParam;
import com.dl.lottery.param.DlJcZqMatchListParam;
import com.dl.lottery.param.GetBetInfoByOrderSn;
import com.dl.lottery.param.GetFilterConditionsParam;
import com.dl.lottery.param.MatchTeamInfosParam;
import com.dl.lottery.param.QueryMatchParam;
import com.dl.member.api.IUserBonusService;
import com.dl.member.api.IUserService;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.StrParam;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.OrderInfoAndDetailDTO;
import com.dl.order.param.OrderSnParam;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.service.DlLeagueInfoService;
import com.dl.shop.lottery.service.LotteryMatchService;
import com.dl.shop.lottery.utils.MD5;

import io.swagger.annotations.ApiOperation;

/**
* Created by CodeGenerator on 2018/03/21.
*/
@RestController
@RequestMapping("/lottery/match")
public class LotteryMatchController {
	
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
	
    @ApiOperation(value = "获取筛选条件列表", notes = "获取筛选条件列表")
    @PostMapping("/filterConditions")
    public BaseResult<List<LeagueInfoDTO>> getFilterConditions(@Valid @RequestBody GetFilterConditionsParam param) {
    	List<LeagueInfoDTO> leagueInfos = dlLeagueInfoService.getFilterConditions();
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
		List<MatchBetPlayDTO> matchBetPlays = param.getMatchBetPlays();
		if(matchBetPlays == null || matchBetPlays.size() < 1) {
			return ResultGenerator.genFailResult("请选择有效的参赛场次！", null);
		}
		//校验赛事投注时间
		MatchBetPlayDTO min = matchBetPlays.get(0);
		if(matchBetPlays.size() > 1) {
			min = matchBetPlays.stream().min((cell1,cell2)->cell1.getMatchTime()-cell2.getMatchTime()).get();
		}
		int betEndTime = min.getMatchTime() - ProjectConstant.BET_PRESET_TIME;
		LocalDate localDate = LocalDateTime.ofEpochSecond(betEndTime, 0, ZoneOffset.UTC).toLocalDate();
		LocalDate now = LocalDate.now();
		if(localDate.isBefore(now)) {
			return ResultGenerator.genFailResult("您有参赛场次投注时间已过！", null);
		}
		//校验串关
		String betType = param.getBetType();
		if(StringUtils.isBlank(betType)) {
			return ResultGenerator.genFailResult("请选择有效的串关！", null);
		}
		if((matchBetPlays.size() == 1 && !betType.equals("11")) || (matchBetPlays.size() > 1 && betType.contains("11"))) {
			return ResultGenerator.genFailResult("请求场次与串关不符！", null);
		}
		List<Integer> betNums = Arrays.asList(betType.split(",")).stream().map(str->Integer.parseInt(str.split("")[0])).sorted().collect(Collectors.toList());
		int maxBetNum = betNums.get(betNums.size()-1);
		if(maxBetNum > matchBetPlays.size()) {
			return ResultGenerator.genFailResult("请求场次与串关不符！", null);
		}
		//校验投注选项
		List<MatchBetPlayDTO> collect = matchBetPlays.stream().filter(dto->{
			int sum = dto.getMatchBetCells().stream().mapToInt(betCell->betCell.getBetCells()==null?0:betCell.getBetCells().size()).sum();
			return sum == 0;
		}).collect(Collectors.toList());
		if(collect.size() > 0) {
			return ResultGenerator.genFailResult("您有参赛场次没有投注选项！", null);
		}
		//校验胆的个数设置
		int minBetNum = betNums.get(0);
		int danEnableNum = minBetNum -1;
		if(minBetNum == matchBetPlays.size()) {
			danEnableNum = 0;
		}
		long danNum = matchBetPlays.stream().filter(dto->dto.getIsDan() == 1).count();
		if(danNum > danEnableNum) {
			return ResultGenerator.genFailResult("参赛设胆场次有误，请核对！", null);
		}
		
		//设置投注倍数
		Integer times = param.getTimes();
		if(null == times || times < 1) {
			param.setTimes(1);
		}
		DLZQBetInfoDTO betInfo = lotteryMatchService.getBetInfo(param);
		return ResultGenerator.genSuccessResult("success", betInfo);
	}
	@ApiOperation(value = "保存投注信息", notes = "保存投注信息")
	@PostMapping("/saveBetInfo")
	public BaseResult<BetPayInfoDTO> saveBetInfo(@Valid @RequestBody DlJcZqMatchBetParam param) {
		List<MatchBetPlayDTO> matchBetPlays = param.getMatchBetPlays();
		if(matchBetPlays == null || matchBetPlays.size() < 1) {
			return ResultGenerator.genFailResult("请选择有效的参赛场次！", null);
		}
		String playType = param.getPlayType();
		if(StringUtils.isBlank(playType)) {
			return ResultGenerator.genFailResult("请选择有效的赛事玩法！", null);
		}
		try {
			int parseInt = Integer.parseInt(playType);
			if(parseInt < 1 || parseInt > 7) {
				return ResultGenerator.genFailResult("请选择有效的赛事玩法！", null);
			}
		} catch (NumberFormatException e) {
			return ResultGenerator.genFailResult("请选择有效的赛事玩法！", null);
		}
		//校验赛事投注时间
		MatchBetPlayDTO min = matchBetPlays.get(0);
		if(matchBetPlays.size() > 1) {
			min = matchBetPlays.stream().min((cell1,cell2)->cell1.getMatchTime()-cell2.getMatchTime()).get();
		}
		int betEndTime = min.getMatchTime() - ProjectConstant.BET_PRESET_TIME;
		LocalDate localDate = LocalDateTime.ofEpochSecond(betEndTime, 0, ZoneOffset.UTC).toLocalDate();
		LocalDate now = LocalDate.now();
		if(localDate.isBefore(now)) {
			return ResultGenerator.genFailResult("您有参赛场次投注时间已过！", null);
		}
		//校验串关
		String betTypeStr = param.getBetType();
		if(StringUtils.isBlank(betTypeStr)) {
			return ResultGenerator.genFailResult("请选择有效的串关！", null);
		}
		String[] betTypes = betTypeStr.split(",");
		boolean isCheckedBetType = false;
		try {
			for(String betType: betTypes) {
				char[] charArray = betType.toCharArray();
				if(charArray.length == 2 && charArray[1] == '1') {
					int num = Integer.valueOf(String.valueOf(charArray[0]));
					if(num > 0 && num < 9) {
						isCheckedBetType = true;
					}
				}
			}
		} catch (NumberFormatException e) {
		}
		if(!isCheckedBetType) {
			return ResultGenerator.genFailResult("请选择有效的串关！", null);
		}
		if((matchBetPlays.size() == 1 && !betTypeStr.equals("11")) || (matchBetPlays.size() > 1 && betTypeStr.contains("11"))) {
			return ResultGenerator.genFailResult("请求场次与串关不符！", null);
		}
		List<Integer> betNums = Arrays.asList(betTypeStr.split(",")).stream().map(str->Integer.parseInt(str.split("")[0])).sorted().collect(Collectors.toList());
		int maxBetNum = betNums.get(betNums.size()-1);
		if(maxBetNum > matchBetPlays.size()) {
			return ResultGenerator.genFailResult("请求场次与串关不符！", null);
		}
		//校验投注选项
		List<MatchBetPlayDTO> collect = matchBetPlays.stream().filter(dto->{
			List<MatchBetCellDTO> list = dto.getMatchBetCells().stream().filter(betCell->betCell.getBetCells() == null || betCell.getBetCells().size() == 0).collect(Collectors.toList());
			return list.size() > 0;
		}).collect(Collectors.toList());
		if(collect.size() > 0) {
			return ResultGenerator.genFailResult("您有参赛场次玩法没有投注选项！", null);
		}
		//校验胆的个数设置
		int minBetNum = betNums.get(0);
		int danEnableNum = minBetNum -1;
		if(minBetNum == matchBetPlays.size()) {
			danEnableNum = 0;
		}
		long danNum = matchBetPlays.stream().filter(dto->dto.getIsDan() == 1).count();
		if(danNum > danEnableNum) {
			return ResultGenerator.genFailResult("参赛设胆场次有误，请核对！", null);
		}
		
		//设置投注倍数
		Integer times = param.getTimes();
		if(null == times || times < 1) {
			param.setTimes(1);
		}
		StrParam strParam = new StrParam();
		BaseResult<UserDTO> userInfoExceptPassRst = userService.userInfoExceptPass(strParam);
		if(userInfoExceptPassRst.getCode() != 0) {
			ResultGenerator.genFailResult("操作失败！", null);
		}
		String totalMoney = userInfoExceptPassRst.getData().getTotalMoney();
		Double userTotalMoney = Double.valueOf(totalMoney);
		BaseResult<List<UserBonusDTO>> userBonusListRst = userBonusService.queryValidBonusList(strParam);
		if(userBonusListRst.getCode() != 0) {
			ResultGenerator.genFailResult("操作失败！", null);
		}
		DLZQBetInfoDTO betInfo = lotteryMatchService.getBetInfo(param);
		Double orderMoney = betInfo.getMoney();
		List<UserBonusDTO> userBonusList = userBonusListRst.getData();
		UserBonusDTO userBonusDto = null;
		if(userBonusList != null && userBonusList.size() > 0) {
			if(param.getBonusId() != null && param.getBonusId().intValue() != 0) {
				Optional<UserBonusDTO> findFirst = userBonusList.stream().filter(dto->dto.getUserBonusId().equals(param.getBonusId())).findFirst();
				userBonusDto = findFirst.isPresent()?findFirst.get():null;
			}else {
				List<UserBonusDTO> userBonuses = userBonusList.stream().filter(dto->{
					double minGoodsAmount = dto.getBonusPrice().doubleValue();
					return orderMoney < minGoodsAmount ? false : true;
				}).sorted((n1,n2)->n1.getBonusPrice().compareTo(n2.getBonusPrice()))
						.collect(Collectors.toList());
				if(userBonuses.size() > 0) {
					if(null != param.getBonusId()) {
						Optional<UserBonusDTO> findFirst = userBonusList.stream().filter(dto->dto.getBonusId()==param.getBonusId()).findFirst();
						userBonusDto = findFirst.isPresent()?findFirst.get():null;
					}
					userBonusDto = userBonusDto == null?userBonuses.get(0):userBonusDto;
				}
			}
		}
		String bonusId = userBonusDto != null?userBonusDto.getUserBonusId().toString():null;
		Double bonusAmount = userBonusDto!=null?userBonusDto.getBonusPrice().doubleValue():0.0;
		Double amountTemp = orderMoney - bonusAmount;
		Double surplus = userTotalMoney>amountTemp?amountTemp:userTotalMoney;
		Double thirdPartyPaid = amountTemp - surplus;
		List<DIZQUserBetCellInfoDTO>  userBetCellInfos = new ArrayList<DIZQUserBetCellInfoDTO>(matchBetPlays.size());
		for(MatchBetPlayDTO matchCell: matchBetPlays) {
			userBetCellInfos.add(new DIZQUserBetCellInfoDTO(matchCell));
		}
		int betNum = betInfo.getBetNum();
		//缓存订单支付信息
		DIZQUserBetInfoDTO dto = new DIZQUserBetInfoDTO(param);
		dto.setUserBetCellInfos(userBetCellInfos);
		dto.setBetNum(betNum);
		dto.setMoney(orderMoney);
		dto.setBonusAmount(bonusAmount);
		dto.setBonusId(bonusId);
		dto.setSurplus(surplus);
		dto.setMaxBonus(betInfo.getMaxBonus());
		dto.setThirdPartyPaid(thirdPartyPaid);
		int requestFrom = 0;
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
		betPlayInfoDTO.setBonusAmount(bonusAmount);
		betPlayInfoDTO.setBonusId(bonusId);
		betPlayInfoDTO.setBonusList(userBonusList);
		betPlayInfoDTO.setOrderMoney(orderMoney);
		betPlayInfoDTO.setSurplus(surplus);
		betPlayInfoDTO.setThirdPartyPaid(thirdPartyPaid);
		return ResultGenerator.genSuccessResult("success", betPlayInfoDTO);
	}
    
	@ApiOperation(value = "抓取赛事列表保存", notes = "抓取赛事列表保存")
    @PostMapping("/saveMatchList")
    public BaseResult<String> saveMatchList() {
		lotteryMatchService.saveMatchList();
    	return ResultGenerator.genSuccessResult("抓取赛事列表保存成功");
    }
	
	@ApiOperation(value = "查询比赛结果", notes = "查询比赛结果")
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
		OrderSnParam orderSnParam = new OrderSnParam();
		orderSnParam.setOrderSn(param.getOrderSn());
		BaseResult<OrderInfoAndDetailDTO> orderWithDetailByOrderSn = orderService.getOrderWithDetailByOrderSn(orderSnParam);
		if(orderWithDetailByOrderSn.getCode() != 0) {
			return ResultGenerator.genFailResult();
		}
		DLZQBetInfoDTO dto = lotteryMatchService.getBetInfoByOrderInfo(orderWithDetailByOrderSn.getData());
    	return ResultGenerator.genSuccessResult("success",dto);
    }
	
	@ApiOperation(value = "球队分析信息", notes = "球队分析信息")
    @PostMapping("/matchTeamInfos")
    public BaseResult<MatchTeamInfosDTO> matchTeamInfos(@Valid @RequestBody MatchTeamInfosParam param) {
		MatchTeamInfosDTO matchTeamInfos = lotteryMatchService.matchTeamInfos(param.getMatchId());
    	return ResultGenerator.genSuccessResult("success", matchTeamInfos);
    }
}

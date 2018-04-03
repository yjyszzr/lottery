package com.dl.shop.lottery.web;
import java.util.ArrayList;
import java.util.List;
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
import com.dl.dto.BetPayInfoDTO;
import com.dl.dto.DIZQUserBetCellInfoDTO;
import com.dl.dto.DIZQUserBetInfoDTO;
import com.dl.dto.DLZQBetInfoDTO;
import com.dl.dto.DlJcZqMatchListDTO;
import com.dl.dto.LotteryMatchDTO;
import com.dl.dto.MatchBetCellDTO;
import com.dl.member.api.IUserBonusService;
import com.dl.member.api.IUserService;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.StrParam;
import com.dl.param.DateStrParam;
import com.dl.param.DlJcZqMatchBetParam;
import com.dl.param.DlJcZqMatchListParam;
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
	
	@ApiOperation(value = "获取赛事列表", notes = "获取赛事列表")
    @PostMapping("/getMatchList")
    public BaseResult<DlJcZqMatchListDTO> getMatchList(@Valid @RequestBody DlJcZqMatchListParam param) {
		DlJcZqMatchListDTO dlJcZqMatchListDTO = lotteryMatchService.getMatchList(param);
    	return ResultGenerator.genSuccessResult("获取赛事列表成功", dlJcZqMatchListDTO);
    }
	
	@ApiOperation(value = "计算投注信息", notes = "计算投注信息,times默认值为1，betType默认值为11")
	@PostMapping("/getBetInfo")
	public BaseResult<DLZQBetInfoDTO> getBetInfo(@Valid @RequestBody DlJcZqMatchBetParam param) {
		Integer times = param.getTimes();
		if(null == times || times < 1) {
			param.setTimes(1);
		}
		String betType = param.getBetType();
		if(StringUtils.isBlank(betType)) { 
			param.setBetType("11");
		}
		DLZQBetInfoDTO betInfo = lotteryMatchService.getBetInfo(param);
		return ResultGenerator.genSuccessResult("success", betInfo);
	}
	
	@ApiOperation(value = "保存投注信息", notes = "保存投注信息")
	@PostMapping("/saveBetInfo")
	public BaseResult<BetPayInfoDTO> saveBetInfo(@Valid @RequestBody DlJcZqMatchBetParam param) {
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
		List<UserBonusDTO> userBonuses = userBonusList.stream().filter(dto->{
			String minGoodsAmountStr = dto.getMinGoodsAmount();
			Double minGoodsAmount = Double.valueOf(minGoodsAmountStr);
			return orderMoney < minGoodsAmount ? false : true;
		}).sorted((n1,n2)->n1.getBonusPrice().compareTo(n2.getBonusPrice()))
		.collect(Collectors.toList());
		String bonusId = null;
		Double bonusAmount = 0.0;
		if(userBonuses.size() > 0) {
			bonusId = userBonuses.get(0).getBonusId()+"";
			bonusAmount = userBonuses.get(0).getBonusPrice().doubleValue();
		}
		Double surplus = userTotalMoney>orderMoney?orderMoney:userTotalMoney;
		Double thirdPartyPaid = orderMoney - surplus - bonusAmount;
		List<MatchBetCellDTO> matchBetCells = param.getMatchBetCells();
		List<DIZQUserBetCellInfoDTO>  userBetCellInfos = new ArrayList<DIZQUserBetCellInfoDTO>(matchBetCells.size());
		for(MatchBetCellDTO matchCell: matchBetCells) {
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
		dto.setThirdPartyPaid(thirdPartyPaid);
		String dtoJson = JSONHelper.bean2json(dto);
		String keyStr = "bet_info_" + SessionUtil.getUserId() +"_"+ System.currentTimeMillis();
		String key = MD5.crypt(keyStr);
		stringRedisTemplate.opsForValue().set(key, dtoJson);
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
    public BaseResult<List<LotteryMatchDTO>> queryMatchResult(@RequestBody DateStrParam dateStrParam) {
		List<LotteryMatchDTO> lotteryMatchDTOList = lotteryMatchService.queryMatchResult(dateStrParam.getDateStr());
    	return ResultGenerator.genSuccessResult("查询比赛结果成功",lotteryMatchDTOList);
    }
	
}

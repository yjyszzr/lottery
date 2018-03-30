package com.dl.shop.lottery.web;
import java.util.List;

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
import com.dl.dto.DIZQUserBetInfoDTO;
import com.dl.dto.DLZQBetInfoDTO;
import com.dl.dto.DlJcZqMatchListDTO;
import com.dl.param.DlJcZqMatchBetParam;
import com.dl.param.DlJcZqMatchListParam;
import com.dl.param.DlJcZqSaveBetInfoParam;
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
	public BaseResult<BetPayInfoDTO> saveBetInfo(@Valid @RequestBody DlJcZqSaveBetInfoParam param) {
		//List<UserBonusDTO> bonusList = null;
		String bonusId = null;
		Double orderMoney = param.getMoney();
		Double surplus = null;
		Double bonusAmount = null;
		Double thirdPartyPaid = orderMoney - surplus - bonusAmount;
		//缓存订单支付信息
		DIZQUserBetInfoDTO dto = new DIZQUserBetInfoDTO(param);
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
//		betPlayInfoDTO.setBonusList(bonusList);
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
	
}

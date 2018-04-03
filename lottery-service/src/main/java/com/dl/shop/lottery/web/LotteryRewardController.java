package com.dl.shop.lottery.web;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.param.DlRewardParam;
import com.dl.param.DlToAwardingParam;
import com.dl.shop.lottery.service.LotteryRewardService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/lottery/reward")
public class LotteryRewardController {

	@Resource
	private LotteryRewardService lotteryRewardService;
	
	@ApiOperation(value = "根据场次id拉取中奖数据", notes = "根据场次id拉取中奖数据")
    @PostMapping("/saveRewardData")
    public BaseResult<String> saveRewardData(@Valid @RequestBody DlRewardParam param) {
		lotteryRewardService.saveRewardData(param);
    	return ResultGenerator.genSuccessResult("拉取开奖数据成功");
    }
	
	@ApiOperation(value = "兑奖接口", notes = "兑奖接口")
    @PostMapping("/toAwarding")
    public BaseResult<String> toAwarding(@Valid @RequestBody DlToAwardingParam param) {
		lotteryRewardService.toAwarding(param);
    	return ResultGenerator.genSuccessResult("兑奖成功");
    }
}

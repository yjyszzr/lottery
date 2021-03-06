package com.dl.shop.lottery.web;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.LotteryRewardByIssueDTO;
import com.dl.lottery.param.DlLotteryRewardByIssueParam;
import com.dl.lottery.param.DlRewardParam;
import com.dl.lottery.param.DlToAwardingParam;
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
	
	@ApiOperation(value = "解析中奖文件", notes = "解析中奖文件")
    @PostMapping("/resolveTxt")
    public BaseResult<String> resolveTxt(@Valid @RequestBody DlRewardParam param) {
		lotteryRewardService.resovleRewardTxt();
    	return ResultGenerator.genSuccessResult("拉取开奖数据成功","");
    }
	
	@ApiOperation(value = "根据期次，查询审核通过的开奖数据", notes = "根据期次，查询审核通过的开奖数据")
    @PostMapping("/queryRewardByIssue")
    public BaseResult<LotteryRewardByIssueDTO> queryRewardByIssue(@Valid @RequestBody DlLotteryRewardByIssueParam param) {
		LotteryRewardByIssueDTO lotteryRewardByIssueDTO = lotteryRewardService.queryRewardByIssue(param);
    	return ResultGenerator.genSuccessResult("根据期次，查询审核通过的开奖数据成功", lotteryRewardByIssueDTO);
    }
	
	@ApiOperation(value = "更新待开奖的订单,提供给定时任务用", notes = "更新待开奖的订单,提供给定时任务用")
	@PostMapping("/updateOrderAfterOpenReward")
    public BaseResult<String> updateOrderAfterOpenReward(@RequestBody EmptyParam emptyParam) {
    	return ResultGenerator.genSuccessResult("更新待开奖的订单成功", lotteryRewardService.updateOrderAfterOpenReward());
	}

	
}

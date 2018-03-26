package com.dl.shop.lottery.web;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.service.LotteryPrintService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/lottery/print")
public class LotteryPrintController {

	@Resource
	private LotteryPrintService lotteryPrintService;
	
	@ApiOperation(value = "投注接口", notes = "投注接口")
    @PostMapping("/toStake")
    public BaseResult<String> toStake() {
    	return ResultGenerator.genSuccessResult("投注成功");
    }
	
	@ApiOperation(value = "投注结果查询", notes = "投注结果查询")
    @PostMapping("/queryStake")
    public BaseResult<String> queryStake() {
    	return ResultGenerator.genSuccessResult("投注结果查询成功");
    }
	
	@ApiOperation(value = "期次查询", notes = "期次查询")
    @PostMapping("/queryIssue")
    public BaseResult<String> queryIssue() {
    	return ResultGenerator.genSuccessResult("期次查询成功");
    }
	
	@ApiOperation(value = "账户余额查询", notes = "账户余额查询")
    @PostMapping("/queryAccount")
    public BaseResult<String> queryAccount() {
    	return ResultGenerator.genSuccessResult("账户余额查询成功");
    }
	
	@ApiOperation(value = "期次投注对账文件查询", notes = "期次投注对账文件查询")
    @PostMapping("/queryStakeFile")
    public BaseResult<String> queryStakeFile() {
    	return ResultGenerator.genSuccessResult("期次投注对账文件查询成功");
    }
	
	@ApiOperation(value = "期次中奖文件查询", notes = "期次中奖文件查询")
    @PostMapping("/queryPrizeFile")
    public BaseResult<String> queryPrizeFile() {
    	return ResultGenerator.genSuccessResult("期次中奖文件查询成功");
    }
}

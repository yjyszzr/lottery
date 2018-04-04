package com.dl.shop.lottery.web;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.DlQueryAccountDTO;
import com.dl.lottery.dto.DlQueryIssueDTO;
import com.dl.lottery.dto.DlQueryPrizeFileDTO;
import com.dl.lottery.dto.DlQueryStakeDTO;
import com.dl.lottery.dto.DlQueryStakeFileDTO;
import com.dl.lottery.dto.DlToStakeDTO;
import com.dl.lottery.param.DlCallbackStakeParam;
import com.dl.lottery.param.DlQueryAccountParam;
import com.dl.lottery.param.DlQueryIssueParam;
import com.dl.lottery.param.DlQueryPrizeFileParam;
import com.dl.lottery.param.DlQueryStakeFileParam;
import com.dl.lottery.param.DlQueryStakeParam;
import com.dl.lottery.param.DlToStakeParam;
import com.dl.lottery.param.SaveLotteryPrintInfoParam;
import com.dl.shop.lottery.service.LotteryPrintService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/lottery/print")
public class LotteryPrintController {

	@Resource
	private LotteryPrintService lotteryPrintService;
	
	@ApiOperation(value = "投注接口", notes = "投注接口")
    @PostMapping("/toStake")
    public BaseResult<DlToStakeDTO> toStake(@Valid @RequestBody DlToStakeParam param) {
		DlToStakeDTO dlToStakeDTO = lotteryPrintService.toStake(param);
    	return ResultGenerator.genSuccessResult("投注成功", dlToStakeDTO);
    }
	
	@ApiOperation(value = "投注结果通知", notes = "投注结果通知")
    @PostMapping("/callbackStake")
    public BaseResult<String> callbackStake(@RequestBody DlCallbackStakeParam param) {
		lotteryPrintService.callbackStake(param);
    	return ResultGenerator.genSuccessResult("投注结果通知成功");
    }
	
	@ApiOperation(value = "投注结果查询", notes = "投注结果查询")
    @PostMapping("/queryStake")
    public BaseResult<DlQueryStakeDTO> queryStake(@Valid @RequestBody DlQueryStakeParam param) {
		DlQueryStakeDTO dlQueryStakeDTO = lotteryPrintService.queryStake(param);
    	return ResultGenerator.genSuccessResult("投注结果查询成功", dlQueryStakeDTO);
    }
	
	@ApiOperation(value = "期次查询", notes = "期次查询")
    @PostMapping("/queryIssue")
    public BaseResult<DlQueryIssueDTO> queryIssue(@Valid @RequestBody DlQueryIssueParam param) {
		DlQueryIssueDTO dlQueryIssueDTO = lotteryPrintService.queryIssue(param);
    	return ResultGenerator.genSuccessResult("期次查询成功", dlQueryIssueDTO);
    }
	
	@ApiOperation(value = "账户余额查询", notes = "账户余额查询")
    @PostMapping("/queryAccount")
    public BaseResult<DlQueryAccountDTO> queryAccount(@Valid @RequestBody DlQueryAccountParam param) {
		DlQueryAccountDTO dlQueryAccountDTO = lotteryPrintService.queryAccount(param);
    	return ResultGenerator.genSuccessResult("账户余额查询成功", dlQueryAccountDTO);
    }
	
	@ApiOperation(value = "期次投注对账文件查询", notes = "期次投注对账文件查询")
    @PostMapping("/queryStakeFile")
    public BaseResult<DlQueryStakeFileDTO> queryStakeFile(@Valid @RequestBody DlQueryStakeFileParam param) {
		DlQueryStakeFileDTO dlQueryStakeFileDTO = lotteryPrintService.queryStakeFile(param);
    	return ResultGenerator.genSuccessResult("期次投注对账文件查询成功", dlQueryStakeFileDTO);
    }
	
	@ApiOperation(value = "期次中奖文件查询", notes = "期次中奖文件查询")
    @PostMapping("/queryPrizeFile")
    public BaseResult<DlQueryPrizeFileDTO> queryPrizeFile(@Valid @RequestBody DlQueryPrizeFileParam param) {
		DlQueryPrizeFileDTO dlQueryPrizeFileDTO = lotteryPrintService.queryPrizeFile(param);
    	return ResultGenerator.genSuccessResult("期次中奖文件查询成功", dlQueryPrizeFileDTO);
    }
	
	@ApiOperation(value = "生成预出票信息", notes = "生成预出票信息")
    @PostMapping("/save")
    public BaseResult<String> saveLotteryPrintInfo(@Valid @RequestBody SaveLotteryPrintInfoParam param) {
		return lotteryPrintService.saveLotteryPrintInfo(param);
    }
}

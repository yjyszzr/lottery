package com.dl.shop.lottery.web;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.dto.DlJcZqMatchListDTO;
import com.dl.param.DlJcZqMatchListParam;
import com.dl.shop.lottery.service.LotteryMatchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "获取赛事列表")
@RestController
@RequestMapping("/lottery/match")
public class LotteryMatchController {
	
	@Resource
	private LotteryMatchService lotteryMatchService;
	
	@ApiOperation(value = "获取赛事列表", notes = "获取赛事列表")
    @PostMapping("/getMatchList")
    public BaseResult<DlJcZqMatchListDTO> getMatchList(@Valid @RequestBody DlJcZqMatchListParam param) {
		DlJcZqMatchListDTO dlJcZqMatchListDTO = lotteryMatchService.getMatchList(param);
    	return ResultGenerator.genSuccessResult("获取赛事列表成功", dlJcZqMatchListDTO);
    }
	
}

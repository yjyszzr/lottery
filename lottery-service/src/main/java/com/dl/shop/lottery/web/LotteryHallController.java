package com.dl.shop.lottery.web;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.DlHallDTO;
import com.dl.lottery.dto.DlPlayClassifyDTO;
import com.dl.lottery.param.DlPlayClassifyParam;
import com.dl.shop.lottery.service.LotteryHallService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/lottery/hall")
public class LotteryHallController {

	@Resource
    private LotteryHallService lotteryHallService;
	
	@ApiOperation(value = "获取彩票大厅数据", notes = "获取彩票大厅数据")
    @PostMapping("/getHallData")
    public BaseResult<DlHallDTO> getHallData() {
		DlHallDTO dlHallDTO = lotteryHallService.getHallData();
    	return ResultGenerator.genSuccessResult("获取彩票大厅数据成功", dlHallDTO);
    }
	
	@ApiOperation(value = "获取彩票玩法列表", notes = "获取彩票玩法列表")
    @PostMapping("/getPlayClassifyList")
    public BaseResult<DlPlayClassifyDTO> getPlayClassifyList(@Valid @RequestBody DlPlayClassifyParam param) {
		DlPlayClassifyDTO dlPlayClassifyDTO = lotteryHallService.getPlayClassifyList(param);
    	return ResultGenerator.genSuccessResult("获取彩票玩法列表成功", dlPlayClassifyDTO);
    }
}

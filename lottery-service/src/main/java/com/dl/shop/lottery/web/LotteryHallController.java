package com.dl.shop.lottery.web;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.dto.DlHallDTO;
import com.dl.param.DlHallParam;
import com.dl.shop.lottery.service.LotteryHallService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/lottery/hall")
public class LotteryHallController {

	@Resource
    private LotteryHallService lotteryHallService;
	
	@ApiOperation(value = "获取彩票大厅数据", notes = "获取彩票大厅数据")
    @PostMapping("/getHallData")
    public BaseResult<DlHallDTO> getHallData(@Valid @RequestBody DlHallParam param) {
		DlHallDTO dlHallDTO = lotteryHallService.getHallData(param);
    	return ResultGenerator.genSuccessResult("获取彩票大厅数据成功", dlHallDTO);
    }
}

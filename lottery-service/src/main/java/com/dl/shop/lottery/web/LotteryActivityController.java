package com.dl.shop.lottery.web;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Condition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.ActivityDTO;
import com.dl.lottery.param.ActTypeParam;
import com.dl.shop.lottery.dao.LotteryActivityMapper;
import com.dl.shop.lottery.model.LotteryActivity;
import com.dl.shop.lottery.service.LotteryActivityService;

import io.swagger.annotations.ApiOperation;

/**
* Created by CodeGenerator on 2018/03/23.
*/
@RestController
@RequestMapping("/lottery/activity")
public class LotteryActivityController {
	
    @Resource
    private LotteryActivityService lotteryActivityService;
	
	@ApiOperation(value = "根据活动类型查询有效的活动", notes = "根据活动类型查询有效的活动")
    @PostMapping("/queryActivityByActType")	
    public BaseResult<List<ActivityDTO>> queryActivityByActType(@RequestBody ActTypeParam actTypeParam){
		return lotteryActivityService.queryActivityByActType(actTypeParam);
	}
}

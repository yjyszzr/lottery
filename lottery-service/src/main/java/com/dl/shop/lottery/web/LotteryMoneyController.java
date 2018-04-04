package com.dl.shop.lottery.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.param.LotterySelectBallParam;
import com.dl.lottery.param.LotterySelectBallParam.SelectData;
import com.dl.shop.lottery.utils.LotteryUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 计算彩票金额
 */
@Api(value = "计算彩票金额")
@RestController
@RequestMapping("/lottery/money")
public class LotteryMoneyController {

	@ApiOperation(value = "获取彩票注数", notes = "获取彩票注数")
    @PostMapping("/getCountAndMoney")
    public BaseResult<String> getCountAndMoney(@Valid @RequestBody LotterySelectBallParam param) {
		List<SelectData> selectDatas = param.getSelectBallParam();
		int combCounts = 0;
		if(CollectionUtils.isNotEmpty(selectDatas)) {
			List<HashMap<String, Vector<String>>> selectBalls = new ArrayList<HashMap<String, Vector<String>>>();
			for(SelectData sd : selectDatas) {
				HashMap<String, Vector<String>> selectBall = new HashMap<String, Vector<String>>();
				selectBall.put(sd.getMatchId(), sd.getMatchResult());
				selectBalls.add(selectBall);
			}
			combCounts = LotteryUtil.getCount(selectBalls);
		}
    	return ResultGenerator.genSuccessResult("获取彩票注数成功", String.valueOf(combCounts));
    }
	
}

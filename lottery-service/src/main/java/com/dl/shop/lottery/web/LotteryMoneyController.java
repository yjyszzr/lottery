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
import com.dl.dto.TestDemoDTO;
import com.dl.param.LotterySelectBallParam;
import com.dl.param.LotterySelectBallParam.SelectData;
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
    public BaseResult<TestDemoDTO> getCountAndMoney(@Valid @RequestBody LotterySelectBallParam param) {
		TestDemoDTO dto = new TestDemoDTO();
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
		dto.setTestStr(String.valueOf(combCounts));
    	return ResultGenerator.genSuccessResult("获取彩票注数成功", dto);
    }
	
}

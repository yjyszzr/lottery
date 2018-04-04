package com.dl.lottery.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.lottery.param.SaveLotteryPrintInfoParam;

@FeignClient(value="lottery-service")
public interface ILotteryPrintService {

	@RequestMapping(path="/lottery/print/save", method=RequestMethod.POST)
	public BaseResult<String> saveLotteryPrintInfo(SaveLotteryPrintInfoParam param);
}
package com.dl.lottery.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.DLZQBetInfoDTO;
import com.dl.lottery.param.DlJcZqMatchBetParam;

@FeignClient(value="lottery-service")
public interface ILotteryMatchService {

	@RequestMapping(path="/lottery/match/getBetInfo", method=RequestMethod.POST)
	public BaseResult<DLZQBetInfoDTO> getBetInfo(DlJcZqMatchBetParam param);
}

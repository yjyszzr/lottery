package com.dl.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.dto.DLZQBetInfoDTO;
import com.dl.param.DlJcZqMatchBetParam;

@FeignClient(value="lottery-service")
public interface ILotteryMatchService {

	@RequestMapping(path="/lottery/match/getBetInfo", method=RequestMethod.POST)
	public BaseResult<DLZQBetInfoDTO> getBetInfo(DlJcZqMatchBetParam param);
}

package com.dl.lottery.api;

import javax.validation.Valid;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.SZCPrizeDTO;
import com.dl.lottery.param.DiscoveryPageParam;

@FeignClient(value="lottery-service")
public interface ILotteryDiscoveryService {
	
	@RequestMapping(path="/dl/discoveryPage/szcDetailList", method=RequestMethod.POST)
    public BaseResult<SZCPrizeDTO> szcDetailList(@Valid @RequestBody DiscoveryPageParam param);
}

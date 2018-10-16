package com.dl.lottery.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.ActivityDTO;

@FeignClient(value="lottery-service")
public interface IArtifiPrintLotteryService {
	
	@RequestMapping(path="/lottery/artifi/tasktimer", method=RequestMethod.POST)
    public BaseResult<List<ActivityDTO>> artifiTaskTimer(@RequestBody EmptyParam emptyParam);
}

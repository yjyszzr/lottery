package com.dl.lottery.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.LogPicDetailDTO;
import com.dl.lottery.param.OrderSnParam;

@FeignClient(value="lottery-service")
public interface ILogOperationService {
	
	@RequestMapping(path="/lottery/opLog/queryLogOpByOrderSn", method=RequestMethod.POST)
    public BaseResult<LogPicDetailDTO> queryLogOpByOrderSn(@RequestBody OrderSnParam param);
}

package com.dl.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.dto.TestDemoDTO;
import com.dl.param.TestDemoParam;

@FeignClient(value = "demo-service")
public interface ITestDemoService {

	@RequestMapping(path = "/test/testInterface", method = RequestMethod.POST)
    BaseResult<TestDemoDTO> testInterface(@RequestBody TestDemoParam param);
}

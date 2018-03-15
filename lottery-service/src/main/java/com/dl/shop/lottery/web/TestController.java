package com.dl.shop.lottery.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.dto.TestDemoDTO;
import com.dl.param.TestDemoParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {
	
	@ApiOperation(value = "测试接口是否可以调通", notes = "测试接口是否可以调通")
    @PostMapping("/testInterface")
    public BaseResult<TestDemoDTO> testInterface(@RequestBody TestDemoParam param) {
		TestDemoDTO dto = new TestDemoDTO();
		dto.setTestStr("demo-service");
    	return ResultGenerator.genSuccessResult("测试接口成功", dto);
    }
	
}


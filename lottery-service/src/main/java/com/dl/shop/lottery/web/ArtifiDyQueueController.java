package com.dl.shop.lottery.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.service.ArtifiDyQueueService;
import io.swagger.annotations.ApiOperation;

/**
 * 动态队列
 * @author wht
 * @date 2018.10.16
 */
@RestController
@RequestMapping("/lottery/artifi")
public class ArtifiDyQueueController {
	private final static Logger logger = LoggerFactory.getLogger(DlArticleController.class);


	@Resource
	private ArtifiDyQueueService artifiDyQueueServlce;
	
	
	@ApiOperation(value = "发现页主页", notes = "发现页主页")
	@PostMapping("/tasktimer")
	public BaseResult<?> timerTaskSchedual(@RequestBody EmptyParam emprt) {
		artifiDyQueueServlce.onTimerExec();
		return ResultGenerator.genSuccessResult();
	}
}

package com.dl.shop.lottery.web;

import java.util.List;

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
import com.dl.lottery.param.ArtifiLotteryModifyParam;
import com.dl.lottery.param.ArtifiLotteryQueryParam;
import com.dl.shop.base.dao.DyArtifiPrintDao;
import com.dl.shop.base.dao.DyArtifiPrintImple;
import com.dl.shop.base.dao.entity.DDyArtifiPrintEntity;
import com.dl.shop.lottery.configurer.DataBaseCfg;
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
	private ArtifiDyQueueService artifiDyQueueService;
	@Resource
	private DataBaseCfg baseCfg;
	
	@ApiOperation(value = "人工分单", notes = "人工分单")
	@PostMapping("/tasktimer")
	public BaseResult<?> timerTaskSchedual(@RequestBody EmptyParam emprt) {
		logger.info("[timerTaskSchedual]" + "人工分单...");
		artifiDyQueueService.onTimerExec();
		return ResultGenerator.genSuccessResult();
	}
	
	@ApiOperation(value = "查询列表", notes = "查询列表")
	@PostMapping("/query")
	public BaseResult<?> queryOrderList(@RequestBody ArtifiLotteryQueryParam param){
		String mobile = param.getMobile();
		if(mobile == null || mobile.length() <= 0) {
			return ResultGenerator.genFailResult("手机号码不能为空");
		}
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(baseCfg);
		List<DDyArtifiPrintEntity> rList = dyArtifiDao.listAll(mobile,param.getStartId());
		return ResultGenerator.genSuccessResult(null,rList);
	}
	
	@ApiOperation(value = "更改订单状态", notes = "更改订单状态")
	@PostMapping("/modify")
	public BaseResult<?> modifyOrderStatus(@RequestBody ArtifiLotteryModifyParam params){
		String mobile = params.getMobile();
		if(mobile == null || mobile.length() <= 0) {
			return ResultGenerator.genFailResult("手机号码不能为空"); 
		}
		return artifiDyQueueService.modifyOrderStatus(mobile,params.getOrderSn(),params.getOrderStatus());
	}
	
	@ApiOperation(value = "测试登录", notes = "测试登录成功")
	@PostMapping("/testlogin")
	public BaseResult<?> testLogin(@RequestBody ArtifiLotteryQueryParam params){
		artifiDyQueueService.userLogin(params.getMobile());
		return ResultGenerator.genSuccessResult();
	}
	
	@ApiOperation(value = "测试退出登录", notes = "测试退出登录")
	@PostMapping("/testlogout")
	public BaseResult<?> testLogout(@RequestBody ArtifiLotteryQueryParam params){
		artifiDyQueueService.userLogout(params.getMobile());
		return ResultGenerator.genSuccessResult(); 
	}
}

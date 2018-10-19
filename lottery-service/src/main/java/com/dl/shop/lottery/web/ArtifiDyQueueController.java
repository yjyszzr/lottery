package com.dl.shop.lottery.web;

import java.util.ArrayList;
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
import com.dl.base.util.SessionUtil;
import com.dl.lottery.param.ArtifiLotteryDetailParam;
import com.dl.lottery.param.ArtifiLotteryModifyParam;
import com.dl.lottery.param.ArtifiLotteryQueryParam;
import com.dl.member.api.IUserService;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.UserIdRealParam;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.ManualOrderDTO;
import com.dl.order.param.OrderSnListParam;
import com.dl.shop.base.dao.DyArtifiPrintDao;
import com.dl.shop.base.dao.DyArtifiPrintImple;
import com.dl.shop.base.dao.entity.DDyArtifiPrintEntity;
import com.dl.shop.base.manager.ArtifiLoginManager;
import com.dl.shop.lottery.configurer.DataBaseCfg;
import com.dl.shop.lottery.service.ArtifiDyQueueService;
import com.dl.shop.lottery.service.ArtifiPrintLotteryUserLoginService;
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
	
	@Resource
	private IOrderService iOrderService;
	@Resource
	private ArtifiPrintLotteryUserLoginService artifiPrintLotteryUserLoginService;
	@Resource
	private IUserService iUserService;
	
	@ApiOperation(value = "人工分单", notes = "人工分单")
	@PostMapping("/tasktimer")
	public BaseResult<?> timerTaskSchedual(@RequestBody EmptyParam emprt) {
		logger.info("[timerTaskSchedual]" + "人工分单...");
		artifiDyQueueService.onTimerExec();
		return ResultGenerator.genSuccessResult();
	}
	

	@ApiOperation(value = "订单详情", notes = "订单详情")
	@PostMapping("/detail")
	public BaseResult<?> queryDetail(@RequestBody ArtifiLotteryDetailParam pp){
		int userId = SessionUtil.getUserId();
		UserIdRealParam userIdParams = new UserIdRealParam();
		userIdParams.setUserId(userId);
		BaseResult<UserDTO> bR = iUserService.queryUserInfoReal(userIdParams);
		if(!bR.isSuccess() || bR.getData() == null) {
			return ResultGenerator.genFailResult("查询用户信息失败");
		}
		String orderSn = pp.getOrderSn();
//		String mobile = pp.getMobile();
		UserDTO userDTO = bR.getData();
		String mobile = userDTO.getMobile();
		logger.info("[queryDetail]" + " mobile:" + mobile);
		if(orderSn == null || orderSn.length() <= 0) {
			return ResultGenerator.genFailResult("请输入订单号参数");
		}
		if(mobile == null || mobile.length() <= 0) {
			return ResultGenerator.genFailResult("请输入手机号");
		}
		ManualOrderDTO orderEntity = null;
		//刷新登录态 
		artifiPrintLotteryUserLoginService.updateUserStatus(mobile);
		List<String> mList = new ArrayList<String>();
		mList.add(orderSn);
		OrderSnListParam params = new OrderSnListParam();
		params.setOrderSnlist(mList);
		BaseResult<List<ManualOrderDTO>> bResult = iOrderService.getManualOrderList(params);
		if(bResult != null && bResult.isSuccess()) {
			List<ManualOrderDTO> rList = bResult.getData();
			if(rList != null && rList.size() > 0) {
				orderEntity = rList.get(0);
			}
		}
		if(orderEntity == null) {
			return ResultGenerator.genFailResult("查询订单数据失败");
		}
		return ResultGenerator.genSuccessResult("succ",orderEntity);
	}
	
	@ApiOperation(value = "查询列表", notes = "查询列表")
	@PostMapping("/query")
	public BaseResult<?> queryOrderList(@RequestBody ArtifiLotteryQueryParam param){
		int userId = SessionUtil.getUserId();
		UserIdRealParam userIdParams = new UserIdRealParam();
		userIdParams.setUserId(userId);
		BaseResult<UserDTO> bR = iUserService.queryUserInfoReal(userIdParams);
		if(bR == null || !bR.isSuccess() || bR.getData() == null) {
			return ResultGenerator.genFailResult("查询用户信息失败");
		}
		String mobile = bR.getData().getMobile();
		logger.info("[queryOrderList]" + " mobile:" + mobile);
		if(mobile == null || mobile.length() <= 0) { 
			return ResultGenerator.genFailResult("手机号码不能为空");
		}
		//如果在登录，登录态未还在生效，那么加入到分配队列
		if(!ArtifiLoginManager.getInstance().containMobile(mobile)) {
			ArtifiLoginManager.getInstance().addMobile(mobile);
		}
		//刷新登录态 
		artifiPrintLotteryUserLoginService.updateUserStatus(mobile);
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(baseCfg);
		List<DDyArtifiPrintEntity> rList = dyArtifiDao.listAll(mobile,param.getStartId());
		return ResultGenerator.genSuccessResult("succ",rList);
	}
	
	@ApiOperation(value = "更改订单状态", notes = "更改订单状态")
	@PostMapping("/modify")
	public BaseResult<?> modifyOrderStatus(@RequestBody ArtifiLotteryModifyParam params){
		int userId = SessionUtil.getUserId();
		UserIdRealParam userIdParams = new UserIdRealParam();
		userIdParams.setUserId(userId);
		BaseResult<UserDTO> bR = iUserService.queryUserInfoReal(userIdParams);
		if(bR == null || !bR.isSuccess() || bR.getData() == null) {
			return ResultGenerator.genFailResult("查询用户信息失败");
		}
//		String mobile = params.getMobile();
		String mobile = bR.getData().getMobile();
		if(mobile == null || mobile.length() <= 0) {
			return ResultGenerator.genFailResult("手机号码不能为空"); 
		}
		return artifiDyQueueService.modifyOrderStatus(userId,mobile,params.getOrderSn(),params.getOrderStatus());
	}
	
	@ApiOperation(value = "测试登录", notes = "测试登录成功")
	@PostMapping("/testlogin")
	public BaseResult<?> testLogin(@RequestBody ArtifiLotteryQueryParam params){
		artifiDyQueueService.userLogin(params.getMobile(),null);
		return ResultGenerator.genSuccessResult();
	}
	
	@ApiOperation(value = "测试退出登录", notes = "测试退出登录")
	@PostMapping("/testlogout")
	public BaseResult<?> testLogout(@RequestBody ArtifiLotteryQueryParam params){
		artifiDyQueueService.userLogout(params.getMobile(),null);
		return ResultGenerator.genSuccessResult(); 
	}
}

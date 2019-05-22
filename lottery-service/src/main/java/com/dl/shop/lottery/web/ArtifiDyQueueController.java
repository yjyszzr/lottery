package com.dl.shop.lottery.web;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.EmojiFilter;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.enums.LotteryResultEnum;
import com.dl.lottery.enums.MemberEnums;
import com.dl.lottery.param.ArtifiLotteryDetailParam;
import com.dl.lottery.param.ArtifiLotteryModifyParamV2;
import com.dl.lottery.param.ArtifiLotteryQueryParam;
import com.dl.lottery.param.ArtifiLotteryQueryParamV2;
import com.dl.member.api.IUserService;
import com.dl.member.dto.MediaTokenDTO;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.MediaTokenParam;
import com.dl.member.param.UserIdRealParam;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.ManualLottoOrderDetailDTO;
import com.dl.order.dto.ManualOrderDTO;
import com.dl.order.param.OrderSnListParam;
import com.dl.shop.base.dao.DyArtifiPrintDao;
import com.dl.shop.base.dao.DyArtifiPrintImple;
import com.dl.shop.base.dao.entity.DDyArtifiPrintEntity;
import com.dl.shop.base.manager.ArtifiLoginManager;
import com.dl.shop.lottery.configurer.DataBaseCfg;
import com.dl.shop.lottery.entity.DlManalLottoOrderDetailDTO;
import com.dl.shop.lottery.entity.DlManalOrderDetailDTO;
import com.dl.shop.lottery.model.DlArtifiPrintLottery;
import com.dl.shop.lottery.model.DlXNWhiteList;
import com.dl.shop.lottery.service.ArtifiDyQueueService;
import com.dl.shop.lottery.service.ArtifiPrintLotteryUserLoginService;
import com.dl.shop.lottery.service.DlXNWhiteListService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态队列1
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
	@Resource
	private DlXNWhiteListService dlXNWhiteListService;
	
	@ApiOperation(value = "人工分单", notes = "人工分单")
	@PostMapping("/tasktimer")
	public BaseResult<?> timerTaskSchedual(@RequestBody EmptyParam emprt) {
//		logger.info("[timerTaskSchedual]" + "人工分单...");
//		artifiDyQueueService.onTimerExec();
		return ResultGenerator.genSuccessResult();
	}
	

	@ApiOperation(value = "订单详情", notes = "订单详情")
	@PostMapping("/lottoDetail")
	public BaseResult<DlManalLottoOrderDetailDTO> queryLottoDetail(@RequestBody ArtifiLotteryDetailParam pp){
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
		logger.info("[queryLottoDetail]" + " mobile:" + mobile);
		if(orderSn == null || orderSn.length() <= 0) {
			return ResultGenerator.genFailResult("请输入订单号参数");
		}
		if(mobile == null || mobile.length() <= 0) {
			return ResultGenerator.genFailResult("请输入手机号");
		}
		//判断是否在白名单内
		Condition c = new Condition(DlXNWhiteList.class);
		c.createCriteria().andEqualTo("mobile",mobile);
		List<DlXNWhiteList> xnWhiteListList = dlXNWhiteListService.findByCondition(c);
		if (xnWhiteListList.size() == 0) {
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
		}
		DlArtifiPrintLottery dlArtifiPrintLottery = artifiDyQueueService.selectArtifiPrintLotteryByOrderSn(orderSn);
		Integer storeId = null;
		if(dlArtifiPrintLottery != null) {
			storeId = dlArtifiPrintLottery.getStoreId();
		}
		logger.info("[queryLottoDetail]" + " storeId:" + storeId + " printLottery:" + dlArtifiPrintLottery);
		ManualLottoOrderDetailDTO mLottoOrderDetailDTO = null;
		List<String> orderSnList = new ArrayList<String>();
		orderSnList.add(orderSn);
		OrderSnListParam p = new OrderSnListParam();
		p.setOrderSnlist(orderSnList);
		p.setStoreId(storeId);
		BaseResult<ManualLottoOrderDetailDTO> baseResult = iOrderService.getManualLottoOrderList(p);
		if(!baseResult.isSuccess()) {
			return ResultGenerator.genResult(baseResult.getCode(),baseResult.getMsg());
		}
		mLottoOrderDetailDTO = baseResult.getData();
		//获取多媒体token
		MediaTokenParam mediaTokenParams = new MediaTokenParam();
		mediaTokenParams.setType(0);
		BaseResult<MediaTokenDTO> baseR = iUserService.getMediaTokenInfo(mediaTokenParams);
		if(baseR == null || !baseR.isSuccess() || baseR.getData() == null) {
			return ResultGenerator.genResult(MemberEnums.MEDIA_TOKEN_FAIL.getcode(),MemberEnums.MEDIA_TOKEN_FAIL.getMsg());
		}
		MediaTokenDTO mediaEntity = baseR.getData();
		logger.info("[queryLottoDetail]" + " fileName:" + mediaEntity.fileName);
		DlManalLottoOrderDetailDTO detailDTO = new DlManalLottoOrderDetailDTO();
		detailDTO.setMediaToken(mediaEntity);
		detailDTO.setDetail(mLottoOrderDetailDTO);
		return ResultGenerator.genSuccessResult("succ",detailDTO);
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
		//判断是否在白名单内
		Condition c = new Condition(DlXNWhiteList.class);
		c.createCriteria().andEqualTo("mobile",mobile);
		List<DlXNWhiteList> xnWhiteListList = dlXNWhiteListService.findByCondition(c);
		if (xnWhiteListList.size() == 0) {
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
		}
		DlArtifiPrintLottery dlArtifiPrintLottery = artifiDyQueueService.selectArtifiPrintLotteryByOrderSn(orderSn);
		Integer storeId = null;
		if(dlArtifiPrintLottery != null) {
			storeId = dlArtifiPrintLottery.getStoreId();
		}
		logger.info("[queryDetail]" + " storeId:" + storeId + " printLottery:" + dlArtifiPrintLottery);
		ManualOrderDTO orderEntity = null;
		//刷新登录态 
		artifiPrintLotteryUserLoginService.updateUserStatus(mobile);
		List<String> mList = new ArrayList<String>();
		mList.add(orderSn);
		OrderSnListParam params = new OrderSnListParam();
		params.setOrderSnlist(mList);
		params.setStoreId(storeId);
		BaseResult<List<ManualOrderDTO>> bResult = iOrderService.getManualOrderList(params);
		if(bResult != null && bResult.isSuccess()) {
			List<ManualOrderDTO> rList = bResult.getData();
			if(rList != null && rList.size() > 0) {
				orderEntity = rList.get(0);
			}
		}
		if(orderEntity == null) {
			return ResultGenerator.genResult(MemberEnums.QUERY_ORDER_FAIL.getcode(),MemberEnums.QUERY_ORDER_FAIL.getMsg());
		}
		//获取多媒体token
		MediaTokenParam mediaTokenParams = new MediaTokenParam();
		mediaTokenParams.setType(0);
		BaseResult<MediaTokenDTO> baseR = iUserService.getMediaTokenInfo(mediaTokenParams);
		if(baseR == null || !baseR.isSuccess() || baseR.getData() == null) {
			return ResultGenerator.genResult(MemberEnums.MEDIA_TOKEN_FAIL.getcode(),MemberEnums.MEDIA_TOKEN_FAIL.getMsg());
		}
		MediaTokenDTO mediaEntity = baseR.getData();
		logger.info("[queryDetail]" + " fileName:" + mediaEntity.fileName);
		DlManalOrderDetailDTO entity = new DlManalOrderDetailDTO();
		entity.setDetail(orderEntity);
		entity.setMediaToken(mediaEntity);
		return ResultGenerator.genSuccessResult("succ",entity);
	}
	
	@ApiOperation(value = "查询列表", notes = "查询列表")
	@PostMapping("/queryV2")
	public BaseResult<List<DDyArtifiPrintEntity>> queryOrderListV2(@RequestBody ArtifiLotteryQueryParamV2 param){
		Integer userId = SessionUtil.getUserId();
		if(userId == null) {
			return ResultGenerator.genResult(MemberEnums.USER_LOGIN_TIPS.getcode(),MemberEnums.USER_LOGIN_TIPS.getMsg());
		}
		UserIdRealParam userIdParams = new UserIdRealParam();
		userIdParams.setUserId(userId);
		BaseResult<UserDTO> bR = iUserService.queryUserInfoReal(userIdParams);
		if(bR == null || !bR.isSuccess() || bR.getData() == null) {
			return ResultGenerator.genResult(MemberEnums.QUERY_USER_FAIL.getcode(),MemberEnums.QUERY_USER_FAIL.getMsg());
		}
		String mobile = bR.getData().getMobile();
		logger.info("[queryOrderList]" + " mobile:" + mobile);
		if(mobile == null || mobile.length() <= 0) { 
			return ResultGenerator.genFailResult("手机号码不能为空");
		}
		//判断是否在白名单内
		Condition c = new Condition(DlXNWhiteList.class);
		c.createCriteria().andEqualTo("mobile",mobile);
		List<DlXNWhiteList> xnWhiteListList = dlXNWhiteListService.findByCondition(c);
		if (xnWhiteListList.size() == 0) {
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
		}
		//进入到分单逻辑
		if(param.getType() != null && param.getType() == 1) {
			DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(baseCfg);

			//检查是否全部都操作完
			boolean isAll = dyArtifiDao.isOperationAll(mobile);
			if(isAll) {
				dyArtifiDao.clearAll(mobile);
			}
			logger.info("[queryOrderListV2]" + " getType -> " + param.getType());
			artifiDyQueueService.allocLotteryV2(mobile);
		}
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(baseCfg);
		List<DDyArtifiPrintEntity> rList = dyArtifiDao.listAll(mobile,0);
        if(CollectionUtils.isEmpty(rList)){
            return ResultGenerator.genResult(LotteryResultEnum.DB_NO_DATA.getCode(),"暂无订单，请稍后尝试");
        }
        filterList(rList);
		return ResultGenerator.genSuccessResult("succ",rList);
	}
	
	private void filterList(List<DDyArtifiPrintEntity> rList) {
		if(rList != null) {
			for(DDyArtifiPrintEntity entity : rList){
				int classifyId = entity.lotteryClassifyId;
				if(classifyId == 1) {
					entity.logo = "https://szcq-icon.oss-cn-beijing.aliyuncs.com/jingzu.png";
				}else if(classifyId == 2) {
					entity.logo = "https://szcq-icon.oss-cn-beijing.aliyuncs.com/daletou.png";
				}
			}
		}
	}
	
	@ApiOperation(value = "查询列表", notes = "查询列表")
	@PostMapping("/query")
	public BaseResult<?> queryOrderList(@RequestBody ArtifiLotteryQueryParam param){
		Integer userId = SessionUtil.getUserId();
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
		//判断是否在白名单内
		Condition c = new Condition(DlXNWhiteList.class);
		c.createCriteria().andEqualTo("mobile",mobile);
		List<DlXNWhiteList> xnWhiteListList = dlXNWhiteListService.findByCondition(c);
		if (xnWhiteListList.size() == 0) {
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
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
	@PostMapping("/modifyV2")
	public BaseResult<?> modifyOrderStatusV2(@RequestBody ArtifiLotteryModifyParamV2 params){
		int userId = SessionUtil.getUserId();
		UserIdRealParam userIdParams = new UserIdRealParam();
		userIdParams.setUserId(userId);
		BaseResult<UserDTO> bR = iUserService.queryUserInfoReal(userIdParams);
		if(bR == null || !bR.isSuccess() || bR.getData() == null) {
			return ResultGenerator.genResult(MemberEnums.QUERY_USER_FAIL.getcode(),MemberEnums.QUERY_USER_FAIL.getMsg());
		}
//		String mobile = params.getMobile();
		String mobile = bR.getData().getMobile();
		if(mobile == null || mobile.length() <= 0) {
			return ResultGenerator.genResult(MemberEnums.QUERY_MOBILE_EMPTY.getcode(),MemberEnums.QUERY_MOBILE_EMPTY.getMsg()); 
		}
		if(params.getOrderStatus() <= 0) {
			return ResultGenerator.genResult(MemberEnums.ORDER_STATUS_FAILURE.getcode(),MemberEnums.ORDER_STATUS_FAILURE.getMsg());
		}
		//判断是否在白名单内
		Condition c = new Condition(DlXNWhiteList.class);
		c.createCriteria().andEqualTo("mobile",mobile);
		List<DlXNWhiteList> xnWhiteListList = dlXNWhiteListService.findByCondition(c);
		if (xnWhiteListList.size() == 0) {
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
		}
		//过滤表情content
		String rfailMsg = null;
		String failMsg = null;
		if(!StringUtils.isEmpty(params.getFailMsg())) {
			failMsg = params.getFailMsg();
			rfailMsg = EmojiFilter.emoji2Unicode(failMsg);
		}

		logger.info("[modifyOrderStatusV2]" + " failMsg:" + failMsg + " rFailMsg:" + rfailMsg);
		return artifiDyQueueService.modifyOrderStatusV2(userId,mobile,params.getOrderSn(),params.getOrderStatus(),params.getPicUrl(),rfailMsg);
	}
	
//	@ApiOperation(value = "更改订单状态", notes = "更改订单状态")
//	@PostMapping("/modify")
//	public BaseResult<?> modifyOrderStatus(@RequestBody ArtifiLotteryModifyParam params){
//		int userId = SessionUtil.getUserId();
//		UserIdRealParam userIdParams = new UserIdRealParam();
//		userIdParams.setUserId(userId);
//		BaseResult<UserDTO> bR = iUserService.queryUserInfoReal(userIdParams);
//		if(bR == null || !bR.isSuccess() || bR.getData() == null) {
//			return ResultGenerator.genFailResult("查询用户信息失败");
//		}
////		String mobile = params.getMobile();
//		String mobile = bR.getData().getMobile();
//		if(mobile == null || mobile.length() <= 0) {
//			return ResultGenerator.genFailResult("手机号码不能为空"); 
//		}
//		if(params.getOrderStatus() <= 0) {
//			return ResultGenerator.genResult(MemberEnums.ORDER_STATUS_FAILURE.getcode(),MemberEnums.ORDER_STATUS_FAILURE.getMsg());
//		}
//		//判断是否在白名单内
//		Condition c = new Condition(DlXNWhiteList.class);
//		c.createCriteria().andEqualTo("mobile",mobile);
//		List<DlXNWhiteList> xnWhiteListList = dlXNWhiteListService.findByCondition(c);
//		if (xnWhiteListList.size() == 0) {
//			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
//		}
//		return artifiDyQueueService.modifyOrderStatus(userId,mobile,params.getOrderSn(),params.getOrderStatus());
//	}
}

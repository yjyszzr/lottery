package com.dl.shop.lottery.service;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.DateUtilNew;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.OrderDTO;
import com.dl.order.param.OrderPicParam;
import com.dl.order.param.OrderSnParam;
import com.dl.shop.base.dao.DyArtifiPrintDao;
import com.dl.shop.base.dao.DyArtifiPrintImple;
import com.dl.shop.base.dao.entity.DDyArtifiPrintEntity;
import com.dl.shop.base.manager.ArtifiLoginManager;
import com.dl.shop.lottery.configurer.DataBaseCfg;
import com.dl.shop.lottery.dao.DlArtifiPrintLotteryMapper;
import com.dl.shop.lottery.dao.DlOpLogMapper;
import com.dl.shop.lottery.model.DlArtifiPrintLottery;
import com.dl.shop.lottery.model.DlOpLog;
import com.dl.store.api.IStoreUserMoneyService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(value = "transactionManager2")
@Slf4j
public class ArtifiDyQueueService{
	private final static Logger logger = LoggerFactory.getLogger(DlDiscoveryPageService.class);
	
	@Resource
	private DataBaseCfg dataBaseCfg;
	
	@Resource
	private DlArtifiPrintLotteryMapper dlArtifiPrintMapper;
	
	@Resource
	private DlOpLogMapper dlOpMapper;
	@Resource
	private IOrderService iOrderService;
	@Resource
	private IStoreUserMoneyService iStoreMoneyService;

	@Resource
	private LotteryPrintService lotteryPrintService;
	
	private final int QUEUE_SIZE = 30;
	
	/**
	 * 用户登录
	 * @param obj
	 */
	public void userLogin(String mobile,List<String> mobileList) {
		ArtifiLoginManager.getInstance().onLogin(mobile);
		if(mobileList != null) {
			ArtifiLoginManager.getInstance().setList(mobileList);
		}
		//该用户queue table创建
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
		if(dyArtifiDao.isDyQueueExist(mobile) <= 0) {
			dyArtifiDao.createDyQueue(mobile);
			logger.info("[userLogin]" + " uid:" + mobile + " table not exist");
		}else {
			logger.info("[userLogin]" + " uid:" + mobile + " table exist");
		}
		//v2版本登录成功不进行分配订单
		
//		//查看有未分配的订单，立刻给分配
//		List<DlArtifiPrintLottery> rSumList = dlArtifiPrintMapper.listLotteryTodayUnAlloc();
//		List<DDyArtifiPrintEntity> rList = dyArtifiDao.listAll(mobile,0);
//		logger.info("[userLogin]" + "今日未分配订单:" + rSumList.size() + " user:" + mobile + " 现有订单数:" + rList.size());
//		int cnt = QUEUE_SIZE - rList.size();
//		if(cnt > 0) {
//			List<DlArtifiPrintLottery> allocList = allocLottery(dyArtifiDao,mobile,rSumList, cnt);
//			if(allocList != null && allocList.size() > 0) {
//				for(DlArtifiPrintLottery entity : allocList) {
//					//总的队列移除
//					rSumList.remove(entity);
//					//更改已分配的状态
//					entity.setOperationStatus(DlArtifiPrintLottery.OPERATION_STATUS_ALLOCATED);
//					//操作人
//					entity.setAdminName(mobile);
//					logger.info("[userLogin]" + " update orderSn:" + entity.getOrderSn() + " adminName:" + mobile + " opStatus:" + entity.getOperationStatus());
//					dlArtifiPrintMapper.updateArtifiLotteryPrint(entity);
//				}
//			}
//		}
	}
	
	/**
	 * 退出登录
	 * @param obj
	 */
	public void userLogout(String mobile,List<String> mobileList){
		logger.info("[userLogout]" + " uid:" + mobile + " 退出登录.. 在线人数:" + mobileList.size());
		ArtifiLoginManager.getInstance().onLogout(mobile);
		if(mobileList != null) {
			ArtifiLoginManager.getInstance().setList(mobileList);
		}
//		//获取该用户队列内容
//		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
//		List<DDyArtifiPrintEntity> rList = dyArtifiDao.listAll(mobile,0);
//		for(DDyArtifiPrintEntity dyArtiPrintEntity : rList) {
//			//查询到该订单信息
//			DlArtifiPrintLottery dlArtifiPrintLottery = new DlArtifiPrintLottery();
//			dlArtifiPrintLottery.setOrderSn(dyArtiPrintEntity.orderSn);
//			List<DlArtifiPrintLottery> list = dlArtifiPrintMapper.selectArtifiLotteryPrintByOrderSn(dlArtifiPrintLottery);
//			//该订单信息回收到总池
//			if(list != null && list.size() > 0) {
//				DlArtifiPrintLottery dlEntity = list.get(0);
//				dlEntity.setOperationStatus(DlArtifiPrintLottery.OPERATION_STATUS_INIT);
//				dlEntity.setAdminName(mobile);
//				dlArtifiPrintMapper.updateArtifiLotteryPrint(dlEntity);
//				logger.info("[userLogout]" + " 该订单:" + dyArtiPrintEntity.orderSn + " 回收到总池... uid:" + mobile);
//				//移除该用户队列的该ordersn订单数据
//				dyArtifiDao.deleteOrderSn(mobile,dyArtiPrintEntity.orderSn);
//			}
//		}
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
		List<DDyArtifiPrintEntity> rList = dyArtifiDao.listAll(mobile,0);
		if(rList.size() <= 0) {
			dyArtifiDao.dropTable(mobile);
		}
	}

	/**
	 * 轮训查询总队列
	 */
	public void onTimerExec() {
		logger.info("[onTimerExec]");
		//获取在线人数据
		List<String> userList = ArtifiLoginManager.getInstance().getCopyList();
		//获取今天未分配的订单
		List<DlArtifiPrintLottery> rSumList = dlArtifiPrintMapper.listLotteryTodayUnAlloc();
		logger.info("[onTimerExec]" + "获取今日未分配订单:" + rSumList.size() + " 在线人数:" + userList.size());
		for(int i = 0;i < userList.size();i++) {
			logger.info("[onTimerExec]" + " uid:" + userList.get(i));
		}
		logger.info("==================================================");
		if(rSumList != null && rSumList.size() > 0) {
			DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
			if(userList != null && userList.size() > 0) {
				Map<String,List<DDyArtifiPrintEntity>> mMap = new HashMap<String,List<DDyArtifiPrintEntity>>();
				for(String uid : userList) {
					List<DDyArtifiPrintEntity> mLotteryList = dyArtifiDao.listAll(uid,0);
					mMap.put(uid,mLotteryList);
				}
				//开始分配订单
				for(int i = 0;rSumList.size() > 0 && i < userList.size();i++) {
					//获取随机一个用户
					String mobile = getRandomItem(userList);
					userList.remove(mobile);
					//该用户的队列大小
					List<DDyArtifiPrintEntity> mLotteryList = mMap.get(mobile);
					//如果不够30个，填充满30个
					if(rSumList.size() > 0 && mLotteryList.size() < QUEUE_SIZE) {
						int size = QUEUE_SIZE - mLotteryList.size();
						List<DlArtifiPrintLottery> allocList = allocLottery(dyArtifiDao,mobile,rSumList,size);
						if(allocList != null && allocList.size() > 0) {
							for(DlArtifiPrintLottery entity : allocList) {
								//总的队列移除
								rSumList.remove(entity);
								//更改已分配的状态
								entity.setOperationStatus(DlArtifiPrintLottery.OPERATION_STATUS_ALLOCATED);
								//操作人
								entity.setAdminName(mobile);
								logger.info("[onTimerExec]" + " update orderSn:" + entity.getOrderSn() + " adminName:" + mobile + " opStatus:" + entity.getOperationStatus());
								dlArtifiPrintMapper.updateArtifiLotteryPrint(entity);
							}
						}
					}
				}
			}
		}
	}
	
	private synchronized List<DlArtifiPrintLottery> allocLottery(DyArtifiPrintDao dyArtifiDao,String uid,List<DlArtifiPrintLottery> rList,int size){
		List<DlArtifiPrintLottery> mList = new ArrayList<DlArtifiPrintLottery>();
		int s = rList.size() > size ? size : rList.size();
		logger.info("[allocLottery]" + " s:" + s);
		for(int i = 0;i < s ;i++){
			DlArtifiPrintLottery entity = rList.get(i);
			//分配出数据
			mList.add(entity);
		}
		return mList;
	}
	
	private String getRandomItem(List<String> mList){
		String r = null;
		if(mList != null) {
			int index = (int) (Math.random() * mList.size());
			r = mList.get(index);
		}
		return r;
	}
	
	public BaseResult<?> modifyOrderStatusV2(int userId,String mobile,String orderSn,Integer orderStatus,String picUrl,String failMsg){
		//删除队列数据
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
		int cnt = dyArtifiDao.updateOrderStatus(mobile,orderSn,orderStatus);
		logger.info("[modifyOrderStatus]" + " cnt:" + cnt);
		//状态回写到总订单池
		DlArtifiPrintLottery dlArtifiPrintLottery = new DlArtifiPrintLottery();
		dlArtifiPrintLottery.setOrderSn(orderSn);
		List<DlArtifiPrintLottery> list = dlArtifiPrintMapper.selectArtifiLotteryPrintByOrderSn(dlArtifiPrintLottery);
		DlArtifiPrintLottery printLottery = null;
		if(list != null && list.size() > 0) {
			printLottery = list.get(0);
			printLottery.setOrderSn(orderSn);
			printLottery.setOrderStatus(orderStatus.byteValue());
			printLottery.setAdminName(mobile);
			printLottery.setAdminId(userId);
			printLottery.setOperationStatus(DlArtifiPrintLottery.OPERATION_STATUS_ALLOCATED);
			printLottery.setOperationTime(DateUtil.getCurrentTimeLong());
			if(orderStatus != null && orderStatus == 1) {//出票成功
				printLottery.setStatisticsPrint(0);
			}
			dlArtifiPrintMapper.updateArtifiLotteryPrint(printLottery); 
		}
		Integer storeId = printLottery.getStoreId();
		//图片回写到表中,成功失败状态回写
		OrderPicParam orderPicParams = new OrderPicParam();
		orderPicParams.setOrderSn(orderSn);
		orderPicParams.setOrderPic(picUrl);
		orderPicParams.setStoreId(storeId);
		iOrderService.saveOrderPicByOrderSn(orderPicParams);
				
		//获取订单金额
		OrderSnParam orderSnParams = new OrderSnParam();
		orderSnParams.setOrderSn(orderSn);
		orderSnParams.setStoreId(storeId);
		BaseResult<OrderDTO> baseR = iOrderService.getOrderInfoByOrderSn(orderSnParams);
		BigDecimal moneyPaid = null;
		Integer lotteryClassifyId = 0;
		if(baseR.isSuccess() && baseR.getData() != null) {
			moneyPaid = baseR.getData().getMoneyPaid();
			lotteryClassifyId = baseR.getData().getLotteryClassifyId();
		}
		//检查是否全部都操作完
		boolean isAll = dyArtifiDao.isOperationAll(mobile);
		if(isAll) {
			dyArtifiDao.clearAll(mobile);
		}
		
		try {
			//若是商户订单号，则主动通知商户出票结果
			String merchantOrderSn = baseR.getData().getMerchantOrderSn();
			if(!StringUtils.isEmpty(merchantOrderSn)){
//				lotteryPrintService.notifyPrintResultToMerchant("http://123.57.34.133:8080/merchant/notify",merchantOrderSn);
				lotteryPrintService.notifyPrintResultToMerchant("http://app.shoumiba.cn/api/callback/ticket/status",merchantOrderSn);
				logger.info("modifyOrderStatusV2出票成功");
			}
		} catch (Exception e) {
			log.info("回调通知失败订单："+orderSn);
			e.printStackTrace();
		}
		
		//添加日志,去重判断
		DlOpLog dlOpLog = dlOpMapper.queryLogByOrderSn(orderSn);
		if(dlOpLog == null || (dlOpLog != null && dlOpLog.getType()!=2)) {
			DlOpLog log = new DlOpLog();
			log.setAddTime(DateUtil.getCurrentTimeLong());
			log.setPhone(mobile);
			log.setType(2);
			log.setOpType(orderStatus);
			log.setOrderSn(orderSn);
			log.setPic(picUrl);
			log.setFailMsg(failMsg);
			log.setMoneyPaid(moneyPaid);
			log.setLotteryClassifyId(lotteryClassifyId);
			log.setStoreId(storeId);
			dlOpMapper.insert(log);
		}

		//如果是第三方的订单,通知第三方

		//出票失败，订单回滚需要客服手动进行回滚
//		if(orderStatus == 2) {	//出票失败
//			OrderDTO orderDTO = null;
//			OrderSnParam p = new OrderSnParam();
//			p.setOrderSn(orderSn);
//			p.setStoreId(storeId);
//			BaseResult<OrderDTO> bResultOrder = iOrderService.getOrderInfoByOrderSn(p);
//			if(bResultOrder.isSuccess()) {
//				orderDTO = bResultOrder.getData();
//			}
//			if(orderDTO != null && orderDTO.getSurplus() != null && orderDTO.getSurplus().doubleValue() > 0) {
//				//订单回滚
//				logger.info("[modifyOrderStatusV2]" + " 余额支付订单回滚开始");
//				OrderRollBackParam rollBackParams = new OrderRollBackParam();
//				rollBackParams.setOrderSn(orderSn);
//				BaseResult<Object> bR = iStoreMoneyService.orderRollBack(rollBackParams);
//				logger.info("[modifyOrderStatusV2]" + " result:" + bR.getData());
//			}
//		}


		return ResultGenerator.genSuccessResult("succ");
	}



	
	/**
	 * 更改订单状态
	 * @param uid
	 * @param orderSn
	 * @param status
	 * @return
	 */
//	public BaseResult<?> modifyOrderStatus(int userId,String mobile,String orderSn,int orderStatus){
//		//删除队列数据
//		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
//		int cnt = dyArtifiDao.delData(mobile,orderSn);
//		logger.info("[modifyOrderStatus]" + " cnt:" + cnt);
//		//回收到主池，更改主池队列状态
//		//查询到该订单信息
//		DlArtifiPrintLottery dlArtifiPrintLottery = new DlArtifiPrintLottery();
//		dlArtifiPrintLottery.setOrderSn(orderSn);
//		List<DlArtifiPrintLottery> list = dlArtifiPrintMapper.selectArtifiLotteryPrintByOrderSn(dlArtifiPrintLottery);
//		DlArtifiPrintLottery printLottery = null;
//		if(list != null && list.size() > 0) {
//			printLottery = list.get(0);
//			printLottery.setOrderSn(orderSn);
//			printLottery.setOrderStatus((byte)orderStatus);
//			printLottery.setAdminName(mobile);
//			printLottery.setAdminId(userId);
//			printLottery.setOperationStatus(DlArtifiPrintLottery.OPERATION_STATUS_ALLOCATED);
//			printLottery.setOperationTime(DateUtil.getCurrentTimeLong());
//			dlArtifiPrintMapper.updateArtifiLotteryPrint(printLottery);
//		}
//		//添加日志
//		DlOpLog log = new DlOpLog();
//		log.setAddTime(DateUtil.getCurrentTimeLong());
//		log.setPhone(mobile);
//		log.setType(2);
//		log.setOpType(orderStatus);
//		log.setOrderSn(orderSn);
//		dlOpMapper.insert(log);
//		return ResultGenerator.genSuccessResult();
//	}

	public DlArtifiPrintLottery selectArtifiPrintLotteryByOrderSn(String orderSn) {
		DlArtifiPrintLottery rPrintLottery = null;
		DlArtifiPrintLottery printLottery = new DlArtifiPrintLottery();
		printLottery.setOrderSn(orderSn);
		List<DlArtifiPrintLottery> rList = dlArtifiPrintMapper.selectArtifiLotteryPrintByOrderSn(printLottery);
		if(rList != null && rList.size() > 0) {
			rPrintLottery = rList.get(0);
		}
		return rPrintLottery;
	}
	
	/***
	 * 分单逻辑v2
	 * @param mobile
	 */
	public synchronized void allocLotteryV2(String mobile) {
		try {
			logger.info("[allocLotteryV2]");
			DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
			List<DDyArtifiPrintEntity> rList = dyArtifiDao.listAll(mobile, 0);
			logger.info("[allocLotteryV2]" + " rList.size:" + rList.size());
			if (rList.size() <= 0) {
				List<DlArtifiPrintLottery> rSumList = null;
				rSumList = dlArtifiPrintMapper.listLotteryTodayUnAlloc();
				//141手机号规则
				//			if("18182506141".equals(mobile)) {
				//				rSumList = dlArtifiPrintMapper.listLotteryTodayUnAlloc();
				//				logger.info("[allocLotteryV2]" + " 18182506141分配" + rSumList.size() + "个订单");
				//			}else {
				//				rSumList = dlArtifiPrintMapper.listLotteryTodayUnAllocNoLotto();
				//				logger.info("[allocLotteryV2]" + " 普通手机号分配:" + rSumList.size()+"个订单");
				//			}
				List<DlArtifiPrintLottery> allocList = allocLottery(dyArtifiDao, mobile, rSumList, QUEUE_SIZE);
				logger.info("[allocLotteryV2]" + " 今日未分配订单个数:" + rSumList.size() + " 分配订单给:" + mobile + "订单个数:" + allocList.size());
				//先批量进行更改订单状态

				List<String> preUpdateOrders = new ArrayList<>();
				if (allocList != null && allocList.size() > 0) {
					for (DlArtifiPrintLottery entity : allocList) {
						//更改已分配的状态
						entity.setOperationStatus(DlArtifiPrintLottery.OPERATION_STATUS_ALLOCATED);
                        entity.setUpdateTime(DateUtilNew.getCurrentTimeLong());
						entity.setAdminName(mobile);//操作人
						logger.info("[userLogin]" + " update orderSn:" + entity.getOrderSn() + " adminName:" + mobile + " opStatus:" + entity.getOperationStatus()+ " updateTime:"+DateUtilNew.getCurrentTimeLong());
						int updateRst = dlArtifiPrintMapper.updateArtifiLotteryPrint(entity);
						if(updateRst == 1){
							preUpdateOrders.add(entity.getOrderSn());
						}
					}
				}
				//然后进行分单到该人手中
				dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);

				List<String> preAddOrders = new ArrayList<>();
				if (allocList != null && allocList.size() > 0) {
					for (DlArtifiPrintLottery entity : allocList) {
						int lotteryClassifyId = entity.getLotteryClassifyId();
						String orderSn = entity.getOrderSn();
						DDyArtifiPrintEntity dEntity = new DDyArtifiPrintEntity();
						dEntity.orderSn = orderSn;
						dEntity.status = 0;
						dEntity.setLotteryClassifyId(lotteryClassifyId);
						int addRst = dyArtifiDao.addDyArtifiPrintInfo(mobile, dEntity);
						if(addRst == 1){
							preAddOrders.add(orderSn);
						}
					}
				}

				List<String> diff = preUpdateOrders.stream().filter(item -> !preAddOrders.contains(item)).collect(Collectors.toList());
				if(diff.size() > 0){
					logger.info("已经轮寻的订单号包括:"+preUpdateOrders.toString());
					logger.info("已经分配的订单号包括:"+preAddOrders.toString());
					logger.info("差异订单号包括:"+diff.toString());
				}
			}
		}catch(Throwable throwable){
			logger.error("---------------关键的分单的时候异常------------:"+throwable.getStackTrace());
		}
	}
	/***
	 * 分单逻辑v2
	 * @param mobile 当前用户手机号
	 */
	public synchronized void allocLotteryV2BySelect(String mobile,Integer pageSize) {
		try {
			logger.info("[allocLotteryV2BySelect]");
			DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
			List<DDyArtifiPrintEntity> rList = dyArtifiDao.listAll(mobile, 0);
			logger.info("[allocLotteryV2BySelect]" + " rList.size:" + rList.size());
			if (rList.size() <= 0) {
				List<DlArtifiPrintLottery> allocList = new ArrayList<DlArtifiPrintLottery>();
				List<DlArtifiPrintLottery> rSumList = new ArrayList<DlArtifiPrintLottery>();
				if(pageSize!=null && pageSize==5) {//单独app
					if("13722300002".equals(mobile)) {//航天城店 新用户
						logger.info("13722300002_allocLotteryV2BySelect查询分单情况:mobile="+mobile);
						rSumList = dlArtifiPrintMapper.listLotteryTodayUnAllocByNewUser(); //新用户分配给02店铺
						allocList = allocLottery(dyArtifiDao, mobile, rSumList, 5);
					}
				}else {
					if("13722300001".equals(mobile)) {//圣和店 老用户
						logger.info("13722300001_allocLotteryV2BySelect查询分单情况:mobile="+mobile);
						rSumList = dlArtifiPrintMapper.listLotteryTodayUnAllocByOldUser(); //老用户分配给01店铺
//						rSumList = dlArtifiPrintMapper.listLotteryTodayUnAlloc();//全部给01店铺
						allocList = allocLottery(dyArtifiDao, mobile, rSumList, QUEUE_SIZE);
					}else if("13722300002".equals(mobile)) {//航天城店 新用户
						logger.info("13722300002_allocLotteryV2BySelect查询分单情况:mobile="+mobile);
						rSumList = dlArtifiPrintMapper.listLotteryTodayUnAllocByNewUser(); //新用户分配给02店铺
						allocList = allocLottery(dyArtifiDao, mobile, rSumList, QUEUE_SIZE);
					}
				}
				
				//141手机号规则 
				//			if("18182506141".equals(mobile)) {
				//				rSumList = dlArtifiPrintMapper.listLotteryTodayUnAlloc();
				//				logger.info("[allocLotteryV2]" + " 18182506141分配" + rSumList.size() + "个订单");
				//			}else {
				//				rSumList = dlArtifiPrintMapper.listLotteryTodayUnAllocNoLotto();
				//				logger.info("[allocLotteryV2]" + " 普通手机号分配:" + rSumList.size()+"个订单");
				//			}
				logger.info("[allocLotteryV2BySelect]" + " 今日未分配订单个数:" + rSumList.size() + " 分配订单给:" + mobile + "订单个数:" + allocList.size());
				//先批量进行更改订单状态

				List<String> preUpdateOrders = new ArrayList<>();
				if (allocList != null && allocList.size() > 0) {
					for (DlArtifiPrintLottery entity : allocList) {
						//更改已分配的状态
						entity.setOperationStatus(DlArtifiPrintLottery.OPERATION_STATUS_ALLOCATED);
                        entity.setUpdateTime(DateUtilNew.getCurrentTimeLong());
						entity.setAdminName(mobile);//操作人
						logger.info("[userLogin]" + " update orderSn:" + entity.getOrderSn() + " adminName:" + mobile + " opStatus:" + entity.getOperationStatus()+ " updateTime:"+DateUtilNew.getCurrentTimeLong());
						int updateRst = dlArtifiPrintMapper.updateArtifiLotteryPrint(entity);
						if(updateRst == 1){
							preUpdateOrders.add(entity.getOrderSn());
						}
					}
				}
				//然后进行分单到该人手中
				dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);

				List<String> preAddOrders = new ArrayList<>();
				if (allocList != null && allocList.size() > 0) {
					for (DlArtifiPrintLottery entity : allocList) {
						int lotteryClassifyId = entity.getLotteryClassifyId();
						String orderSn = entity.getOrderSn();
						DDyArtifiPrintEntity dEntity = new DDyArtifiPrintEntity();
						dEntity.orderSn = orderSn;
						dEntity.status = 0;
						dEntity.setLotteryClassifyId(lotteryClassifyId);
						int addRst = dyArtifiDao.addDyArtifiPrintInfo(mobile, dEntity);
						if(addRst == 1){
							preAddOrders.add(orderSn);
						}
					}
				}

				List<String> diff = preUpdateOrders.stream().filter(item -> !preAddOrders.contains(item)).collect(Collectors.toList());
				if(diff.size() > 0){
					logger.info("allocLotteryV2BySelect已经轮寻的订单号包括:"+preUpdateOrders.toString());
					logger.info("allocLotteryV2BySelect已经分配的订单号包括:"+preAddOrders.toString());
					logger.info("allocLotteryV2BySelect差异订单号包括:"+diff.toString());
				}
			}
		}catch(Throwable throwable){
			logger.error("---------------关键的分单的时候异常------------:"+throwable.getStackTrace());
		}
	}
}

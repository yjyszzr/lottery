package com.dl.shop.lottery.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.shop.base.dao.DyArtifiPrintDao;
import com.dl.shop.base.dao.DyArtifiPrintImple;
import com.dl.shop.base.dao.entity.DDyArtifiPrintEntity;
import com.dl.shop.base.manager.ArtifiLoginManager;
import com.dl.shop.lottery.configurer.DataBaseCfg;
import com.dl.shop.lottery.dao.DlArtifiPrintLotteryMapper;
import com.dl.shop.lottery.model.DlArtifiPrintLottery;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(value = "transactionManager2")
@Slf4j
public class ArtifiDyQueueService{
	private final static Logger logger = LoggerFactory.getLogger(DlDiscoveryPageService.class);
	
	@Resource
	private DataBaseCfg dataBaseCfg;
	
	@Resource
	private DlArtifiPrintLotteryMapper dlArtifiPrintMapper;

	private final int QUEUE_SIZE = 30;
	
	/**
	 * 用户登录
	 * @param obj
	 */
	public void userLogin(String uid) {
		ArtifiLoginManager.getInstance().onLogin(uid);
		//该用户queue table创建
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
		if(dyArtifiDao.isDyQueueExist(uid) <= 0) {
			dyArtifiDao.createDyQueue(uid);
			logger.info("[userLogin]" + " uid:" + uid + " table not exist");
		}else {
			logger.info("[userLogin]" + " uid:" + uid + " table exist");
		}
	}
	
	/**
	 * 退出登录
	 * @param obj
	 */
	public void userLogout(String mobile) {
		logger.info("[userLogout]" + " uid:" + mobile + " 退出登录...");
		ArtifiLoginManager.getInstance().onLogout(mobile);
		//获取该用户队列内容
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
		List<DDyArtifiPrintEntity> rList = dyArtifiDao.listAll(mobile,0);
		for(DDyArtifiPrintEntity dyArtiPrintEntity : rList) {
			//查询到该订单信息
			DlArtifiPrintLottery dlArtifiPrintLottery = new DlArtifiPrintLottery();
			dlArtifiPrintLottery.setOrderSn(dyArtiPrintEntity.orderSn);
			List<DlArtifiPrintLottery> list = dlArtifiPrintMapper.selectArtifiLotteryPrintByOrderSn(dlArtifiPrintLottery);
			//该订单信息回收到总池
			if(list != null && list.size() > 0) {
				DlArtifiPrintLottery dlEntity = list.get(0);
				dlEntity.setOperationStatus(DlArtifiPrintLottery.OPERATION_STATUS_INIT);
				dlEntity.setAdminName(mobile);
				dlEntity.setOperationTime(DateUtil.getCurrentTimeLong());
				dlArtifiPrintMapper.updateArtifiLotteryPrint(dlEntity);
				logger.info("[userLogout]" + " 该订单:" + dyArtiPrintEntity.orderSn + " 回收到总池... uid:" + mobile);
			}
		}
		//清空回收该该用户队列
		dyArtifiDao.dropTable(mobile);
	}

	/**
	 * 轮训查询总队列
	 */
	public void onTimerExec() {
		logger.info("[onTimerExec]");
		//获取今天未分配的订单
		List<DlArtifiPrintLottery> rSumList = dlArtifiPrintMapper.listLotteryTodayUnAlloc();
		logger.info("[onTimerExec]" + "获取今日未分配订单:" + rSumList.size());
		if(rSumList != null && rSumList.size() > 0) {
			DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
			//获取在线人数据
			List<String> userList = ArtifiLoginManager.getInstance().getCopyList();
			logger.info("[onTimerExec]" + "在线人数:" + userList.size());
			for(int i = 0;i < userList.size();i++) {
				logger.info("[onTimerExec]" + " uid:" + userList.get(i));
			}
			logger.info("==================================================");
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
	
	private List<DlArtifiPrintLottery> allocLottery(DyArtifiPrintDao dyArtifiDao,String uid,List<DlArtifiPrintLottery> rList,int size){
		List<DlArtifiPrintLottery> mList = new ArrayList<DlArtifiPrintLottery>();
		int s = rList.size() > size ? size : rList.size();
		logger.info("[allocLottery]" + " s:" + s);
		for(int i = 0;i < s ;i++){
			DlArtifiPrintLottery entity = rList.get(i);
			//该item数据分配给uid
			String orderSn = entity.getOrderSn();
			DDyArtifiPrintEntity dEntity = new DDyArtifiPrintEntity();
			dEntity.orderSn = orderSn;
			dyArtifiDao.addDyArtifiPrintInfo(uid,dEntity);
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
	
	/**
	 * 更改订单状态
	 * @param uid
	 * @param orderSn
	 * @param status
	 * @return
	 */
	public BaseResult<?> modifyOrderStatus(String mobile,String orderSn,int orderStatus){
		//删除队列数据
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
		int cnt = dyArtifiDao.delData(mobile,orderSn);
		if(cnt <= 0) {
			return ResultGenerator.genFailResult("订单查询失败");
		}
		//回收到主池，更改主池队列状态
		//查询到该订单信息
		DlArtifiPrintLottery dlArtifiPrintLottery = new DlArtifiPrintLottery();
		dlArtifiPrintLottery.setOrderSn(orderSn);
		List<DlArtifiPrintLottery> list = dlArtifiPrintMapper.selectArtifiLotteryPrintByOrderSn(dlArtifiPrintLottery);
		DlArtifiPrintLottery printLottery = null;
		if(list != null && list.size() > 0) {
			printLottery = list.get(0);
			printLottery.setOrderSn(orderSn);
			printLottery.setOrderStatus((byte)orderStatus);
			printLottery.setAdminName(mobile);
			printLottery.setOperationTime(DateUtil.getCurrentTimeLong());
			dlArtifiPrintMapper.updateArtifiLotteryPrint(printLottery);
		}
		return ResultGenerator.genSuccessResult();
	}
}

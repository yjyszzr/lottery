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
			logger.debug("[userLogin]" + " uid:" + uid + " table not exist");
		}else {
			logger.debug("[userLogin]" + " uid:" + uid + " table exist");
		}
	}
	
	/**
	 * 退出登录
	 * @param obj
	 */
	public void userLogout(String uid) {
		logger.info("[userLogout]" + " uid:" + uid + " 退出登录...");
		ArtifiLoginManager.getInstance().onLogout(uid);
		//获取该用户队列内容
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
		List<DDyArtifiPrintEntity> rList = dyArtifiDao.listAll(uid);
		for(DDyArtifiPrintEntity dyArtiPrintEntity : rList) {
			//查询到该订单信息
			DlArtifiPrintLottery dlArtifiPrintLottery = new DlArtifiPrintLottery();
			dlArtifiPrintLottery.setOrderSn(dyArtiPrintEntity.orderSn);
			List<DlArtifiPrintLottery> list = dlArtifiPrintMapper.selectArtifiLotteryPrintByOrderSn(dlArtifiPrintLottery);
			//该订单信息回收到总池
			if(list != null && list.size() > 0) {
				DlArtifiPrintLottery dlEntity = list.get(0);
				dlEntity.setOperationStatus((byte) 0);
				dlEntity.setAdminId(Integer.valueOf(uid));
				dlEntity.setOperationTime(DateUtil.getCurrentTimeLong());
				dlArtifiPrintMapper.updateArtifiLotteryPrint(dlEntity);
				logger.info("[userLogout]" + " 该订单:" + dyArtiPrintEntity.orderSn + " 回收到总池... uid:" + uid);
			}
		}
		//清空回收该该用户队列
		dyArtifiDao.dropTable(uid);
	}

	/**
	 * 轮训查询总队列
	 */
	public void onTimerExec() {
		logger.info("[onTimerExec]" + " dlArtifiPrintMapper:" + dlArtifiPrintMapper);
		//获取今天未分配的订单
		List<DlArtifiPrintLottery> rList = dlArtifiPrintMapper.listLotteryTodayUnAlloc();
		logger.debug("[onTimerExec]" + "获取今日未分配订单:" + rList.size());
		DyArtifiPrintDao dyArtifiDao = new DyArtifiPrintImple(dataBaseCfg);
		if(rList != null && rList.size() > 0) {
			//获取在线人数据
			List<String> userList = ArtifiLoginManager.getInstance().getCopyList();
			logger.debug("[onTimerExec]" + "在线人数:" + userList.size());
			for(int i = 0;i < userList.size();i++) {
				logger.debug("[onTimerExec]" + " uid:" + userList.get(i));
			}
			logger.debug("==================================================");
			if(userList != null && userList.size() > 0) {
				Map<String,List<DDyArtifiPrintEntity>> mMap = new HashMap<String,List<DDyArtifiPrintEntity>>();
				for(String uid : userList) {
					List<DDyArtifiPrintEntity> mLotteryList = dyArtifiDao.listAll(uid);
					mMap.put(uid,mLotteryList);
				}
				//开始分配订单
				for(int i = 0;rList.size() > 0 && i < userList.size();i++) {
					//获取随机一个用户
					String uid = getRandomItem(userList);
					//该用户的队列大小
					List<DDyArtifiPrintEntity> mLotteryList = mMap.get(uid);
					//如果不够30个，填充满30个
					if(rList.size() > 0 && mLotteryList.size() < QUEUE_SIZE) {
						int size = QUEUE_SIZE - mLotteryList.size();
						List<DlArtifiPrintLottery> allocList = allocLottery(dyArtifiDao,uid,rList,size);
						for(DlArtifiPrintLottery entity : allocList) {
							allocList.remove(entity);
							//更改已分配的状态
							entity.setOperationStatus((byte) 1);
							dlArtifiPrintMapper.updateArtifiLotteryPrint(entity);
						}
					}
				}
			}
		}
	}
	
	private List<DlArtifiPrintLottery> allocLottery(DyArtifiPrintDao dyArtifiDao,String uid,List<DlArtifiPrintLottery> rList,int size){
		List<DlArtifiPrintLottery> mList = new ArrayList<DlArtifiPrintLottery>();
		int s = rList.size() > size ? size : rList.size();
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
}

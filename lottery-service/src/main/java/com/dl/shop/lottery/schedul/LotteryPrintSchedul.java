package com.dl.shop.lottery.schedul;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.dl.base.result.BaseResult;
import com.dl.base.util.DateUtil;
import com.dl.lottery.dto.DlToStakeDTO;
import com.dl.lottery.dto.DlToStakeDTO.BackOrderDetail;
import com.dl.lottery.param.DlToStakeParam;
import com.dl.lottery.param.DlToStakeParam.PrintTicketOrderParam;
import com.dl.order.api.IOrderService;
import com.dl.order.param.OrderSnListGoPrintLotteryParam;
import com.dl.order.param.UpdateOrderInfoParam;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.service.DlLeagueMatchAsiaService;
import com.dl.shop.lottery.service.DlLeagueMatchEuropeService;
import com.dl.shop.lottery.service.DlLeagueMatchResultService;
import com.dl.shop.lottery.service.DlMatchSupportService;
import com.dl.shop.lottery.service.LotteryMatchService;
import com.dl.shop.lottery.service.LotteryPrintService;
import com.dl.shop.lottery.service.LotteryRewardService;
import com.dl.shop.payment.api.IpaymentService;
import com.dl.shop.payment.param.RollbackOrderAmountParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class LotteryPrintSchedul {
	
	@Resource
	private LotteryPrintMapper lotteryPrintMapper;
	
	@Resource
	private LotteryPrintService lotteryPrintService;
	
	@Resource
	private LotteryMatchService lotteryMatchService;
	
	@Resource
	private DlLeagueMatchResultService matchResultService;
	
	@Resource
	private LotteryRewardService lotteryRewardService;
	
	 @Resource
	 private DlMatchSupportService dlMatchSupportService;
	 
	 @Resource
	 private IOrderService orderService;
	 
	 @Resource
	 private IpaymentService paymentService;

	 @Resource
	 private DlLeagueMatchAsiaService dlLeagueMatchAsiaService;

	 @Resource
	 private DlLeagueMatchEuropeService dlLeagueMatchEuropeService;
	 
	 /**
	  * 赔率任务 （每5分钟执行一次）
	  */
	 //@Scheduled(cron = "0 0/5 * * * ?")
	 public void refreshPeilv() {
		 log.info("开始拉取赔率信息");
		 int start = DateUtil.getCurrentTimeLong();
		 List<Integer> changciIds = lotteryMatchService.getChangcidIsUnEnd();
		 if(CollectionUtils.isNotEmpty(changciIds)) {
			 for(Integer changciId: changciIds) {
				 log.info("拉取亚盘赔率信息"+changciId);
				 dlLeagueMatchAsiaService.refreshMatchAsiaInfoFromZC(changciId);
				 log.info("拉取欧赔赔率信息"+changciId);
				 dlLeagueMatchEuropeService.refreshMatchEuropeInfoFromZC(changciId);
			 }
		 }
		 int end = DateUtil.getCurrentTimeLong();
		 log.info("结束拉取赔率信息, time="+(end-start));
	 }
	
	/**
	 * 出票任务 （每5分钟执行一次）
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
    public void printLottery() {
        log.info("出票定时任务启动");
        OrderSnListGoPrintLotteryParam orderSnListGoPrintLotteryParam = new OrderSnListGoPrintLotteryParam();
        List<String> orderSns = orderService.orderSnListGoPrintLottery(orderSnListGoPrintLotteryParam).getData();
        if(CollectionUtils.isEmpty(orderSns)) {
        	log.info("暂时没有可出票的订单号");
        	return;
        }
        log.info("可出票的订单数："+orderSns.size());
        Set<String> successOrderSn = new HashSet<String>(orderSns.size());
        List<LotteryPrint> lotteryPrintList = lotteryPrintMapper.getPrintLotteryListByOrderSns(orderSns);
        if(CollectionUtils.isNotEmpty(lotteryPrintList)) {
        	log.info("lotteryPrintList size="+lotteryPrintList.size());
        	while(lotteryPrintList.size() > 0) {
        		int toIndex = lotteryPrintList.size() > 50?50:lotteryPrintList.size();
        		List<LotteryPrint> lotteryPrints = lotteryPrintList.subList(0, toIndex);
        		log.info(" go tostake size="+lotteryPrints.size());
        		this.toStak(successOrderSn, lotteryPrints);
        		lotteryPrintList.removeAll(lotteryPrints);
        	}
        	orderSns.removeAll(successOrderSn);
			if(!orderSns.isEmpty()) {
				log.info("出票失败的订单："+orderSns.stream().collect(Collectors.joining(",")));
				for(String orderSn: orderSns) {
					UpdateOrderInfoParam param = new UpdateOrderInfoParam();
					param.setOrderSn(orderSn);
					param.setAcceptTime(DateUtil.getCurrentTimeLong());
					param.setOrderStatus(2);
					param.setTicketTime(DateUtil.getCurrentTimeLong());
					BaseResult<String> updateOrderInfo = orderService.updateOrderInfo(param);
					//回退订单金额
					if(updateOrderInfo.getCode() == 0) {
						RollbackOrderAmountParam param1 = new RollbackOrderAmountParam();
						param1.setOrderSn(orderSn);
						log.info("invoke rollbackOrderAmount 准备回流资金");
						BaseResult rollbackOrderAmount = paymentService.rollbackOrderAmount(param1);
						log.info("rollbackOrderAmount ordersn="+orderSn+" result: code="+rollbackOrderAmount.getCode()+" msg="+rollbackOrderAmount.getMsg());
					}
				}
			}
        }
        log.info("出票定时任务结束");
    }


	/**
	 * 调用第三方出票
	 * @param successOrderSn
	 * @param lotteryPrintList
	 */
	private void toStak(Set<String> successOrderSn, List<LotteryPrint> lotteryPrints) {
		DlToStakeParam dlToStakeParam = new DlToStakeParam();
		dlToStakeParam.setMerchant(lotteryPrints.get(0).getMerchant());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dlToStakeParam.setTimestamp(sdf.format(new Date()));
		dlToStakeParam.setVersion("1.0");
		List<PrintTicketOrderParam> printTicketOrderParams = new LinkedList<PrintTicketOrderParam>();
		Map<String, String> ticketIdOrderSnMap = new HashMap<String, String>();
		lotteryPrints.forEach(lp->{
			PrintTicketOrderParam printTicketOrderParam = new PrintTicketOrderParam();
			printTicketOrderParam.setTicketId(lp.getTicketId());
			printTicketOrderParam.setGame(lp.getGame());
			printTicketOrderParam.setIssue(lp.getIssue());
			printTicketOrderParam.setPlayType(lp.getPlayType());
			printTicketOrderParam.setBetType(lp.getBetType());
			printTicketOrderParam.setTimes(lp.getTimes());
			printTicketOrderParam.setMoney(lp.getMoney().intValue());
			printTicketOrderParam.setStakes(lp.getStakes());
			printTicketOrderParams.add(printTicketOrderParam);
			ticketIdOrderSnMap.put(lp.getTicketId(), lp.getOrderSn());
		});
		dlToStakeParam.setOrders(printTicketOrderParams);
		DlToStakeDTO dlToStakeDTO = lotteryPrintService.toStake(dlToStakeParam);
		if(null != dlToStakeDTO && CollectionUtils.isNotEmpty(dlToStakeDTO.getOrders())) {
			log.info("inf tostake orders");
			List<LotteryPrint> lotteryPrintErrors = new LinkedList<LotteryPrint>();
			List<LotteryPrint> lotteryPrintSuccess = new LinkedList<LotteryPrint>();
			for(BackOrderDetail backOrderDetail : dlToStakeDTO.getOrders()) {
				LotteryPrint lotteryPrint = new LotteryPrint();
				lotteryPrint.setTicketId(backOrderDetail.getTicketId());
				if(backOrderDetail.getErrorCode() != 0) {
					lotteryPrint.setErrorCode(backOrderDetail.getErrorCode());
					//出票失败
					lotteryPrint.setStatus(2);
					lotteryPrintErrors.add(lotteryPrint);
				} else {
					//出票中
					successOrderSn.add(ticketIdOrderSnMap.get(backOrderDetail.getTicketId()));
					lotteryPrint.setStatus(3);
					lotteryPrintSuccess.add(lotteryPrint);
				}
			}
			if(CollectionUtils.isNotEmpty(lotteryPrintErrors)) {
				log.info("lotteryPrintErrors size = "+lotteryPrintErrors.size());
				long start = System.currentTimeMillis();
				for(LotteryPrint lotteryPrint:lotteryPrintErrors) {
					lotteryPrintService.update(lotteryPrint);
				}
				long end = System.currentTimeMillis();
				log.info("lotteryPrintErrors size = "+lotteryPrintErrors.size() + "  times=" + (end-start));
//				lotteryPrintService.updateBatchErrorByTicketId(lotteryPrintErrors);
			}
			if(CollectionUtils.isNotEmpty(lotteryPrintSuccess)) {
				log.info("lotteryPrintSuccess size="+lotteryPrintSuccess.size());
				long start = System.currentTimeMillis();
//				lotteryPrintService.updateBatchSuccessByTicketId(lotteryPrintSuccess);
				for(LotteryPrint lotteryPrint:lotteryPrintErrors) {
					lotteryPrintService.update(lotteryPrint);
				}
				long end = System.currentTimeMillis();
				log.info("lotteryPrintSuccess size="+lotteryPrintSuccess.size() + "  times=" + (end-start));
			}
		}
	}
	
	
	
	 /**
	  * 获取已完成比赛的比赛分数 （每2分钟执行一次）
	  */
	 @Scheduled(cron = "0 0/2 * * * ?")
	 public void fetchMatchScore() {
		 log.info("当天比赛结果拉取开始");
	    lotteryMatchService.pullMatchResult();
		log.info("当天比赛结果拉取完成");
		log.info("当天比赛结果详情拉取开始");
		matchResultService.pullMatchResultInfos();
		log.info("当天比赛结果详情拉取完成");
		log.info("比赛支持率拉取开始");
		dlMatchSupportService.refreshMatchSupports();
		log.info("比赛支持率拉取完成");
	}
	
	/**
	 * 获取开奖结果的txt （每2分钟执行一次）
	 */
	@Scheduled(cron = "0 0/2 * * * ?")
	public void fetchRewardTxt() {
		log.info("获取开奖结果开始");
		lotteryRewardService.resovleRewardTxt();
		log.info("获取开奖结果结束");
	}
	/**
	 * 赛事列表获取
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void fetchMatch() {
		log.info("赛事列表获取开始");
		lotteryMatchService.saveMatchList();
		log.info("赛事列表获取结束");
	}
	
	/**
	 * 更新待开奖的订单
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void updateOrderAfterOpenReward() {
		log.info("更新待开奖的订单开始");
		lotteryRewardService.updateOrderAfterOpenReward();
		log.info("更新待开奖的订单结束");
	}
	
	/**
	 * 更新中奖用户的账户
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void addRewardMoneyToUsers() {
		log.info("更新中奖用户的账户开始");
		lotteryRewardService.addRewardMoneyToUsers();
		log.info("更新中奖用户的账户结束");
		
	}
	
	/**
	 * 更新彩票信息
	 */
	//@Scheduled(cron = "0 0/1 * * * ?")
	public void updatePrintLotteryCompareStatus() {
		log.info("更新中奖用户的账户开始");
		lotteryPrintService.updatePrintLotteryCompareStatus();
		log.info("更新中奖用户的账户结束");
		
	}
	
}

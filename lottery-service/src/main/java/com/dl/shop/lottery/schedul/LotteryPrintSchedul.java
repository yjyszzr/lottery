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
	private LotteryRewardService lotteryRewardService;
	
	 @Resource
	 private DlMatchSupportService dlMatchSupportService;
	 
	 @Resource
	 private IOrderService orderService;
	 
	 @Resource
	 private IpaymentService paymentService;
	 
	
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
        List<LotteryPrint> lotteryPrints = lotteryPrintMapper.getPrintLotteryListByOrderSns(orderSns);
        if(CollectionUtils.isNotEmpty(lotteryPrints)) {
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
        		Set<String> successOrderSn = new HashSet<String>(orderSns.size());
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
        			lotteryPrintService.updateBatchErrorByTicketId(lotteryPrintErrors);
        		}
        		if(CollectionUtils.isNotEmpty(lotteryPrintSuccess)) {
        			log.info("lotteryPrintSuccess size="+lotteryPrintSuccess.size());
        			lotteryPrintService.updateBatchSuccessByTicketId(lotteryPrintSuccess);
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
        					paymentService.rollbackOrderAmount(param1);
        				}
        			}
        		}
        	}
        }
        log.info("出票定时任务结束");
    }
	
	
	
	 /**
	  * 获取已完成比赛的比赛分数 （每2分钟执行一次）
	  */
	 @Scheduled(cron = "0 0/2 * * * ?")
	 public void fetchMatchScore() {
		 log.info("当天比赛结果拉取开始");
		int rst = lotteryMatchService.pullMatchResult();
		log.info("当天比赛结果拉取完成");
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
}

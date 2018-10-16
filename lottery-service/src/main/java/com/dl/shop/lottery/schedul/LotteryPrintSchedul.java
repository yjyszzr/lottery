package com.dl.shop.lottery.schedul;

import java.time.LocalTime;
import java.time.ZoneId;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.dl.order.api.IOrderService;
import com.dl.shop.lottery.service.DlLeagueMatchAsiaService;
import com.dl.shop.lottery.service.DlLeagueMatchEuropeService;
import com.dl.shop.lottery.service.DlLeagueMatchResultService;
import com.dl.shop.lottery.service.DlMatchSupportService;
import com.dl.shop.lottery.service.LotteryMatchService;
import com.dl.shop.lottery.service.LotteryPrintService;
import com.dl.shop.lottery.service.LotteryRewardService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
@EnableScheduling
public class LotteryPrintSchedul {
	
//	@Resource
//	private LotteryPrintService lotteryPrintService;
//	
//	@Resource
//	private LotteryMatchService lotteryMatchService;
//	
//	@Resource
//	private DlLeagueMatchResultService matchResultService;
//	
//	@Resource
//	private LotteryRewardService lotteryRewardService;
//	
//	 @Resource
//	 private DlMatchSupportService dlMatchSupportService;
//	 
//	 @Resource
//	 private IOrderService orderService;
//	 
//	 @Resource
//	 private DlLeagueMatchAsiaService dlLeagueMatchAsiaService;
//
//	 @Resource
//	 private DlLeagueMatchEuropeService dlLeagueMatchEuropeService;
//	 
//	 *//**
//	  * 赔率任务 （每5分钟执行一次）
//	  *//*
//	 //@Scheduled(cron = "0 0/5 * * * ?")
//	 public void refreshPeilv() {
//		 log.info("开始拉取赔率信息");
//		 int start = DateUtil.getCurrentTimeLong();
//		 List<Integer> changciIds = lotteryMatchService.getChangcidIsUnEnd();
//		 if(CollectionUtils.isNotEmpty(changciIds)) {
//			 for(Integer changciId: changciIds) {
//				 log.info("拉取亚盘赔率信息"+changciId);
//				 dlLeagueMatchAsiaService.refreshMatchAsiaInfoFromZC(changciId);
//				 log.info("拉取欧赔赔率信息"+changciId);
//				 dlLeagueMatchEuropeService.refreshMatchEuropeInfoFromZC(changciId);
//			 }
//		 }
//		 int end = DateUtil.getCurrentTimeLong();
//		 log.info("结束拉取赔率信息, time="+(end-start));
//	 }
//	
//	*//**
//	 * 出票任务 ,调用第三方接口出票定时任务
//	 *//*
//	@Scheduled(cron = "0 0/1 * * * ?")
//    public void printLottery() {
//        log.info("出票定时任务启动");
//        lotteryPrintService.goPrintLottery();
//        log.info("出票定时任务结束");
//        //每天9点前不作查询处理，只作出票处理
//        LocalTime localTime = LocalTime.now(ZoneId.systemDefault());
//        int hour = localTime.getHour();
//        if(hour >= 9) {
//        	log.info("彩票出票状态查询定时任务启动");
//        	lotteryPrintService.goQueryStake();
//        	log.info("彩票出票状态查询定时任务结束");
//        }
//    }
//	
//
//	 *//**
//	  * 抓取已完成比赛的比赛分数 （每10分钟执行一次）
//	  * 这里不需爬虫作处理
//	  *//*
//	 @Scheduled(cron = "0 0/10 * * * ?")
//	 public void fetchMatchScore() {
//		 log.info("当天比赛结果拉取开始");
//	    lotteryMatchService.pullMatchResult();
//		log.info("当天比赛结果拉取完成");
//		log.info("当天比赛结果详情拉取开始");
//		matchResultService.pullMatchResultInfos();
//		log.info("当天比赛结果详情拉取完成");
//		log.info("比赛支持率拉取开始");
//		dlMatchSupportService.refreshMatchSupports();
//		log.info("比赛支持率拉取完成");
//	}
//	
//	*//**
//	 * 获取开奖结果的txt （每2分钟执行一次）
//	 *//*
//	//@Scheduled(cron = "0 0/2 * * * ?")
//	public void fetchRewardTxt() {
//		log.info("获取开奖结果开始");
//		lotteryRewardService.resovleRewardTxt();
//		log.info("获取开奖结果结束");
//	}
//	*//**
//	 * 抓取赛事列表获取
//	 * 当天可投比赛信息
//	 *//*
//	@Scheduled(cron = "0 0/15 * * * ?")
//	public void fetchMatch() {
//		log.info("赛事列表获取开始");
//		lotteryMatchService.saveMatchList();
//		log.info("赛事列表获取结束");
//	}
//	
//	*//**
//	 * 更新待开奖的订单
//	 * 
//	 *//*
//	@Scheduled(cron = "0 0/5 * * * ?")
//	public void updateOrderAfterOpenReward() {
//		log.info("更新待开奖的订单开始");
//		lotteryRewardService.updateOrderAfterOpenReward();
//		log.info("更新待开奖的订单结束");
//	}
//	
//	*//**
//	 * 更新彩票信息
//	 *//*
//	@Scheduled(cron = "0 0/5 * * * ?")
//	public void updatePrintLotteryCompareStatus() {
//		log.info("更新彩票信息，彩票对奖开始");
//		lotteryPrintService.updatePrintLotteryCompareStatus();
//		log.info("更新彩票信息，彩票对奖结束");
//		
//	}
	
	@Scheduled(cron = "0/5 * *  * * ?")
	public void artifiPrintLotterySchedual() {
		log.info("[artifiPrintLotterySchedual]" + "...");
	}
}

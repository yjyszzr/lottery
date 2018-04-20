package com.dl.shop.lottery.schedul;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.dl.lottery.dto.DlToStakeDTO;
import com.dl.lottery.dto.DlToStakeDTO.BackOrderDetail;
import com.dl.lottery.param.DlToStakeParam;
import com.dl.lottery.param.DlToStakeParam.PrintTicketOrderParam;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.service.DlLeagueMatchAsiaService;
import com.dl.shop.lottery.service.DlLeagueMatchEuropeService;
import com.dl.shop.lottery.service.DlMatchSupportService;
import com.dl.shop.lottery.service.LotteryMatchService;
import com.dl.shop.lottery.service.LotteryPrintService;
import com.dl.shop.lottery.service.LotteryRewardService;

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
    private DlLeagueMatchAsiaService dlLeagueMatchAsiaService;
	
	 @Resource
	 private DlLeagueMatchEuropeService dlLeagueMatchEuropeService;
	 
	 @Resource
	 private DlMatchSupportService dlMatchSupportService;
	 
	/**
	 * 赔率任务 （每5分钟执行一次）
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
    public void refreshPeilv() {
		log.info("开始拉取赔率信息");
		List<Integer> changciIds = lotteryMatchService.getChangcidIsUnEnd();
		if(CollectionUtils.isNotEmpty(changciIds)) {
			for(Integer changciId: changciIds) {
				log.info("拉取亚盘赔率信息"+changciId);
				dlLeagueMatchAsiaService.refreshMatchAsiaInfoFromZC(changciId);
				log.info("拉取欧赔赔率信息"+changciId);
				dlLeagueMatchEuropeService.refreshMatchEuropeInfoFromZC(changciId);
			}
		}
		log.info("结束拉取赔率信息");
	}
	
	/**
	 * 出票任务 （每5分钟执行一次）
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
    public void printLottery() {
        log.info("出票定时任务启动");
        List<LotteryPrint> lotteryPrints = lotteryPrintMapper.getPrintLotteryList();
        if(CollectionUtils.isNotEmpty(lotteryPrints)) {
        	DlToStakeParam dlToStakeParam = new DlToStakeParam();
        	dlToStakeParam.setMerchant(lotteryPrints.get(0).getMerchant());
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	dlToStakeParam.setTimestamp(sdf.format(new Date()));
        	dlToStakeParam.setVersion("1.0");
        	List<PrintTicketOrderParam> printTicketOrderParams = new LinkedList<PrintTicketOrderParam>();
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
        	});
        	dlToStakeParam.setOrders(printTicketOrderParams);
        	DlToStakeDTO dlToStakeDTO = lotteryPrintService.toStake(dlToStakeParam);
        	if(null != dlToStakeDTO && CollectionUtils.isNotEmpty(dlToStakeDTO.getOrders())) {
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
        				lotteryPrint.setStatus(3);
        				lotteryPrintSuccess.add(lotteryPrint);
        			}
        		}
        		if(CollectionUtils.isNotEmpty(lotteryPrintErrors)) {
        			lotteryPrintService.updateBatchErrorByTicketId(lotteryPrintErrors);
        		}
        		if(CollectionUtils.isNotEmpty(lotteryPrintSuccess)) {
        			lotteryPrintService.updateBatchSuccessByTicketId(lotteryPrintSuccess);
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

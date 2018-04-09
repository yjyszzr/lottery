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
        		List<LotteryPrint> lotteryPrintList = new LinkedList<LotteryPrint>();
        		for(BackOrderDetail backOrderDetail : dlToStakeDTO.getOrders()) {
        			if(backOrderDetail.getErrorCode() != 0) {
        				LotteryPrint lotteryPrint = new LotteryPrint();
        				lotteryPrint.setTicketId(backOrderDetail.getTicketId());
        				lotteryPrint.setErrorCode(backOrderDetail.getErrorCode());
        				lotteryPrint.setStatus(2);
        				lotteryPrintList.add(lotteryPrint);
        			}
        		}
        		if(CollectionUtils.isNotEmpty(lotteryPrintList)) {
        			lotteryPrintMapper.updateBatchByTicketId(lotteryPrintList);
        		}
        	}
        }
    }
	
	
	
	 /**
	  * 获取已完成比赛的比赛分数 （每2分钟执行一次）
	  */
	 @Scheduled(cron = "0 0/2 * * * ?")
	 public void fetchMatchScore() {
		int rst = lotteryMatchService.pullMatchResult();
		if(rst == -1) {
			log.info("当天比赛结果拉取完成");
		}
	}
	
	/**
	 * 获取开奖结果的txt （每2分钟执行一次）
	 */
	@Scheduled(cron = "0 0/2 * * * ?")
	public void fetchRewardTxt() {
		lotteryRewardService.resovleRewardTxt();
	}
}

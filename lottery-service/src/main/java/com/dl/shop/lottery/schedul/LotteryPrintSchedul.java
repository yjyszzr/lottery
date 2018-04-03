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

import com.dl.param.DlToStakeParam;
import com.dl.param.DlToStakeParam.PrintTicketOrderParam;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.service.LotteryPrintService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class LotteryPrintSchedul {
	
	@Resource
	private LotteryPrintMapper lotteryPrintMapper;
	
	@Resource
	private LotteryPrintService lotteryPrintService;
	
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
        		printTicketOrderParam.setPlayType(lp.getPlaytype());
        		printTicketOrderParam.setBetType(lp.getBettype());
        		printTicketOrderParam.setTimes(lp.getTimes());
        		printTicketOrderParam.setMoney(lp.getMoney().intValue());
        		printTicketOrderParam.setStakes(lp.getStakes());
        		printTicketOrderParams.add(printTicketOrderParam);
        	});
        	dlToStakeParam.setOrders(printTicketOrderParams);
        	lotteryPrintService.toStake(dlToStakeParam);
        }
    }
}

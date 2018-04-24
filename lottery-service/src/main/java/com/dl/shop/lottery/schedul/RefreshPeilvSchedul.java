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
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.dl.base.util.DateUtil;
import com.dl.lottery.dto.DlToStakeDTO;
import com.dl.lottery.dto.DlToStakeDTO.BackOrderDetail;
import com.dl.lottery.param.DlToStakeParam;
import com.dl.lottery.param.DlToStakeParam.PrintTicketOrderParam;
import com.dl.order.api.IOrderService;
import com.dl.order.param.UpdateOrderStatusParam;
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
public class RefreshPeilvSchedul {
	
	@Resource
	private LotteryMatchService lotteryMatchService;
	
	@Resource
    private DlLeagueMatchAsiaService dlLeagueMatchAsiaService;
	
	 @Resource
	 private DlLeagueMatchEuropeService dlLeagueMatchEuropeService;
	 
	 private final static String START_FLAG = "in";
	 
	/**
	 * 赔率任务 （每5分钟执行一次）
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
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
	
	
}

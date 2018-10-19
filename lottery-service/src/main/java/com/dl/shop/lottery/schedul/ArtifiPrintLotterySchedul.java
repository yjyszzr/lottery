package com.dl.shop.lottery.schedul;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.dl.shop.lottery.service.ArtifiPrintLotteryUserLoginService;

@Slf4j
@Configuration
@EnableScheduling
public class ArtifiPrintLotterySchedul {
	@Resource
	private ArtifiPrintLotteryUserLoginService artifiPrintLotteryUserLoginService;

	/**
	 * 检查用户是否退出
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void printLottery() {
		log.info("检查用户是否退出,定时任务启动");
		artifiPrintLotteryUserLoginService.deleteUserInfo();
		log.info("定时任务结束");
	}
}

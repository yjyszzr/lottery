package com.dl.shop.lottery.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dl.shop.base.cfg.DataBaseCfg;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(value = "transactionManager2")
@Slf4j
public class ArtifiDyQueueService{
	@Resource
	private DataBaseCfg dataBaseCfg;
	
	/**
	 * 用户登录
	 * @param obj
	 */
	public void userLogin(Object obj) {
		
	}
	
	/**
	 * 退出登录
	 * @param obj
	 */
	public void userLogout(Object obj) {
		
	}
	
	
}

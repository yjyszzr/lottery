package com.dl.shop.lottery.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArtifiPrintLotteryUserLoginService {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 重置用户活跃时间
	 * 
	 * @param mobile
	 */
	public void updateUserStatus(String mobile) {
		try {
			stringRedisTemplate.opsForValue().set(mobile, "1", 900, TimeUnit.SECONDS);
		}catch(Throwable ee) {
			ee.printStackTrace();
		}
	};
}

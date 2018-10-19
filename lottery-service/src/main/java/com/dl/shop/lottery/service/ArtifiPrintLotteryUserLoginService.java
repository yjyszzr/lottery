package com.dl.shop.lottery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ArtifiPrintLotteryUserLoginService {
	private final static Logger logger = LoggerFactory.getLogger(ArtifiPrintLotteryUserLoginService.class);
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
		} catch (Throwable ee) {
			ee.printStackTrace();
		}
	};

	public void deleteUserInfo() {
		try {
			Set<String> keys = stringRedisTemplate.keys("XN_*");
			List<String> strList = new ArrayList<String>();
			for (String str : keys) {
				Long expireTime = stringRedisTemplate.getExpire(str);// 根据key获取过期时间
				logger.info("登录人信息为============={},过期时间为:==========================={}", str, expireTime);
				str = str.replace("XN_", "");
				strList.add(str);
				// stringRedisTemplate.delete("XN_" + str);
			}
		} catch (Throwable ee) {
			ee.printStackTrace();
		}
	};

}

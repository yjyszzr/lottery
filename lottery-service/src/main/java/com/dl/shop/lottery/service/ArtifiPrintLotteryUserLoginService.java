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

import com.dl.base.result.BaseResult;
import com.dl.member.api.IUserLoginService;
import com.dl.member.dto.UserLoginDTO;
import com.dl.member.param.MobileInfoParam;
import com.dl.shop.auth.api.IAuthService;
import com.dl.shop.auth.dto.InvalidateTokenDTO;

@Service
public class ArtifiPrintLotteryUserLoginService {
	private final static Logger logger = LoggerFactory.getLogger(ArtifiPrintLotteryUserLoginService.class);
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private IAuthService authService;

	@Resource
	private static IUserLoginService userLoginService;

	@Resource
	private static ArtifiDyQueueService artifiDyQueueService;

	/**
	 * 重置用户活跃时间
	 * 
	 * @param mobile
	 */
	public void updateUserStatus(String mobile) {
		try {
			stringRedisTemplate.opsForValue().set(mobile, "1", 240, TimeUnit.SECONDS);
		} catch (Throwable ee) {
			ee.printStackTrace();
		}
	};

	public void deleteUserInfo() {
		try {
			Set<String> keys = stringRedisTemplate.keys("XN_*");
			List<String> strList = new ArrayList<String>();

			for (String mobileStr : keys) {
				mobileStr = mobileStr.replace("XN_", "");
				strList.add(mobileStr);
			}
			for (String str : keys) {
				Long expireTime = stringRedisTemplate.getExpire(str);// 根据key获取过期时间
				logger.info("登录人信息为============={},过期时间为:==========================={}", str, expireTime);
				str = str.replace("XN_", "");

				if (expireTime < 60) {
					// 清空该用户redis中的信息
					stringRedisTemplate.delete("XN_" + str);
					MobileInfoParam mobileInfoParam = new MobileInfoParam();
					BaseResult<UserLoginDTO> mobileInfo = userLoginService.findByMobile(mobileInfoParam);
					if (null != mobileInfo.getData()) {
						// 清空过期用户的所有token
						InvalidateTokenDTO invalidateTokenDTO = new InvalidateTokenDTO();
						invalidateTokenDTO.setInvalidateType(2);
						mobileInfoParam.setMobile(str);
						invalidateTokenDTO.setUserId(Integer.parseInt(mobileInfo.getData().getMobile()));
						authService.invalidate(invalidateTokenDTO);
						// 剔除该登录用户
						strList.remove(str);
						// 去除该用户的队列
						artifiDyQueueService.userLogout(str, strList);
					}
				}

			}
		} catch (Throwable ee) {
			ee.printStackTrace();
		}
	};
}

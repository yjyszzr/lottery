package com.dl.shop.lottery.web;

import io.swagger.annotations.ApiOperation;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.JSONHelper;
import com.dl.base.util.RegexUtil;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.enums.MemberEnums;
import com.dl.member.api.ISMSService;
import com.dl.member.api.IUserLoginService;
import com.dl.member.dto.UserLoginDTO;
import com.dl.member.param.LoginLogParam;
import com.dl.member.param.SmsParam;
import com.dl.member.param.UserLoginWithSmsParam;
import com.dl.shop.lottery.service.ArtifiDyQueueService;
import com.dl.shop.lottery.service.ArtifiPrintLotteryUserLoginService;

@RestController
@RequestMapping("/artifiPrintLotteryUserLogin")
public class ArtifiPrintLotteryUserLoginController {
	private final static Logger logger = LoggerFactory.getLogger(ArtifiPrintLotteryUserLoginController.class);
	@Resource
	private ArtifiPrintLotteryUserLoginService artifiPrintLotteryUserLoginService;

	@Resource
	private ISMSService smsService;

	@Resource
	private IUserLoginService userLoginService;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private ArtifiDyQueueService artifiDyQueueService;

	/**
	 * 发送短信验证码
	 * 
	 * @param mobileNumberParam
	 * @return
	 */
	@ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
	@PostMapping("/sendSmsCode")
	public BaseResult<String> sendSms(@RequestBody SmsParam smsParam) {
		return smsService.sendSmsCode(smsParam);
	}

	@ApiOperation(value = "验证码登录", notes = "验证码登录")
	@PostMapping("/loginBySms")
	public BaseResult<UserLoginDTO> loginBySms(@RequestBody UserLoginWithSmsParam userLoginMobileParam, HttpServletRequest request) {
		// 校验手机号的合法性
		String loginParams = JSONHelper.bean2json(userLoginMobileParam);
		if (!RegexUtil.checkMobile(userLoginMobileParam.getMobile())) {
			LoginLogParam loginLogParam = new LoginLogParam();
			loginLogParam.setUserId(-1);
			loginLogParam.setLoginType(0);
			loginLogParam.setLoginSstatus(1);
			loginLogParam.setLoginParams(loginParams);
			loginLogParam.setLoginResult(MemberEnums.MOBILE_VALID_ERROR.getMsg());
			userLoginService.loginLog(loginLogParam);
			return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
		}
		// 校验验证码的正确性
		String mobile = userLoginMobileParam.getMobile();
		logger.info("手机号码为:======================" + mobile);
		String cacheSmsCode = smsService.getRedisSmsCode(mobile);
		logger.info("redis返回的验证码为:======================" + cacheSmsCode);
		logger.info("手机端录入的验证码为:======================" + userLoginMobileParam.getSmsCode().toString());
		logger.info("验证码为比较状态:======================" + cacheSmsCode.equals(userLoginMobileParam.getSmsCode().toString()));

		if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(userLoginMobileParam.getSmsCode().toString())) {
			LoginLogParam loginLogParam = new LoginLogParam();
			loginLogParam.setUserId(-1);
			loginLogParam.setLoginType(0);
			loginLogParam.setLoginSstatus(1);
			loginLogParam.setLoginParams(loginParams);
			loginLogParam.setLoginResult(MemberEnums.SMSCODE_WRONG.getMsg());
			userLoginService.loginLog(loginLogParam);
			return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
		}
		UserLoginDTO userLoginDTO = userLoginService.findByMobile(mobile);
		// 校验手机号是否存在
		if (null == userLoginDTO) {
			LoginLogParam loginLogParam = new LoginLogParam();
			loginLogParam.setUserId(-1);
			loginLogParam.setLoginType(0);
			loginLogParam.setLoginSstatus(1);
			loginLogParam.setLoginParams(loginParams);
			loginLogParam.setLoginResult(MemberEnums.NO_REGISTER.getMsg());
			userLoginService.loginLog(loginLogParam);
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
		}
		// 将手机号存入redis,置为在线状态,过期时间为15分钟
		stringRedisTemplate.opsForValue().set(mobile, "1", 900, TimeUnit.SECONDS);
		// 调用用户登录
		artifiDyQueueService.userLogin(mobile);
		// 清空验证码
		// smsService.deleteRedisSmsCode(mobile);
		// LoginLogParam loginLogParam = new LoginLogParam();
		// loginLogParam.setUserId(Integer.parseInt(userLoginDTO.getMobile()));
		// loginLogParam.setLoginType(0);
		// loginLogParam.setLoginSstatus(0);
		// loginLogParam.setLoginParams(loginParams);
		// loginLogParam.setLoginResult(JSONHelper.bean2json(userLoginDTO));
		// userLoginService.loginLog(loginLogParam);
		userLoginDTO = userLoginService.loginBySms(userLoginMobileParam);

		return ResultGenerator.genSuccessResult("登录成功", userLoginDTO);
	}

	@ApiOperation(value = "退出", notes = "退出")
	@PostMapping("/logout")
	public void logout(@RequestBody String mobile) {
		SessionUtil.getUserId();
		stringRedisTemplate.delete(mobile);
		// 调用用户退出登录
		artifiDyQueueService.userLogout(mobile);
	}
}

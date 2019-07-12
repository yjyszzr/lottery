package com.dl.shop.lottery.web;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
import tk.mybatis.mapper.entity.Condition;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.JSONHelper;
import com.dl.base.util.RegexUtil;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.enums.MemberEnums;
import com.dl.member.api.ISMSService;
import com.dl.member.api.IUserLoginService;
import com.dl.member.api.IUserService;
import com.dl.member.dto.UserDTO;
import com.dl.member.dto.UserLoginDTO;
import com.dl.member.param.LoginLogParam;
import com.dl.member.param.MobileInfoParam;
import com.dl.member.param.MobilePwdCreateParam;
import com.dl.member.param.SmsParam;
import com.dl.member.param.UserIdRealParam;
import com.dl.member.param.UserLoginWithPassParam;
import com.dl.member.param.UserLoginWithSmsParam;
import com.dl.member.param.UserRePwdParam;
import com.dl.shop.auth.api.IAuthService;
import com.dl.shop.auth.dto.InvalidateTokenDTO;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.model.DlXNWhiteList;
import com.dl.shop.lottery.service.ArtifiDyQueueService;
import com.dl.shop.lottery.service.ArtifiPrintLotteryUserLoginService;
import com.dl.shop.lottery.service.DlXNWhiteListService;

@RestController
@RequestMapping("/artifiPrintLotteryUserLogin")
public class ArtifiPrintLotteryUserLoginController {
	private final static Logger logger = LoggerFactory.getLogger(ArtifiPrintLotteryUserLoginController.class);
	@Resource
	private IAuthService authService;

	@Resource
	private ArtifiPrintLotteryUserLoginService artifiPrintLotteryUserLoginService;

	@Resource
	private ISMSService smsService;

	@Resource
	private IUserLoginService userLoginService;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private IUserService userService;

	@Resource
	private ArtifiDyQueueService artifiDyQueueService;

	@Resource
	private DlXNWhiteListService dlXNWhiteListService;
	
//  20181119 用密码登录替代	
//	/**
//	 * 发送短信验证码
//	 * 
//	 * @param mobileNumberParam
//	 * @return
//	 */
//	@ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
//	@PostMapping("/sendSmsCode")
//	public BaseResult<String> sendSms(@RequestBody SmsParam smsParam) {
//		Condition c = new Condition(DlXNWhiteList.class);
//		c.createCriteria().andEqualTo("mobile", smsParam.getMobile());
//		List<DlXNWhiteList> xnWhiteListList = dlXNWhiteListService.findByCondition(c);
//		if (xnWhiteListList.size() == 0) {
//			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
//		}
//		return smsService.sendSmsCode(smsParam);
//	}

//  20181119 用密码登录替代	
//	@ApiOperation(value = "验证码登录", notes = "验证码登录")
//	@PostMapping("/loginBySms")
//	public BaseResult<UserLoginDTO> loginBySms(@RequestBody UserLoginWithSmsParam userLoginMobileParam, HttpServletRequest request) {
//		// 校验手机号的合法性
//		String loginParams = JSONHelper.bean2json(userLoginMobileParam);
//		if (!RegexUtil.checkMobile(userLoginMobileParam.getMobile())) {
//			LoginLogParam loginLogParam = new LoginLogParam();
//			loginLogParam.setUserId(-1);
//			loginLogParam.setLoginType(0);
//			loginLogParam.setLoginSstatus(1);
//			loginLogParam.setLoginParams(loginParams);
//			loginLogParam.setLoginResult(MemberEnums.MOBILE_VALID_ERROR.getMsg());
//			userLoginService.loginLog(loginLogParam);
//			return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
//		}
//		// 校验验证码的正确性
//		String mobile = userLoginMobileParam.getMobile();
//		logger.info("手机号码为:======================" + mobile);
//		String cacheSmsCode = smsService.getRedisSmsCode(mobile);
//		cacheSmsCode = cacheSmsCode.replace("\"", "");
//		logger.info("redis返回的验证码为:======================" + cacheSmsCode);
//		logger.info("手机端录入的验证码为:======================" + userLoginMobileParam.getSmsCode().toString());
//		logger.info("验证码为比较状态:======================" + cacheSmsCode.equals(userLoginMobileParam.getSmsCode().toString()));
//
//		if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(userLoginMobileParam.getSmsCode().toString())) {
//			LoginLogParam loginLogParam = new LoginLogParam();
//			loginLogParam.setUserId(-1);
//			loginLogParam.setLoginType(0);
//			loginLogParam.setLoginSstatus(1);
//			loginLogParam.setLoginParams(loginParams);
//			loginLogParam.setLoginResult(MemberEnums.SMSCODE_WRONG.getMsg());
//			userLoginService.loginLog(loginLogParam);
//			return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
//		}
//		MobileInfoParam mobileInfo = new MobileInfoParam();
//		mobileInfo.setMobile(mobile);
//
//		BaseResult<UserLoginDTO> userLoginDTO = userLoginService.findByMobile(mobileInfo);
//		// 校验手机号是否存在
//		if (null == userLoginDTO.getData()) {
//			LoginLogParam loginLogParam = new LoginLogParam();
//			loginLogParam.setUserId(-1);
//			loginLogParam.setLoginType(0);
//			loginLogParam.setLoginSstatus(1);
//			loginLogParam.setLoginParams(loginParams);
//			loginLogParam.setLoginResult(MemberEnums.NO_REGISTER.getMsg());
//			userLoginService.loginLog(loginLogParam);
//			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
//		}
//		// 将手机号存入redis,置为在线状态,过期时间为15分钟
//		// 清空验证码
//		// smsService.deleteRedisSmsCode(mobile);
//		// LoginLogParam loginLogParam = new LoginLogParam();
//		// loginLogParam.setUserId(Integer.parseInt(userLoginDTO.getMobile()));
//		// loginLogParam.setLoginType(0);
//		// loginLogParam.setLoginSstatus(0);
//		// loginLogParam.setLoginParams(loginParams);
//		// loginLogParam.setLoginResult(JSONHelper.bean2json(userLoginDTO));
//		// userLoginService.loginLog(loginLogParam);
//		userLoginDTO = userLoginService.loginBySms(userLoginMobileParam);
//
//		logger.info("登录信息为:======================" + userLoginDTO);
//		stringRedisTemplate.opsForValue().set("XN_" + mobile, "1", ProjectConstant.EXPIRE_TIME, TimeUnit.SECONDS);
//		List<String> mobileList = getAllLoginInfo();
//		logger.info("登录人数为:======================" + mobileList.size());
//		logger.info("登录人list:======================" + mobileList);
//
//		// 调用用户登录
//		artifiDyQueueService.userLogin(mobile, mobileList);
//
//		return ResultGenerator.genSuccessResult("登录成功", userLoginDTO.getData());
//	}

	private BaseResult<UserLoginDTO> onUserLogin(UserLoginWithPassParam loginPwdParams){
		logger.info("[onUserLogin]");
		String mobile = loginPwdParams.getMobile();
		String loginParams = JSONHelper.bean2json(loginPwdParams);
		MobileInfoParam mobileInfo = new MobileInfoParam();
		mobileInfo.setMobile(loginPwdParams.getMobile());
		BaseResult<UserLoginDTO> userLoginDTO = userLoginService.findByMobile(mobileInfo);
		// 校验手机号是否存在
		if (null == userLoginDTO.getData()) {
			LoginLogParam loginLogParam = new LoginLogParam();
			loginLogParam.setUserId(-1);
			loginLogParam.setLoginType(0);
			loginLogParam.setLoginSstatus(1);
			loginLogParam.setLoginParams(loginParams);
			loginLogParam.setLoginResult(MemberEnums.NO_REGISTER.getMsg());
			userLoginService.loginLog(loginLogParam);
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
		}
		userLoginDTO = userLoginService.loginWithPwd(loginPwdParams);
		if(!userLoginDTO.isSuccess()) {
			return ResultGenerator.genResult(userLoginDTO.getCode(),userLoginDTO.getMsg());
		}
		logger.info("登录信息为:======================" + userLoginDTO);
		stringRedisTemplate.opsForValue().set("XN_" + mobile, "1", ProjectConstant.EXPIRE_TIME*24, TimeUnit.SECONDS);
		List<String> mobileList = getAllLoginInfo();
		logger.info("登录人数为:======================" + mobileList.size());
		logger.info("登录人list:======================" + mobileList);
		// 调用用户登录
		artifiDyQueueService.userLogin(mobile, mobileList);
		return ResultGenerator.genSuccessResult("登录成功", userLoginDTO.getData());
	}
	
	@ApiOperation(value = "密码登录", notes = "密码登录")
	@PostMapping("/loginByPwd")
	public BaseResult<UserLoginDTO> loginByPwd(@RequestBody UserLoginWithPassParam params, HttpServletRequest request){
		String mobile = params.getMobile();
		String pwd = params.getPassword();
		logger.info("[loginByPwd]" + " mobile:" + mobile + " pwd:" + pwd);
		if(!RegexUtil.checkMobile(mobile)){
			return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
		}
		if(StringUtils.isEmpty(pwd)) {
			return ResultGenerator.genResult(MemberEnums.PASS_FORMAT_ERROR.getcode(), MemberEnums.PASS_FORMAT_ERROR.getMsg());
		}
		//判断是否是白名单
		Condition c = new Condition(DlXNWhiteList.class);
		c.createCriteria().andEqualTo("mobile", mobile);
		List<DlXNWhiteList> xnWhiteListList = dlXNWhiteListService.findByCondition(c);
		if (xnWhiteListList.size() == 0) {
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
		}
		//如果彩小秘系统中没有该用户
		MobileInfoParam mobileParams = new MobileInfoParam();
		mobileParams.setMobile(mobile);
		BaseResult<UserLoginDTO> userLoginDTO = userLoginService.findByMobile(mobileParams);
		//校验手机号是否不存在,直接创建该用户
		if (null == userLoginDTO.getData()) {
			if(!"123456".equals(pwd)) {
				return ResultGenerator.genResult(MemberEnums.WRONG_IDENTITY.getcode(), MemberEnums.WRONG_IDENTITY.getMsg());
			}
			MobilePwdCreateParam mobilePwdParams = new MobilePwdCreateParam();
			mobilePwdParams.setMobile(mobile);
			mobilePwdParams.setPassword(pwd);
			mobilePwdParams.setLoginSource(params.getLoginSource());
			BaseResult<UserLoginDTO> bResult = userLoginService.onCreateUser(mobilePwdParams);
			if(bResult != null && bResult.isSuccess()) {
				logger.info("[loginByPwd]" + "创建用户成功");
			}else {
				logger.info("[loginByPwd]" + "创建用户失败");
			}
		}
		//用户登录
		return onUserLogin(params);
	}
	
	@ApiOperation(value = "密码登录", notes = "密码登录")
	@PostMapping("/repwd")
	public BaseResult<?> rePwd(@RequestBody UserRePwdParam params, HttpServletRequest request){
		String mobile = params.getMobile();
		String pwd = params.getPassword();
		String newPwd = params.getNewPwd();
		logger.info("[rePwd]" + " mobile:" + mobile + " pwd:" + pwd + " newPwd:" + newPwd);
		if(!RegexUtil.checkMobile(mobile)){
			return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
		}
		if(StringUtils.isEmpty(pwd)) {
			return ResultGenerator.genResult(MemberEnums.PASS_FORMAT_ERROR.getcode(), MemberEnums.PASS_FORMAT_ERROR.getMsg());
		}
		if(StringUtils.isEmpty(newPwd)) {
			return ResultGenerator.genResult(MemberEnums.PASS_FORMAT_ERROR.getcode(), MemberEnums.PASS_FORMAT_ERROR.getMsg());
		}
		if(newPwd.length() < 6) {
			return ResultGenerator.genResult(MemberEnums.USER_PASS_ACCOUNT_SIX.getcode(),MemberEnums.USER_PASS_ACCOUNT_SIX.getMsg());
		}
		if(newPwd.length() > 20) {
			return ResultGenerator.genResult(MemberEnums.USER_PASS_ACCOUNT_20.getcode(),MemberEnums.USER_PASS_ACCOUNT_20.getMsg());
		}
		//判断是否是白名单
		Condition c = new Condition(DlXNWhiteList.class);
		c.createCriteria().andEqualTo("mobile", mobile);
		List<DlXNWhiteList> xnWhiteListList = dlXNWhiteListService.findByCondition(c);
		if (xnWhiteListList.size() == 0) {
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
		}
		return userLoginService.rePwd(params);
	}
	
	
	private List<String> getAllLoginInfo() {
		Set<String> keys = stringRedisTemplate.keys("XN_*");
		List<String> strList = new ArrayList<String>();
		for (String str : keys) {
			str = str.replace("XN_", "");
			strList.add(str);
		}
		return strList;
	}

	@ApiOperation(value = "退出", notes = "退出")
	@PostMapping("/logout")
	public BaseResult logout(@RequestBody String mobile) {
		Integer userId = SessionUtil.getUserId();
		logger.info("登录人UserId:======================" + userId);
		logger.info("退出人入参的手机号:======================" + mobile);
		UserIdRealParam params = new UserIdRealParam();
		params.setUserId(userId);
		BaseResult<UserDTO> queryUserInfo = userService.queryUserInfoReal(params);
		mobile = queryUserInfo.getData().getMobile();
		logger.info("退出人session中获取得手机号:======================" + mobile);
		stringRedisTemplate.delete("XN_" + mobile);
		// 调用用户退出登录
		InvalidateTokenDTO invalidateTokenDTO = new InvalidateTokenDTO();
		invalidateTokenDTO.setInvalidateType(2);
		invalidateTokenDTO.setUserId(userId);
		logger.info("=======================清空过期{}用户的所有token", mobile);
		authService.invalidate(invalidateTokenDTO);
		List<String> mobileList = getAllLoginInfo();
		logger.info("退出后剩余的登录人数:======================" + mobileList.size());
		logger.info("退出后剩余人的list:======================" + mobileList);
		artifiDyQueueService.userLogout(mobile, mobileList);
		return ResultGenerator.genSuccessResult("退出成功", null);
	}
}

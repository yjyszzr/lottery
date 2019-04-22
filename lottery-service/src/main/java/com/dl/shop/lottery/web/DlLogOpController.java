package com.dl.shop.lottery.web;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.LogPicDetailDTO;
import com.dl.lottery.dto.OperationRecordDTO;
import com.dl.lottery.param.DateStrPageParam;
import com.dl.lottery.param.OrderSnParam;
import com.dl.member.api.IUserService;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.UserIdRealParam;
import com.dl.shop.lottery.service.DlLogOpService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/lottery/opLog")
public class DlLogOpController {

	@Resource
	private DlLogOpService dlLogOpService;

	@Resource
	private IUserService iUserService;

	@ApiOperation(value = "根据时间查询打单记录", notes = "根据时间查询打单记录")
	@PostMapping("/queryLogByTime")
	public BaseResult<OperationRecordDTO> queryLogByTime(@RequestBody DateStrPageParam param) {
		Integer userId = SessionUtil.getUserId();
		UserIdRealParam uParam = new UserIdRealParam();
		uParam.setUserId(userId);
		log.info("queryLogByTime=============={}", uParam.toString());
		BaseResult<UserDTO> uDTO = iUserService.queryUserInfoReal(uParam);
		if (uDTO.getCode() != 0) {
			return ResultGenerator.genFailResult("查询用户信息异常", null);
		}

		Integer startTime = null;
		Integer endTime = null;
		String dateTime = param.getDateStr();
		if (StringUtils.isEmpty(dateTime)) {
			startTime = DateUtil.getTimeAfterDays(new Date(), 0, 0, 0, 0);
			endTime = DateUtil.getCurrentTimeLong();
		} else {
			Date someDate = DateUtil.strToDate(dateTime);
			startTime = DateUtil.getTimeAfterDays(someDate, 0, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(someDate, 0, 23, 59, 59);
		}

		return dlLogOpService.queryLogByTime(param.getPageNum(), param.getPageSize(), uDTO.getData().getMobile(), startTime, endTime);
	}

	@ApiOperation(value = "根据订单号查询彩票照片详情", notes = "根据订单号查询彩票照片详情")
	@PostMapping("/queryLogOpByOrderSn")
	public BaseResult<LogPicDetailDTO> queryLogOpByOrderSn(@RequestBody OrderSnParam param) {
		return dlLogOpService.queryLogOpByOrderSn(param.getOrderSn());
	}
}

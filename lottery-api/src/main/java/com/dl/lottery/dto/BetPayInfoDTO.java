package com.dl.lottery.dto;

import java.util.List;

import com.dl.member.dto.UserBonusDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BetPayInfoDTO {

	@ApiModelProperty(value="订单信息token")
	private String payToken;
	@ApiModelProperty(value="订单金额")
	private String orderMoney;
	@ApiModelProperty(value="余额抵扣")
	private String surplus;
	@ApiModelProperty(value="优惠卷抵扣")
	private String bonusAmount;
	@ApiModelProperty(value="当前优惠卷")
	private String bonusId;
	@ApiModelProperty(value="需第三方支付")
	private String thirdPartyPaid;
	@ApiModelProperty(value="用户可用优惠卷列表")
	private List<UserBonusDTO> bonusList;
}

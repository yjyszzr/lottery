package com.dl.lottery.param;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArtifiLotteryDetailParam {
	@ApiModelProperty(value="手机号")
	private String mobile;
	
	@ApiModelProperty(value="订单号")
	@NotEmpty
	private String orderSn;
	
	@ApiModelProperty(value="每日赛事值为5")
	private Integer appCode;
}

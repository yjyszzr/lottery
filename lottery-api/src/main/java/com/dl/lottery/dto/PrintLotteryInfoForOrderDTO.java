package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PrintLotteryInfoForOrderDTO {

	@ApiModelProperty("订单号")
	private String orderSn;
	@ApiModelProperty("订单出票状态")
	private int orderStatus;
	@ApiModelProperty("出票信息")
	private String printSp;
	@ApiModelProperty("接单时间")
	private Integer acceptTime;
	@ApiModelProperty("出票时间")
	private Integer ticketTime;
}

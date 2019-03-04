package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderIdDTO {	
	@ApiModelProperty("订单id")
	private String orderId;
	
	@ApiModelProperty("订单号")
	private String orderSn;
}

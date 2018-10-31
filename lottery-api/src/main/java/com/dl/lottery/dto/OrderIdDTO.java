package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderIdDTO {	
	@ApiModelProperty("订单id")
	 private String orderId;
}

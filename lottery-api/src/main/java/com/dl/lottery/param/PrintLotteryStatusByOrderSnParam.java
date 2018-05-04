package com.dl.lottery.param;

import java.io.Serializable;
import java.util.List;

import com.dl.lottery.dto.LotteryPrintDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PrintLotteryStatusByOrderSnParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "订单号", required = true)
	private String orderSn;
	
}

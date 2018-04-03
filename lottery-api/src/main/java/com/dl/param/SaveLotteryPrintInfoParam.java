package com.dl.param;

import java.io.Serializable;
import java.util.List;

import com.dl.dto.LotteryPrintDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveLotteryPrintInfoParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "预出票信息", required = true)
	private List<LotteryPrintDTO> lotteryPrints;
	@ApiModelProperty(value = "订单号", required = true)
	private String orderSn;
	
}

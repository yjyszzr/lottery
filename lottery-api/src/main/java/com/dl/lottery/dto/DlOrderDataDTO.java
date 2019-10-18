package com.dl.lottery.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlOrderDataDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "订单编号")
	public String orderSn;
	
	@ApiModelProperty(value = "该订单的中奖金额")
	public BigDecimal realRewardMoney;
	
	public String compareStatus;
	
	public int printStatus;
}

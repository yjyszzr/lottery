package com.dl.lottery.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlLotteryPrintMoneyDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "兑奖金额（超过该金额，需要审核后兑奖）")
	public BigDecimal rewardLimit; 
	
	@ApiModelProperty(value = "中奖订单列表")
	public List<DlOrderDataDTO> orderDataDTOs; 
	
}

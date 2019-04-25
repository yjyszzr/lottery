package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrintStakeResultDTO implements Serializable {
	
	@ApiModelProperty(value = "订单编号")
	private String orderSn = "";
	
	@ApiModelProperty(value = "订单状态:0-待出票 1-出票成功 2-出票失败 3-待开奖  4-未中奖5-已中奖")
	private String status = "";

	@ApiModelProperty(value = "彩票的照片")
	private String picUrl = "";

	@ApiModelProperty(value = "中奖金额")
	private String awardMoney = "0";

	@ApiModelProperty(value = "赔率：格式 玩法|赛事编号|cellCode@赔率")
	private String print_sp = "";
	
	@ApiModelProperty(value = "商户订单号")
	private String merchantOrderSn = "";

}

package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlOpLogDTO {
	
	@ApiModelProperty(value = "订单号")
	private String orderSn;
	
	@ApiModelProperty(value = "彩种id:1-足彩 2-大乐透")
	private String lotteryClassifyId;
	
	@ApiModelProperty(value = "1已出票，2出票失败")
	private String optType;
		
	@ApiModelProperty(value = "logo")
	private String logo;
}

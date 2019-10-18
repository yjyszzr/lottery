package com.dl.lottery.dto;


import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TicketRewardDTO {
	
	@ApiModelProperty(value="rewardSum")
	private BigDecimal rewardSum;
	
	@ApiModelProperty(value="compareStatus")
	private String compareStatus;
	
	@ApiModelProperty(value="compareStatus")
	private BigDecimal thirdPartRewardMoney;
	

}

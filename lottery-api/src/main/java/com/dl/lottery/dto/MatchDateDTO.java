package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchDateDTO {
	
	@ApiModelProperty(value = "标志:1-今天  0-非今天", required = true)
	public String isSelected = "0";
	
	@ApiModelProperty(value = "日期:", required = true)
	public String strDate;
	
}

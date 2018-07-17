package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchLiveEventDTO {

	@ApiModelProperty("事件类型")
	String eventType;
	@ApiModelProperty("事件编码")
	String eventCode;
	@ApiModelProperty("事件名称")
	String eventName;
	@ApiModelProperty("时间")
	String minute;
	@ApiModelProperty("队员id")
	String personId;
	@ApiModelProperty("队员")
	String person;
	@ApiModelProperty("主客队")
	String isHa;

}

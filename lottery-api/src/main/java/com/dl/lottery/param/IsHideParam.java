package com.dl.lottery.param;

import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IsHideParam implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="投注结束时间")
	private Integer betEndTime;
 
	@ApiModelProperty(value="比赛时间")
	private Integer matchTime;
	
	
}

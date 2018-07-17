package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchMinuteAndScoreDTO {

	@ApiModelProperty("比赛时长")
	String minute;
	@ApiModelProperty("上半场比分")
	String firstHalf;
	@ApiModelProperty("全场比分")
	String whole;;
}

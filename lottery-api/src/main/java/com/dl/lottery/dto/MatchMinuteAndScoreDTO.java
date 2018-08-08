package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchMinuteAndScoreDTO {

	@ApiModelProperty("比赛时长")
	private String minute = "";
	@ApiModelProperty("上半场比分")
	private String firstHalf ="";
	@ApiModelProperty("全场比分")
	private String whole = "";
	@ApiModelProperty("比赛状态:0-未开赛,1-已完成,2-取消,4-推迟,5-暂停,6-进行中")
	private String matchStatus ="";
}

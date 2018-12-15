package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class MatchTimePream implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "比赛时间")
	private Integer matchTime;
}

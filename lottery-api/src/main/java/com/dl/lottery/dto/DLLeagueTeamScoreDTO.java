package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class DLLeagueTeamScoreDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Integer teamId;

	@ApiModelProperty(value = "球队名称")
	private String teamName;

	@ApiModelProperty(value = "比赛场次数")
	private Integer matchNum;

	@ApiModelProperty(value = "胜场次数")
	private Integer matchH;

	@ApiModelProperty(value = "平场次数")
	private Integer matchD;

	@ApiModelProperty(value = "负场次数")
	private Integer matchL;

	@ApiModelProperty(value = "进球数")
	private Integer ballIn;

	@ApiModelProperty(value = "失球数")
	private Integer ballLose;

	@ApiModelProperty(value = "净球数")
	private Integer ballClean;

	@ApiModelProperty(value = "积分")
	private Integer score;

	@ApiModelProperty(value = "排名")
	private Integer teamOrder;

}

package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchTeamInfoDTO implements Serializable{

	@ApiModelProperty(value = "球队名称", required = true)
	public String teamAbbr;
	
	@ApiModelProperty(value = "胜数", required = true)
	public int win;
	
	@ApiModelProperty(value = "负场数", required = true)
	private int lose;
	
	@ApiModelProperty(value = "平局数", required = true)
	private int draw;
	
	@ApiModelProperty(value = "总数", required = true)
	private int total;
	
	@ApiModelProperty(value = "比赛详情", required = true)
	private List<MatchInfoDTO> matchInfos;
}

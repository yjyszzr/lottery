package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class DLLeagueTeamScoreInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Integer teamId;

	@ApiModelProperty(value = "球队名称")
	private String teamName;

	@ApiModelProperty(value = "客")
	private DLLeagueTeamScoreDTO tteamScore;
	@ApiModelProperty(value = "主")
	private DLLeagueTeamScoreDTO hteamScore;
	@ApiModelProperty(value = "总")
	private DLLeagueTeamScoreDTO lteamScore;

}

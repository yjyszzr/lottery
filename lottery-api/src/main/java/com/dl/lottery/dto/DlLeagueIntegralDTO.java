package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlLeagueIntegralDTO {

	@ApiModelProperty(value = "客")
	private List<DLLeagueTeamScoreDTO> vteamScoreList;
	@ApiModelProperty(value = "主")
	private List<DLLeagueTeamScoreDTO> hteamScoreList;
	@ApiModelProperty(value = "总")
	private List<DLLeagueTeamScoreDTO> tteamScoreList;

}

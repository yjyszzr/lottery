package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlLeagueScoreDTO {
	@ApiModelProperty(value = "客队积分<联赛>")
	private List<DLLeagueTeamScoreDTO> vteamScoreList;
	@ApiModelProperty(value = "主队积分<联赛>")
	private List<DLLeagueTeamScoreDTO> hteamScoreList;
	@ApiModelProperty(value = "总积分<联赛>")
	private List<DLLeagueTeamScoreDTO> tteamScoreList;
	@ApiModelProperty(value = "A组积分(杯赛)")
	private List<DLLeagueTeamScoreDTO> groupAScoreList;
	@ApiModelProperty(value = "B组积分(杯赛)")
	private List<DLLeagueTeamScoreDTO> groupBScoreList;

}

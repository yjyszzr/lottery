package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlLeagueSeason500wDTO {

	@ApiModelProperty(value = "赛季")
	private List<DlSeason500wDTO> leagueSeasonInfoList;

}

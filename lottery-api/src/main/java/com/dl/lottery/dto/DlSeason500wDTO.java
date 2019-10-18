package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlSeason500wDTO {

	@ApiModelProperty("赛季id")
	private Integer seasonId;

	@ApiModelProperty("联赛id")
	private Integer leagueId;

	@ApiModelProperty("联赛赛季")
	private String matchSeason;

}

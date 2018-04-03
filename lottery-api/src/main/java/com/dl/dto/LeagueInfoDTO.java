package com.dl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LeagueInfoDTO {

	@ApiModelProperty(value="联赛id")
	private Integer leagueId;
	@ApiModelProperty(value="联赛名称")
	 private String leagueName;
	@ApiModelProperty(value="联赛简称")
	private String leagueAddr;
}

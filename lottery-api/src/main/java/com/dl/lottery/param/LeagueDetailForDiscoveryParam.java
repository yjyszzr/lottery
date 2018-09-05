package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LeagueDetailForDiscoveryParam {

	@ApiModelProperty("联赛id")
	private Integer leagueId;
	@ApiModelProperty("赛季id")
	private Integer seasonId;
}

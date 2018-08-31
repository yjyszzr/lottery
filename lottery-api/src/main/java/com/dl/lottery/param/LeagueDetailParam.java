package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LeagueDetailParam {

	@ApiModelProperty("联赛id")
	private Integer leagueId;
}

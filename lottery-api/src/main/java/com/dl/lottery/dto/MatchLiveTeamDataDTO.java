package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchLiveTeamDataDTO {

	@ApiModelProperty("主队数据")
	private String teamHData;
	@ApiModelProperty("客队数据")
	private String teamAData;
}

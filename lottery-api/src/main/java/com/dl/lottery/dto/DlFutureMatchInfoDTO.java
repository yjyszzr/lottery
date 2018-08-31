package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlFutureMatchInfoDTO {

	@ApiModelProperty("比赛时间")
	private String matchTime;

	@ApiModelProperty("联赛简称")
	private String leagueAbbr;

	@ApiModelProperty("主队简称")
	private String homeTeamAbbr;

	@ApiModelProperty("客队简称")
	private String visitorTeamAbbr;

}

package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DLFutureMatchDTO {

	@ApiModelProperty("竞猜赛事id")
	private Integer matchId;
	
	@ApiModelProperty("比赛日期")
    private String matchDate;

	@ApiModelProperty("比赛时间")
    private String matchTime;

	@ApiModelProperty("联赛名称")
    private String leagueName;

	@ApiModelProperty("联赛简称")
    private String leagueAbbr;

	@ApiModelProperty("联赛id")
    private Integer leagueId;

	@ApiModelProperty("主队名称")
    private String homeTeamName;

	@ApiModelProperty("主队简称")
    private String homeTeamAbbr;

	@ApiModelProperty("主队id")
    private Integer homeTeamId;

	@ApiModelProperty("客队名称")
    private String visitorTeamName;

	@ApiModelProperty("客队简称")
    private String visitorTeamAbbr;

	@ApiModelProperty("客队id")
    private Integer visitorTeamId;

}

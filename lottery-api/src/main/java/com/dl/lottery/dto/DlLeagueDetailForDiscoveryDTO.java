package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlLeagueDetailForDiscoveryDTO {

	@ApiModelProperty(value = "联赛id")
	private Integer leagueId;
	@ApiModelProperty(value = "联赛名称")
	private String leagueName;
	@ApiModelProperty(value = "联赛简称")
	private String leagueAddr;
	@ApiModelProperty(value = "联赛logo")
	private String leaguePic;
	@ApiModelProperty(value = "联赛规则")
	private String leagueRule;
	@ApiModelProperty(value = "0:杯赛,1:联赛")
	private Integer leagueType;
	@ApiModelProperty(value = "赛季")
	private DlLeagueSeason500wDTO leagueSeason;
	@ApiModelProperty(value = "联赛积分")
	private DlLeagueScoreDTO leagueScore;
	@ApiModelProperty(value = "联赛射手")
	private DlLeagueShooterDTO leagueShooter;
	@ApiModelProperty(value = "联赛赛程")
	private DlMatchGroupData500WDTO matchGroupData;
	@ApiModelProperty(value = "联赛球队")
	private DlLeagueTeamDTO leagueTeam;

}

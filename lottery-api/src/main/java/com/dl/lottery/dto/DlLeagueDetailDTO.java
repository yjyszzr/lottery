package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class DlLeagueDetailDTO {

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
	@ApiModelProperty(value = "联赛积分")
	private DlLeagueIntegralDTO leagueScore;
	@ApiModelProperty(value = "联赛射手")
	private DlLeagueShooterDTO leagueShooter;
	@ApiModelProperty(value = "联赛赛程")
	private DlLeagueMatchDTO leagueMatch;
	@ApiModelProperty(value = "联赛球队")
	private DlLeagueTeamDTO leagueTeam;
	
}

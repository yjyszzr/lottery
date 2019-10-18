package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchLiveStatisticsDTO {

	@ApiModelProperty("进攻")
	private MatchLiveTeamDataDTO attacks;
	@ApiModelProperty("进球")
	private MatchLiveTeamDataDTO goals;
	@ApiModelProperty("")
	private MatchLiveTeamDataDTO goalKicks;
	@ApiModelProperty("所有")
	private MatchLiveTeamDataDTO possession;
	@ApiModelProperty("替代")
	private MatchLiveTeamDataDTO substitutions;
	@ApiModelProperty("投掷")
	private MatchLiveTeamDataDTO throwIns;
	
	@ApiModelProperty("角球")
	private MatchLiveTeamDataDTO corners;
	@ApiModelProperty("有威胁攻势")
	private MatchLiveTeamDataDTO dangerousAttacks;
	@ApiModelProperty("犯规")
	private MatchLiveTeamDataDTO fouls;
	@ApiModelProperty("任意球")
	private MatchLiveTeamDataDTO freeKicks;
	@ApiModelProperty("越位")
	private MatchLiveTeamDataDTO offsides;
	@ApiModelProperty("控球率")
	private MatchLiveTeamDataDTO posession;
	@ApiModelProperty("被封堵")
	private MatchLiveTeamDataDTO shotsBlocked;
	@ApiModelProperty("射偏")
	private MatchLiveTeamDataDTO shotsOffTarget;
	@ApiModelProperty("射正")
	private MatchLiveTeamDataDTO shotsOnTarget;
	@ApiModelProperty("黄牌")
	private MatchLiveTeamDataDTO yellowCards;
}

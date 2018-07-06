package com.dl.lottery.dto;

import lombok.Data;

@Data
public class MatchLiveStatisticsDTO {

	private MatchLiveTeamDataDTO attacks;
	private MatchLiveTeamDataDTO corners;
	private MatchLiveTeamDataDTO dangerousAttacks;
	private MatchLiveTeamDataDTO fouls;
	private MatchLiveTeamDataDTO freeKicks;
	private MatchLiveTeamDataDTO goals;
	private MatchLiveTeamDataDTO goalKicks;
	private MatchLiveTeamDataDTO offsides;
	private MatchLiveTeamDataDTO posession;
	private MatchLiveTeamDataDTO possession;
	private MatchLiveTeamDataDTO shotsBlocked;
	private MatchLiveTeamDataDTO shotsOffTarget;
	private MatchLiveTeamDataDTO shotsOnTarget;
	private MatchLiveTeamDataDTO substitutions;
	private MatchLiveTeamDataDTO throwIns;
	private MatchLiveTeamDataDTO yellowCards;
}

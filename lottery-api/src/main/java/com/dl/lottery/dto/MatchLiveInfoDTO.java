package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchLiveInfoDTO {

	@ApiModelProperty("比赛时间")
	private Integer matchTime;
	@ApiModelProperty("场次")
	private String changci;
	@ApiModelProperty("联赛")
	private String leagueAddr;
	@ApiModelProperty("主队")
	private String homeTeamAbbr;
	@ApiModelProperty("客队")
	private String visitingTeamAbbr;
	@ApiModelProperty("事件列表")
	private List<MatchLiveEventDTO> eventList;
	@ApiModelProperty("统计数据")
	private List<MatchLiveTeamDataDTO> matchLiveStatisticsDTO;
}

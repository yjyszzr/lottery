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
	@ApiModelProperty("比赛状态")
	private String matchStatus;
	@ApiModelProperty("比赛时长")
	private String minute;
	@ApiModelProperty("主队全场比分")
    private String fsH;
	@ApiModelProperty("客队队全场比分")
    private String fsA;
	@ApiModelProperty("主队半场比分")
    private String htsH;
	@ApiModelProperty("客队半场比分")
    private String htsA;	
	@ApiModelProperty("事件列表")
	private List<MatchLiveEventDTO> eventList;
	@ApiModelProperty("统计数据")
	private List<MatchLiveTeamDataDTO> matchLiveStatisticsDTO;
}

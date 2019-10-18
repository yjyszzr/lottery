package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlLeagueScoreDTO {
	@ApiModelProperty(value = "比赛类型,0杯赛，1联赛")
	private Integer matchType;
	@ApiModelProperty(value = "联赛积分列表")
	private List<MatchScoreDTO> matchScoreDTOList;

	@Data
	public static class MatchScoreDTO {
		@ApiModelProperty(value = "组名(主队积分,客队积分,总积分<联赛>)( A组,B组,C组...<杯赛>)")
		private String groupName;
		@ApiModelProperty(value = "联赛积分列表")
		private List<DLLeagueTeamScoreDTO> leagueCcoreList;
	}

}

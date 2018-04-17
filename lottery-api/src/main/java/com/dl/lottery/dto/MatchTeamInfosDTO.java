package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchTeamInfosDTO implements Serializable{

	@ApiModelProperty(value = "历史交锋", required = true)
	public MatchTeamInfoDTO hvMatchTeamInfo;
	
	@ApiModelProperty(value = "主场战绩", required = true)
	public MatchTeamInfoDTO hMatchTeamInfo;
	
	@ApiModelProperty(value = "客场战绩", required = true)
	private MatchTeamInfoDTO vMatchTeamInfo;
	
	@ApiModelProperty(value = "亚盘", required = true)
	List<LeagueMatchAsiaDTO> leagueMatchAsias ;
	
	@ApiModelProperty(value = "欧赔", required = true)
	List<LeagueMatchEuropeDTO> leagueMatchEuropes ;
	
	@ApiModelProperty(value = "主场积分", required = true)
	DLLeagueTeamScoreInfoDTO homeTeamScoreInfo;
	
	@ApiModelProperty(value = "客场积分", required = true)
	DLLeagueTeamScoreInfoDTO visitingTeamScoreInfo ;
	
}

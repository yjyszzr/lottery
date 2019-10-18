package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchTeamInfosDTO implements Serializable{

	@ApiModelProperty(value = "赛事信息", required = true)
	private MatchInfoForTeamDTO matchInfo;
	
	@ApiModelProperty(value = "历史交锋", required = true)
	private MatchTeamInfoDTO hvMatchTeamInfo;
	
	@ApiModelProperty(value = "主场战绩", required = true)
	private MatchTeamInfoDTO hMatchTeamInfo;
	
	@ApiModelProperty(value = "客场战绩", required = true)
	private MatchTeamInfoDTO vMatchTeamInfo;
	
	@ApiModelProperty(value = "主场主战绩", required = true)
	private MatchTeamInfoDTO hhMatchTeamInfo;
	
	@ApiModelProperty(value = "客场客战绩", required = true)
	private MatchTeamInfoDTO vvMatchTeamInfo;
	
	@ApiModelProperty(value = "亚盘", required = true)
	private List<LeagueMatchAsiaDTO> leagueMatchAsias ;
	
	@ApiModelProperty(value = "欧赔", required = true)
	private List<LeagueMatchEuropeDTO> leagueMatchEuropes ;
	
	@ApiModelProperty(value = "主场积分", required = true)
	private DLLeagueTeamScoreInfoDTO homeTeamScoreInfo;
	
	@ApiModelProperty(value = "客场积分", required = true)
	private DLLeagueTeamScoreInfoDTO visitingTeamScoreInfo ;
	
	@ApiModelProperty(value = "大小球", required = true)
	private List<LeagueMatchDaoXiaoDTO> leagueMatchDaoxiaos ;
	
	@ApiModelProperty(value = "主队未来赛事", required = true)
	private List<DLFutureMatchDTO> hFutureMatchInfos;
	
	@ApiModelProperty(value = "客队未来赛事", required = true)
	private List<DLFutureMatchDTO> vFutureMatchInfos;
}

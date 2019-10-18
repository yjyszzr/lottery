package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchLineUpInfosDTO {

	@ApiModelProperty("主裁判")
	private String refereeName;
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
	@ApiModelProperty("主队首发队员")
	private List<MatchLineUpPersonDTO> hlineupPersons ;
	@ApiModelProperty("主队替补队员")
	private List<MatchLineUpPersonDTO> hbenchPersons ;
	@ApiModelProperty("客队首发队员")
	private List<MatchLineUpPersonDTO> alineupPersons ;
	@ApiModelProperty("客队替补队员")
	private List<MatchLineUpPersonDTO> abenchPersons ;
	@ApiModelProperty("主队受伤队员")
	private List<MatchLineUpPersonDTO> hInjureiesPersons ;
	@ApiModelProperty("客队受伤队员")
	private List<MatchLineUpPersonDTO> aInjureiesPersons ;
	@ApiModelProperty("主队停赛队员")
	private List<MatchLineUpPersonDTO> hSuspensionPersons ;
	@ApiModelProperty("客队停赛队员")
	private List<MatchLineUpPersonDTO> aSuspensionPersons ;
	@ApiModelProperty("客队阵型")
	private String formationTeamA;
	@ApiModelProperty("客队教练")
	private String coachTeamA ;
	@ApiModelProperty("主队阵型")
	private String formationTeamH ;
	@ApiModelProperty("主队教练")
	private String coachTeamH ;
	
	@ApiModelProperty("比赛状态")
	private String matchStatus;
	@ApiModelProperty("比赛时长")
	private String minute;
	
	
}

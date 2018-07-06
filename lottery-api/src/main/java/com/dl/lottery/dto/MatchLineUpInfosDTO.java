package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchLineUpInfosDTO {

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
}

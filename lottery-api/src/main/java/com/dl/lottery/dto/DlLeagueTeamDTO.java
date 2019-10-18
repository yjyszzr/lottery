package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlLeagueTeamDTO {

	@ApiModelProperty(value = " 联赛赛季id")
	private List<DlLeagueTeamInfoDTO> leagueTeamInfoDTOList;

}

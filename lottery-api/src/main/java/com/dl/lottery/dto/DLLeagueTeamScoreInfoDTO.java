package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import com.dl.member.dto.UserBonusDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DLLeagueTeamScoreInfoDTO implements Serializable{

	@ApiModelProperty(value="")
	 private Integer teamId;

	@ApiModelProperty(value="球队名称")
    private String teamName;

	@ApiModelProperty(value="客")
	private DLLeagueTeamScoreDTO tteamScore ;
	@ApiModelProperty(value="主")
	private DLLeagueTeamScoreDTO hteamScore ;
	@ApiModelProperty(value="总")
	private DLLeagueTeamScoreDTO lteamScore;


}

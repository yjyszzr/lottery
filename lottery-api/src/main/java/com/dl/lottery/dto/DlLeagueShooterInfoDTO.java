package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlLeagueShooterInfoDTO {

	@ApiModelProperty(value = " 联赛赛季id")
	private Integer matchSeasonId;

	@ApiModelProperty(value = " 球员名称")
	private String playerName;

	@ApiModelProperty(value = "球队名称")
	private String playerTeam;

	@ApiModelProperty(value = "进球数")
	private Integer inNum;

	@ApiModelProperty(value = "  排名")
	private Integer sort;

}

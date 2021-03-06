package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlLeagueTeamInfoDTO {

	@ApiModelProperty(value = "球队Id")
	private Integer teamId;

	@ApiModelProperty(value = "球队名称")
	private String teamAddr;

	@ApiModelProperty(value = "球队名拼音")
	private String teamInitials;

	@ApiModelProperty(value = "球队图标")
	private String teamPic;

}

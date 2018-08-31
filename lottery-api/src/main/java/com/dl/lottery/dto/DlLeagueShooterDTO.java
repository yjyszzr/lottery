package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlLeagueShooterDTO {

	@ApiModelProperty(value = "射手榜")
	private List<DlLeagueShooterInfoDTO> leagueShooterInfoList;

}

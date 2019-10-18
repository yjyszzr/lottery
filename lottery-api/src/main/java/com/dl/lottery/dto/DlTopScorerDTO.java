package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlTopScorerDTO {

	@ApiModelProperty(value = "射手榜联赛名")
	private String LeagueName;
	@ApiModelProperty(value = "射手榜成员列表")
	List<DlLeagueShooterInfoDTO> leagueShooterInfoList;
	// private List<DlTopScorerMemberDTO> topScorerMemberList;
}

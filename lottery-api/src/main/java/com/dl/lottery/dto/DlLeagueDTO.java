package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlLeagueDTO {

	@ApiModelProperty("联赛id")
	private Integer leagueId;
	@ApiModelProperty("联赛名称")
	private String leagueName;
	@ApiModelProperty("联赛图标")
	private String leagueImg;
	@ApiModelProperty("联赛规则")
	private String leagueRule;
	@ApiModelProperty("联赛展示标题")
	private List<DlLeagueTabDTO> leagueTabs;
	
}

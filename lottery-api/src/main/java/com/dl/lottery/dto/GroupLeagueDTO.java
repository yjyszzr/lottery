package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupLeagueDTO {

	@ApiModelProperty(value = "分组id")
	private Integer groupId;
	
	@ApiModelProperty(value = "国家列表")
	private List<DlLeagueContryDTO> contrys;
	
	@ApiModelProperty(value = "杯赛列表")
	private List<LeagueInfoDTO> groupLeagues;
}

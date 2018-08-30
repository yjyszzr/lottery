package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlLeaguePageDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "分组名称")
	private String groupName;

	@ApiModelProperty(value = "分类")
	private Integer groupStatus;

	@ApiModelProperty(value = "联赛列表")
	private List<LeagueInfoDTO> leagueInfoList;

	@ApiModelProperty(value = "国家列表")
	private List<DlLeagueContryDTO> leagueContryList;

}

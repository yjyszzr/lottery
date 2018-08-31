package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlLeagueContryDTO {
	/*@ApiModelProperty(value = "id")
	private Integer id;*/
	@ApiModelProperty(value = "分组id")
	private Integer groupId;
	@ApiModelProperty(value = "国家名称")
	private String contryName;
	@ApiModelProperty(value = "国家Logo")
	private String contryPic;
	
	@ApiModelProperty(value = "联赛列表")
	private List<LeagueInfoDTO> leagueInfoList;
}
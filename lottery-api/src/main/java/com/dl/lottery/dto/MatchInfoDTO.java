package com.dl.lottery.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchInfoDTO implements Serializable{

	
	@ApiModelProperty(value = "联赛名称", required = true)
	public String leagueAddr;
	
	@ApiModelProperty(value = "主队名称", required = true)
	public String homeTeamAbbr;
	
	@ApiModelProperty(value = "客队名称", required = true)
	public String visitingTeamAbbr;
	
	@ApiModelProperty(value = "比赛日期", required = true)
	public String matchDay;
	
	@ApiModelProperty(value = "比赛比分", required = true)
	private String whole;
	
	@ApiModelProperty(value = "比赛结果", required = true)
	private String matchRs;
	

}

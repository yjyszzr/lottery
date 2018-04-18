package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchTeamInfosSumDTO implements Serializable{

	@ApiModelProperty(value = "赛事信息", required = true)
	private MatchInfoForTeamDTO matchInfo;
	
	@ApiModelProperty(value = "历史交锋", required = true)
	public MatchTeamInfoDTO hvMatchTeamInfo;
	
	@ApiModelProperty(value = "主场战绩", required = true)
	public MatchTeamInfoDTO hMatchTeamInfo;
	
	@ApiModelProperty(value = "客场战绩", required = true)
	private MatchTeamInfoDTO vMatchTeamInfo;
	
	@ApiModelProperty(value = "主场主战绩", required = true)
	public MatchTeamInfoDTO hhMatchTeamInfo;
	
	@ApiModelProperty(value = "客场客战绩", required = true)
	private MatchTeamInfoDTO vvMatchTeamInfo;
	
	@ApiModelProperty(value = "胜平负支持率", required = true)
	private TeamSupportDTO hadTeamSupport;
	
	@ApiModelProperty(value = "让球胜平负支持率", required = true)
	private TeamSupportDTO hhadTeamSupport;
	
}

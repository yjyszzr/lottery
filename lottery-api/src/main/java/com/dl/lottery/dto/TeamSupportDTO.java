package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeamSupportDTO implements Serializable{

	@ApiModelProperty(value = "胜支持率", required = true)
	private String hSupport;
	
	@ApiModelProperty(value = "平支持率", required = true)
	public String dSupport;
	
	@ApiModelProperty(value = "负支持率", required = true)
	public String aSupport;
	
	@ApiModelProperty(value = "让球数", required = true)
	private String fixedOdds;
	
	
	
	
}

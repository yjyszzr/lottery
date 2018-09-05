package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JCResultDTO {
	
	@ApiModelProperty(value = "让球胜平负")
	private String hhad;
	
	@ApiModelProperty(value = "胜平负")
	private String had;
	
	@ApiModelProperty(value = "比分")
	private String crs;
	
	@ApiModelProperty(value = "总进球")
	private String ttg;
	
	@ApiModelProperty(value = "半全场")
	private String hafu;

}

package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchResultDTO {

	@ApiModelProperty("投注内容")
	private Integer changciId;
	private Integer playType;
	private String playCode;
	private String cellCode;
	private String cellName;
	private Integer single;
	private String goalline;
	private Double odds;

}

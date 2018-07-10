package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchLineUpPersonDTO {

	@ApiModelProperty("队员id")
	private String personId;
	@ApiModelProperty("队员名称")
	private String personName;
	@ApiModelProperty("队员位置")
	private String position;
	@ApiModelProperty("队员位置X")
	private String positionX;
	@ApiModelProperty("队员位置Y")
	private String positionY;
	@ApiModelProperty("队员编号")
	private String shirtNumber;
	
}

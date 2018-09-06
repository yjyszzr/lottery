package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeamParam {

	@ApiModelProperty(value = "球队Id")
	private Integer teamId;

}

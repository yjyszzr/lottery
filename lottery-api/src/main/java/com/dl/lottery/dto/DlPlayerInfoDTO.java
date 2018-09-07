package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlPlayerInfoDTO {

	@ApiModelProperty(value = "球员名称")
	private String playerName;
}

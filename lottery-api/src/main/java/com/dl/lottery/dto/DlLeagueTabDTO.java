package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlLeagueTabDTO {

	@ApiModelProperty("标题")
	private String tabTitle;
	@ApiModelProperty("内容")
	private Object tabContent;
}

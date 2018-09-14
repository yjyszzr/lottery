package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlMatchGroupDTO {

	@ApiModelProperty(value = "分组名称")
	private String groupName;
	@ApiModelProperty(value = "分组Id")
	private Integer groupId;
}

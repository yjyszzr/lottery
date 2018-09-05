package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class KeyValueDTO {
	
	@ApiModelProperty(value = "键名称")
	public String keyName;
	
	@ApiModelProperty(value = "键值")
	public String keyValue;

}

package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActTypeParam {
	
	@ApiModelProperty(value="活动分类")
	private Integer actType;

}

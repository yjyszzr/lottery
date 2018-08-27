package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DiscoveryPageParam {

	@ApiModelProperty(value = "页码")
	private Integer page;
	@ApiModelProperty(value = "每页显示数量")
	private Integer size;
}

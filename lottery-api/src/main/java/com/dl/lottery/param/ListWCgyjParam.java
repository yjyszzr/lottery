package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListWCgyjParam {

	@ApiModelProperty("筛选条件，用','分隔")
	private String countryIds;
}

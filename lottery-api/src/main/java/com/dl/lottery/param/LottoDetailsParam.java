package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LottoDetailsParam {

	@ApiModelProperty(value = "期号")
	private Integer termNum;
}

package com.dl.lottery.param;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArtifiLotteryQueryParamV2 {
	
	@ApiModelProperty(value="锚点")
	@NotEmpty
	private Integer type; //0自动刷新  1按钮手动刷新
}

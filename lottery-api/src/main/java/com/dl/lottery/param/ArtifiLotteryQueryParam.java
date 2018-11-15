package com.dl.lottery.param;

import org.hibernate.validator.constraints.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArtifiLotteryQueryParam {
	@ApiModelProperty(value="电话号")
	private String mobile;
	
	@ApiModelProperty(value="锚点")
	@NotEmpty
	private Integer startId;
}

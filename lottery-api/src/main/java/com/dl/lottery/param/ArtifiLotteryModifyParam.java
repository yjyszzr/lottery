package com.dl.lottery.param;

import org.hibernate.validator.constraints.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArtifiLotteryModifyParam {
	
	@ApiModelProperty(value="电话号")
	@NotEmpty
	private String mobile;
	
	@ApiModelProperty(value="订单号")
	@NotEmpty
	private String orderSn;

	@ApiModelProperty(value="订单状态  0待确认1出票成功2出票失败,默认为待确认状态")
	@NotEmpty
	private Integer operationStatus;
}

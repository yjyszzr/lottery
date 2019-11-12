package com.dl.lottery.param;

import org.hibernate.validator.constraints.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArtifiLotteryModifyParamV2 {
	@ApiModelProperty(value="订单号")
	@NotEmpty
	private String orderSn;
	
	@ApiModelProperty(value="订单状态  0待确认1出票成功2出票失败,默认为待确认状态")
	@NotEmpty
	private Integer orderStatus;

	@ApiModelProperty(value="出票图片地址")
	private String picUrl;
	
	@ApiModelProperty(value="失败原因")
	private String failMsg;
	
	@ApiModelProperty(value="每日赛事值为5")
	private Integer appCode;
}

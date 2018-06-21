package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class PrintLotterysRefundsByOrderSnParam {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "订单号", required = true)
	private String orderSn;
}

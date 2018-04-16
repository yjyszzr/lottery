package com.dl.lottery.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author zhangzirong
 */
@Data
public class GetBetInfoByOrderSn {
	
    @ApiModelProperty(value = "订单编码")
    private String orderSn;
}

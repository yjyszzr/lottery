package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class NotifyParam {

    @ApiModelProperty(value="回调地址")
    @NotEmpty(message = "回调地址不能为空")
    private String notifyUrl;

    @ApiModelProperty(value="商户号")
    @NotEmpty(message = "商户号不能为空")
    private String merchantOrderSn;

}

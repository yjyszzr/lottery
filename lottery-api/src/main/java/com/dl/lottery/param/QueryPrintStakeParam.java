package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class QueryPrintStakeParam {

    @ApiModelProperty(value="代理商户号 示例：test")
    @NotEmpty(message = "商户号不能为空")
    private String merchant;

    @ApiModelProperty(value="协议版本号 示例：1.0")
    @NotEmpty(message = "协议版本号不能为空")
    private String version;

    @ApiModelProperty(value="时间戳 示例：2019-02-04 16:37:35")
    @NotEmpty(message = "协议版本号不能为空")
    private String timestamp;

    @ApiModelProperty(value="商户订单号")
    @NotEmpty(message = "商户订单号不能为空")
    private String merchantOrderSn;

}

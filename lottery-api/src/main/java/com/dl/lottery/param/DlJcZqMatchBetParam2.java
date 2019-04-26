package com.dl.lottery.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlJcZqMatchBetParam2 extends DlJcZqMatchBetParam implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="代理商户号 示例：test")
	private String merchant;
	@ApiModelProperty(value="协议版本号 示例：1.0")
	private String version;
	@ApiModelProperty(value="时间戳 示例：2019-02-04 16:37:35")
	private String timestamp;
	@ApiModelProperty(value="商户订单号 注：对应您方彩票的唯一标识")
	private String merchantOrderSn;
	@ApiModelProperty(value="签名")
	private String sign;
}

package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlJcZqMatchBetParam2 extends DlJcZqMatchBetParam {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="代理商户号 示例：test")
	private String merchant;
	@ApiModelProperty(value="协议版本号 示例：1.0")
	private String version;
	@ApiModelProperty(value="时间戳 示例：2019-02-04 16:37:35")
	private String timestamp;
	

}

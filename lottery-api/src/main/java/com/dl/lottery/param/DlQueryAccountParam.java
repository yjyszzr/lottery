package com.dl.lottery.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlQueryAccountParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "代理商编号", required = true)
    private String merchant;
	
	@ApiModelProperty(value = "版本号", required = true)
    private String version;
	
	@ApiModelProperty(value = "时间戳", required = true)
    private String timestamp;
}

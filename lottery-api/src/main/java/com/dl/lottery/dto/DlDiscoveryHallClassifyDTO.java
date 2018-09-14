package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlDiscoveryHallClassifyDTO {

	@ApiModelProperty("发现页分类id")
	private String classifyId;
	
	@ApiModelProperty("名称")
	private String className;

	@ApiModelProperty("logo")
	private String classImg;

	@ApiModelProperty("是否上线 0 待上线,1上线")
	private Integer status;

	@ApiModelProperty("状态说明")
	private String statusReason;

	@ApiModelProperty("跳转链接")
	private String redirectUrl;
}
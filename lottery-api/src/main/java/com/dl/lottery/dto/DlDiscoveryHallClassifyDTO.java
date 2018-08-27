package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlDiscoveryHallClassifyDTO {

	@ApiModelProperty("名称")
	private String className;

	@ApiModelProperty("logo")
	private String classImg;

	@ApiModelProperty("副标题")
	private String subTitle;

	@ApiModelProperty("跳转链接")
	private String redirectUrl;
}
package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlBannerForActive {

	@ApiModelProperty(value = "banner名称")
	public String bannerName;

	@ApiModelProperty(value = "banner大图")
	public String bannerImage;

	@ApiModelProperty(value = "图片跳转链接")
	public String bannerLink;

}
package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActivityDTO {
	
	@ApiModelProperty(value="标题")
	private String title;
	@ApiModelProperty(value=" 描述/ 详情/ 说明")
	private String detail;
	@ApiModelProperty(value="图标")
	private String icon;
	@ApiModelProperty(value="活动跳转链接")
	private String actUrl;
	
}

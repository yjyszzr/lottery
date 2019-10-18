package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LogPicDetailDTO {
	
	@ApiModelProperty(value = "彩票照片url")
	private String picUrl = "";
	
	@ApiModelProperty(value = "1已出票，2出票失败")
	private String optType = "";
		
	@ApiModelProperty(value = "操作日期")
	private String dateStr = "";
	
	@ApiModelProperty(value = "出票失败原因")
	private String failReason = "";
	
	
}

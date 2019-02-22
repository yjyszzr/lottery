package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResponseDTO {
	
	@ApiModelProperty(value="结果")
	private String code;

	@ApiModelProperty(value="数据")
	private String data;

	@ApiModelProperty(value="消息")
	private String msg;

	@ApiModelProperty(value="成功")
	private String success;

	@ApiModelProperty(value="商户号")
	private String merchant;

	@ApiModelProperty(value="版本")
	private String version;

	@ApiModelProperty(value="时间戳")
	private String time;

}

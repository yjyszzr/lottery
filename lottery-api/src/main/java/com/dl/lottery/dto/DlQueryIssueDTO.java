package com.dl.lottery.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlQueryIssueDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "代理商编号", required = true)
    private String merchant;
	
	@ApiModelProperty(value = "版本号", required = true)
    private String version;
	
	@ApiModelProperty(value = "时间戳", required = true)
    private String timestamp;
	
	@ApiModelProperty(value = "返回码", required = true)
    private String retCode;
	
	@ApiModelProperty(value = "返回码描述信息", required = true)
    private String retDesc;
	
	@ApiModelProperty(value = "订单详情", required = true)
    private QueryIssue issue;
	
	@Data
	public static class QueryIssue {
		
		@ApiModelProperty(value = "游戏id", required = true)
	    private String game;
		
		@ApiModelProperty(value = "期次", required = true)
		private String issue;
		
		@ApiModelProperty(value = "开售时间", required = true)
		private String beginTime;
		
		@ApiModelProperty(value = "停售时间", required = true)
		private String stopTime;
		
		@ApiModelProperty(value = "官方截至时间", required = true)
		private String endTime;
		
		@ApiModelProperty(value = "期次状态 0-待开售 8-销售中 12-已停售 16-已开奖", required = true)
		private Integer status;
		
		@ApiModelProperty(value = "开奖号码", required = true)
		private String result;
	}
}

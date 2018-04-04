package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlToStakeDTO implements Serializable{
	
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
	
	@ApiModelProperty(value = "返回订单详情", required = true)
    private List<BackOrderDetail> orders;
	
	@Data
	public static class BackOrderDetail {
		
		@ApiModelProperty(value = "商户订单号", required = true)
	    private String ticketId;
		
		@ApiModelProperty(value = "中心平台订单编号", required = true)
	    private String platformId;
		
		@ApiModelProperty(value = "处理结果", required = true)
	    private Integer errorCode;
	}
}

package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlQueryStakeDTO implements Serializable{
	
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
    private List<BackQueryStake> orders;
	
	@Data
	public static class BackQueryStake {
		
		@ApiModelProperty(value = "返回码 0-成功 4-票不存在", required = true)
	    private Integer status;
		
		@ApiModelProperty(value = "订单id", required = true)
	    private String orderId;
		
		@ApiModelProperty(value = "商户订单号", required = true)
	    private String ticketId;
		
		@ApiModelProperty(value = "中心平台订单编号", required = true)
	    private String platformId;
		
		@ApiModelProperty(value = "订单状态 8-出票中 16-成功 17-失败", required = true)
	    private Integer printStatus;
		
		@ApiModelProperty(value = "赔率数据", required = true)
	    private String sp;
		
		@ApiModelProperty(value = "票面上的票号", required = true)
	    private String printNo;
		
		@ApiModelProperty(value = "票面上的出票时间", required = true)
	    private String printTime;
	}
}

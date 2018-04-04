package com.dl.lottery.param;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlCallbackStakeParam implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "返回出票详情", required = true)
    private List<CallbackStake> orders;
	
	@Data
	public static class CallbackStake {
		
		@ApiModelProperty(value = "商户订单号", required = true)
	    private String ticketId;
		
		@ApiModelProperty(value = "中心平台订单编号", required = true)
	    private String platformId;
		
		@ApiModelProperty(value = "订单状态", required = true)
	    private Integer printStatus;
		
		@ApiModelProperty(value = "赔率数据", required = true)
	    private String sp;
		
		@ApiModelProperty(value = "票面上的票号", required = true)
	    private String printNo;
		
		@ApiModelProperty(value = "票面上的出票时间", required = true)
	    private String printTime;
	}
}

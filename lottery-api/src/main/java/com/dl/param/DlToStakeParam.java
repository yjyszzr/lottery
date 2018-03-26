package com.dl.param;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlToStakeParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "代理商编号", required = true)
    private String merchant;
	
	@ApiModelProperty(value = "版本号", required = true)
    private String version;
	
	@ApiModelProperty(value = "时间戳", required = true)
    private String timestamp;
	
	@ApiModelProperty(value = "订单详情", required = true)
    private List<PrintTicketOrderDTO> orders;
	
	@Data
	public static class PrintTicketOrderDTO {
		
		@ApiModelProperty(value = "商户订单号", required = true)
	    private String ticketId;
		
		@ApiModelProperty(value = "游戏编号", required = true)
		private String game;
		
		@ApiModelProperty(value = "期次", required = true)
		private String issue;
		
		@ApiModelProperty(value = "玩法", required = true)
		private String playType;
		
		@ApiModelProperty(value = "投注方式", required = true)
		private String betType;
		
		@ApiModelProperty(value = "倍数", required = true)
		private Integer times;
		
		@ApiModelProperty(value = "订单金额", required = true)
		private Integer money;
		
		@ApiModelProperty(value = "投注号码", required = true)
		private String stakes;
	}
}

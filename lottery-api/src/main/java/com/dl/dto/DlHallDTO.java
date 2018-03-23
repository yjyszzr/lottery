package com.dl.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlHallDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "中奖信息列表")
	public List<DlWinningLogDTO> winningMsgs;

	
	@Data
	public static class DlWinningLogDTO {
		
		@ApiModelProperty(value = "中奖信息")
		public String winningMsg;
		
		@ApiModelProperty(value = "中奖金额")
		public String winningMoney;
	}
}

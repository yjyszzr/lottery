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
	
	@ApiModelProperty(value = "彩票分类列表")
	public List<DlLotteryClassifyDTO> lotteryClassifys;

	@Data
	public static class DlWinningLogDTO {
		
		@ApiModelProperty(value = "中奖信息")
		public String winningMsg;
		
		@ApiModelProperty(value = "中奖金额")
		public String winningMoney;
	}
	
	@Data
	public static class DlLotteryClassifyDTO {
		
		@ApiModelProperty(value = "彩票名称")
		public String lotteryName;
		
		@ApiModelProperty(value = "彩票图片")
		public String lotteryImg;
		
		@ApiModelProperty(value = "彩票状态 0-售卖 1-停售")
		public Integer status;
	}
}

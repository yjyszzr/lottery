package com.dl.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlHallDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "活动数据")
	public DlActivityDTO activity;
	
	@ApiModelProperty(value = "中奖信息列表")
	public List<DlWinningLogDTO> winningMsgs;
	
	@ApiModelProperty(value = "彩票分类列表")
	public List<DlLotteryClassifyDTO> lotteryClassifys;
	
	@Data
	public static class DlActivityDTO {
		
		@ApiModelProperty(value = "活动标题")
		public String actTitle;
		
		@ApiModelProperty(value = "活动图片")
		public String actImg;
		
		@ApiModelProperty(value = "活动跳转url")
		public String actUrl;
	}

	@Data
	public static class DlWinningLogDTO {
		
		@ApiModelProperty(value = "中奖信息")
		public String winningMsg;
		
		@ApiModelProperty(value = "中奖金额")
		public String winningMoney;
	}
	
	@Data
	public static class DlLotteryClassifyDTO {
		
		@ApiModelProperty(value = "彩票分类id")
		public Integer lotteryId;

		@ApiModelProperty(value = "彩票名称")
		public String lotteryName;
		
		@ApiModelProperty(value = "彩票图片")
		public String lotteryImg;
		
		@ApiModelProperty(value = "彩票状态 0-售卖 1-停售")
		public Integer status;
	}
}

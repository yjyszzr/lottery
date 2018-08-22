package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlHallDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "首页轮播图列表")
	public List<DlNavBannerDTO> navBanners;
	
	@ApiModelProperty(value = "活动数据")
	public DlActivityDTO activity;
	
	@ApiModelProperty(value = "中奖信息列表")
	public List<DlWinningLogDTO> winningMsgs;
	
	//第一版只显示竞彩足球的子列表
	@ApiModelProperty(value = "彩票分类列表")
	public List<DlLotteryClassifyDTO> lotteryClassifys;
	
	@ApiModelProperty(value = "玩法分类明细")
	public List<DlPlayClassifyDetailDTO> dlPlayClassifyDetailDTOs;
	
	@Data
	public static class DlNavBannerDTO {
		
		@ApiModelProperty(value = "banner名称")
		public String bannerName;
		
		@ApiModelProperty(value = "banner大图")
		public String bannerImage;
		
		@ApiModelProperty(value = "图片跳转链接")
		public String bannerLink;
		
		@ApiModelProperty(value = "banner图生效起始时间")
		public Integer startTime;
		
		@ApiModelProperty(value = "banner图生效截止时间")
		public Integer endTime;
	}
	
	@Data
	public static class DlActivityDTO {
		
		@ApiModelProperty(value = "活动标题")
		public String actTitle;
		
		@ApiModelProperty(value = "活动图片")
		public String actImg;
		
		@ApiModelProperty(value = "活动链接")
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
		public String lotteryId;

		@ApiModelProperty(value = "彩票名称")
		public String lotteryName;
		
		@ApiModelProperty(value = "彩票图片")
		public String lotteryImg;
		
		@ApiModelProperty(value = "彩票状态 0-售卖 1-停售")
		public String status;
		
		@ApiModelProperty(value = "副标题")
		public String subTitle;
		
		@ApiModelProperty(value = "跳转链接地址")
		public String redirectUrl;
		
	}
}

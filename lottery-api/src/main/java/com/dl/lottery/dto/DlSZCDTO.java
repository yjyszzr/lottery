package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlSZCDTO {
	
	@ApiModelProperty(value = "彩票分类")
	private String lotteryClassify ="";
	
	@ApiModelProperty(value = "彩票名称")
	private String lotteryName ="";
	
	@ApiModelProperty(value = "期号")
	private Integer termNum;

	@ApiModelProperty(value = "期次")
	private String period;

	@ApiModelProperty(value = "开奖日期")
	private String prizeDate;

	@ApiModelProperty(value = "红球中奖号码")
	private List<String> redPrizeNumList;

	@ApiModelProperty(value = "红球中奖号码")
	private List<String> bluePrizeNumList;

}
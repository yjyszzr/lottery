package com.dl.lottery.dto;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SZCPrizeDTO {
	
	@ApiModelProperty(value = "彩票分类")
	private String lotteryClassify ="";
	
	@ApiModelProperty(value = "彩票名称")
	private String lotteryName ="";
	
	@ApiModelProperty(value = "数字彩开奖列表")
	private PageInfo<DlSZCDTO> szcPrizePageInfo;
}

package com.dl.lottery.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JCResultDTO {
	
	@ApiModelProperty(value = "彩票分类")
	private String lotteryClassify = "";
	
	@ApiModelProperty(value = "彩票名称")
	private String lotteryName = "";
	
	@ApiModelProperty(value = "日期")
	private String dateStr = "";
	
	@ApiModelProperty(value = "已经开奖的字符串")
	private String prizeMatchStr = "";
	
	@ApiModelProperty(value = "集合")
	private List<LeagueMatchResultDTO> list = new ArrayList<>();

}

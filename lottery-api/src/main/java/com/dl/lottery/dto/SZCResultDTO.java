package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SZCResultDTO {
	
	@ApiModelProperty(value = "彩票分类")
	private String lotteryClassify;
	
	@ApiModelProperty(value = "彩票名称")
	private String lotteryName;
	
	@ApiModelProperty(value = "期次")
	private String period;

	@ApiModelProperty(value = "开奖日期")
	private String prizeDate;

	@ApiModelProperty(value = "红球中奖号码")
	private List<String> redPrizeNumList;

	@ApiModelProperty(value = "蓝球中奖号码")
	private List<String> bluePrizeNumList;

	@ApiModelProperty(value = "奖金池")
	private String prizes;

	@ApiModelProperty(value = "销售金额")
	private String sellAmount;

	@ApiModelProperty(value = "获奖详情")
	private List<DlSuperLottoRewardDetailsDTO> superLottoRewardDetailsList;
}

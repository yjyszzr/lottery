package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlSuperLottoRewardDetailsDTO {

	@ApiModelProperty(value = "获奖级别1，2，3，4，5，6")
	private Integer rewardLevel;
	
	@ApiModelProperty(value = "获奖级别名称")
	private String rewardLevelName;

	@ApiModelProperty(value = "中奖注数")
	private Integer rewardNum;

	@ApiModelProperty(value = "单注奖金")
	private Integer rewardPrice;

//	@ApiModelProperty(value = "追加中奖注数")
//	private Integer rewardNum2;
//
//	@ApiModelProperty(value = "追加单注奖金")
//	private Integer rewardPrice2;
}

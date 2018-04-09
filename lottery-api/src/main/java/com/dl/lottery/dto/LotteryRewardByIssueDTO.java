package com.dl.lottery.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LotteryRewardByIssueDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("期次号")
	private String issue;
	
	@ApiModelProperty("中奖数据")
	private String rewardData;
}

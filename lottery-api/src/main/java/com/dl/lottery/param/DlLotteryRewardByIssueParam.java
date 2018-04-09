package com.dl.lottery.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlLotteryRewardByIssueParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("期次号")
	private String issue;
}

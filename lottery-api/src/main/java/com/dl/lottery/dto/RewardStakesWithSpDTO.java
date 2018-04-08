package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RewardStakesWithSpDTO {

	@ApiModelProperty(value="获胜期次的赔率集合")
	private List<Double> winSpList;
	
	@ApiModelProperty(value="获胜的 期次@赔率 集合")
	private String winIssueSpStr;
}

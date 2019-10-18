package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlTopScorerMemberDTO {

	@ApiModelProperty(value = "排名")
	private Integer ranking;
	@ApiModelProperty(value = "射手榜球员名称")
	private String memberName;
	@ApiModelProperty(value = "射手榜队名")
	private String topScorerTeam;
	@ApiModelProperty(value = "总进球")
	private Integer totalGoal;

}

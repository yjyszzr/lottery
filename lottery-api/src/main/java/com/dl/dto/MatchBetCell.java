package com.dl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchBetCell {

	@ApiModelProperty(value="该场次玩法")
	private String playType;
	@ApiModelProperty(value="投注号码，同一场次的不同选项用','分隔")
	private String cellCode;
	@ApiModelProperty(value="赔率")
	private String cellOdds;
	@ApiModelProperty(value="投注赛事编码")
	private String playCode;
	@ApiModelProperty(value = "场次:周三001", required = true)
	public String changci;
	@ApiModelProperty(value="是否设胆，0：否，1是")
	private int isDan;
}

package com.dl.shop.lottery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ZqMatchBetCell {

	@ApiModelProperty(value="投注号码用','分隔")
	private String cellCode;
	@ApiModelProperty(value="赔率,用','分隔，与cellCode一一对应")
	private double cellOdds;
	@ApiModelProperty(value="投注赛事编码")
	private String playCode;
	@ApiModelProperty(value = "场次:周三001", required = true)
	public String changci;
	@ApiModelProperty(value="是否设胆，0：否，1是")
	private int isDan;
}

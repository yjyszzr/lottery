package com.dl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DLBetMatchCellDTO {

	@ApiModelProperty("投注内容")
	private String betContent;
	@ApiModelProperty("投注方式")
	private String betType;
	@ApiModelProperty("玩法")
	private String playType;
	@ApiModelProperty("投注倍数")
	private Integer times;
	@ApiModelProperty("投注奖金")
	private Double amount;
}

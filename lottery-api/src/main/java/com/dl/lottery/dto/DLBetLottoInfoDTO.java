package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DLBetLottoInfoDTO {

	@ApiModelProperty("出票编号")
	private String ticketId;
	@ApiModelProperty("投注号码")
	private String stakes;
	@ApiModelProperty("投注方式")
	private String betType;
	@ApiModelProperty("玩法")
	private String playType;
	@ApiModelProperty("投注倍数")
	private Integer times;
	@ApiModelProperty("投注奖金")
	private Double amount;
	@ApiModelProperty("出票状态， 0-待出票 1-已出票 2-出票失败 3-出票中")
	private Integer status;
}

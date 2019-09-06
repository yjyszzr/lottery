package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchBasketBallBetCellDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="该场次玩法")
	private String playType;
	@ApiModelProperty(value="投注选项")
	private List<DlJcLqMatchCellDTO> betCells;
	@ApiModelProperty(value = "单场，1可以单场，0不可以", required = true)
	private Integer single;
	@ApiModelProperty("让球数")
	private String fixedOdds;
	@ApiModelProperty("预设分数")
	private String forecastScore;
}

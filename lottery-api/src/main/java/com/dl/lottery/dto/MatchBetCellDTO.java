package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchBetCellDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="该场次玩法")
	private String playType;
	@ApiModelProperty(value="投注选项")
	private List<DlJcZqMatchCellDTO> betCells;
}

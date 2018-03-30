package com.dl.param;

import java.io.Serializable;
import java.util.List;

import com.dl.dto.MatchBetCellDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlJcZqMatchBetParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="投注倍数")
	private Integer times;
	@ApiModelProperty(value="投注方式,如：31代表3串1")
	private String betType;
	@ApiModelProperty(value="该投票玩法")
	private String playType;
	@ApiModelProperty(value="投注详细")
	private List<MatchBetCellDTO> matchBetCells;
	
}

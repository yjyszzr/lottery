package com.dl.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchBetCellDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="赛事id")
	private String matchId;
	@ApiModelProperty(value = "场次:周三001", required = true)
	public String changci;
	@ApiModelProperty(value="是否设胆，0：否，1是")
	private int isDan;
	@ApiModelProperty(value="彩票种类")
	private int lotteryClassifyId;
	@ApiModelProperty(value="彩票玩法类别")
	private int lotteryPlayClassifyId;
	@ApiModelProperty(value="投注场次队名")
	private String matchTeam;
	@ApiModelProperty(value="该场次玩法")
	private String playType;
	@ApiModelProperty(value="投注赛事编码")
	private String playCode;
	@ApiModelProperty(value="投注选项")
	private List<DlJcZqMatchCellDTO> betCells;
}

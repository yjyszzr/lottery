package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("每场赛事玩法信息")
@Data
public class DlJcLqMatchPlayDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "玩法类型", required = true)
	private Integer playType;
	
	@ApiModelProperty(value = "玩法内容", required = true)
	public String playContent;
	
	@ApiModelProperty(value = "主队选项，胜平负，让球胜平负使用主客平三选项，比分也使用三选项（三个选项只有名称，没有赔率）", required = true)
	private DlJcZqMatchCellDTO homeCell;
	
	@ApiModelProperty(value = "客队选项", required = true)
	private DlJcZqMatchCellDTO visitingCell;
	
	@ApiModelProperty(value = "平局选项", required = true)
	private DlJcZqMatchCellDTO flatCell;
	
	@ApiModelProperty(value = "比赛所有选项，半全场，总进球数只使用这个属性，不使用homeCell，visitingCell，flatCell", required = true)
	private List<DlJcZqMatchCellDTO> matchCells;
	
	@ApiModelProperty(value = "让球数", required = true)
	private String fixedOdds;
	
	@ApiModelProperty(value = "单场，1可以单场，0不可以", required = true)
	private Integer single=0;
	
	@ApiModelProperty(value = "是否有数据，1有，0无", required = true)
	private Integer isShow=1;
}

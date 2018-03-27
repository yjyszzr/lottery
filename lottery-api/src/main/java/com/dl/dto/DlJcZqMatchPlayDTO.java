package com.dl.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlJcZqMatchPlayDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "联赛id", required = true)
	public String leagueId;
	
	@ApiModelProperty(value = "联赛名称", required = true)
	public String leagueName;
	
	@ApiModelProperty(value = "联赛名称", required = true)
	public String leagueAddr;
	
	@ApiModelProperty(value = "场次id", required = true)
	public String changciId;
	
	@ApiModelProperty(value = "场次:周三001", required = true)
	public String changci;
	
	@ApiModelProperty(value = "主队id", required = true)
	public Integer homeTeamId;
	
	@ApiModelProperty(value = "主队名称", required = true)
	public String homeTeamName;
	
	@ApiModelProperty(value = "主队名称", required = true)
	public String homeTeamAbbr;
	
	@ApiModelProperty(value = "主队名称", required = true)
	public String homeTeamRank;
	
	@ApiModelProperty(value = "客队id", required = true)
	public String visitingTeamId;
	
	@ApiModelProperty(value = "客队名称", required = true)
	public String visitingTeamName;
	
	@ApiModelProperty(value = "客队名称", required = true)
	public String visitingTeamAbbr;
	
	@ApiModelProperty(value = "客队名称", required = true)
	public String visitingTeamRank;
	
	@ApiModelProperty(value = "比赛日期", required = true)
	public String matchDay;
	
	@ApiModelProperty(value = "比赛时间", required = true)
	public Date matchTime;
	
	@ApiModelProperty(value = "玩法内容", required = true)
	public String playContent;
	
	@ApiModelProperty(value = "玩法类型", required = true)
	private Integer playType;
	
	@ApiModelProperty(value = "是否热度", required = true)
	private Integer isHot;
	
	@ApiModelProperty(value = "赛事编码", required = true)
	private String playCode;
	
	@ApiModelProperty(value = "主队选项", required = true)
	private DlJcZqMatchCellDTO homeCell;
	
	@ApiModelProperty(value = "客队选项", required = true)
	private DlJcZqMatchCellDTO visitingCell;
	
	@ApiModelProperty(value = "平局选项", required = true)
	private DlJcZqMatchCellDTO flatCell;
	
	@ApiModelProperty(value = "比赛所有选项", required = true)
	private List<DlJcZqMatchCellDTO> matchCells;
	
}

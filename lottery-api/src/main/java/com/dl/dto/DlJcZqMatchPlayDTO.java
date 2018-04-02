package com.dl.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("每场赛事信息")
@Data
public class DlJcZqMatchPlayDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="赛事id")
	private String matchId;
	
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
	
	@ApiModelProperty(value = "主队选项，胜平负，让球胜平负使用主客平三选项，比分也使用三选项（三个选项只有名称，没有赔率）", required = true)
	private DlJcZqMatchCellDTO homeCell;
	
	@ApiModelProperty(value = "客队选项", required = true)
	private DlJcZqMatchCellDTO visitingCell;
	
	@ApiModelProperty(value = "平局选项", required = true)
	private DlJcZqMatchCellDTO flatCell;
	
	@ApiModelProperty(value = "比赛所有选项，半全场，总进球数只使用这个属性，不使用homeCell，visitingCell，flatCell", required = true)
	private List<DlJcZqMatchCellDTO> matchCells;
	
}

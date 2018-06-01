package com.dl.lottery.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchInfoForTeamDTO implements Serializable{

	
	@ApiModelProperty(value="赛事id")
	private Integer matchId;
	
	@ApiModelProperty(value = "联赛名称", required = true)
	public String leagueAddr;
	
	@ApiModelProperty(value = "场次id", required = true)
	public Integer changciId;
	
	@ApiModelProperty(value = "场次:周三001", required = true)
	public String changci;
	
	@ApiModelProperty(value = "主队id", required = true)
	public Integer homeTeamId;
	
	@ApiModelProperty(value = "主队名称", required = true)
	public String homeTeamAbbr;
	
	@ApiModelProperty(value = "主队排名", required = true)
	public String homeTeamRank;
	
	@ApiModelProperty(value = "主队图标", required = true)
	public String homeTeamPic;
	
	@ApiModelProperty(value = "客队id", required = true)
	public Integer visitingTeamId;
	
	@ApiModelProperty(value = "客队名称", required = true)
	public String visitingTeamAbbr;
	
	@ApiModelProperty(value = "客队排名", required = true)
	public String visitingTeamRank;
	
	@ApiModelProperty(value = "客队图标", required = true)
	public String visitingTeamPic;
	
	@ApiModelProperty(value = "比赛时间", required = true)
	public int matchTime;
	
	@ApiModelProperty(value = "胜赔率", required = true)
	public String hOdds;
	
	@ApiModelProperty(value = "平赔率", required = true)
	public String dOdds;
	
	@ApiModelProperty(value = "负赔率", required = true)
	public String aOdds;
	
}

package com.dl.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlJcZqMatchCellDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "联赛id", required = true)
	public String leagueId;
	
	@ApiModelProperty(value = "联赛名称", required = true)
	public String leagueName;
	
	@ApiModelProperty(value = "场次id", required = true)
	public String changCiId;
	
	@ApiModelProperty(value = "场次:周三001", required = true)
	public String changCi;
	
	@ApiModelProperty(value = "主队id", required = true)
	public Integer hId;
	
	@ApiModelProperty(value = "主队名称", required = true)
	public String hTeam;
	
	@ApiModelProperty(value = "客队id", required = true)
	public String vId;
	
	@ApiModelProperty(value = "客队名称", required = true)
	public String vTeam;
	
	@ApiModelProperty(value = "比赛日期", required = true)
	public String matchDay;
	
	@ApiModelProperty(value = "比赛时间", required = true)
	public String matchTime;
	
	@ApiModelProperty(value = "显示时间", required = true)
	public String showTime;
	
	/** 胜平负 */
	@ApiModelProperty(value = "胜平负-胜赔率", required = true)
	public String spfBetWinBl;
	
	@ApiModelProperty(value = "胜平负-平赔率", required = true)
	public String spfBetPingBl;
	
	@ApiModelProperty(value = "胜平负-负赔率", required = true)
	public String spfBetFuBl;
	
	/** 让球胜平负 */
	@ApiModelProperty(value = "让球胜平负-胜赔率", required = true)
	public String rqspfBetWinBl;
	
	@ApiModelProperty(value = "让球胜平负-平赔率", required = true)
	public String rqspfBetPingBl;
	
	@ApiModelProperty(value = "让球胜平负-负赔率", required = true)
	public String rqspfBetFuBl;
	
	/** 半全场胜平负 */
	@ApiModelProperty(value = "半全场胜平负-胜赔率", required = true)
	public String bqspfBetWinBl;
	
	@ApiModelProperty(value = "半全场胜平负-平赔率", required = true)
	public String bqspfBetPingBl;
	
	@ApiModelProperty(value = "半全场胜平负-负赔率", required = true)
	public String bqspfBetFuBl;
	
	/** 比分 */
	@ApiModelProperty(value = "比分-胜赔率", required = true)
	public String bfBetWinBl;
	
	@ApiModelProperty(value = "比分-平赔率", required = true)
	public String bfBetPingBl;
	
	@ApiModelProperty(value = "比分-负赔率", required = true)
	public String bfBetFuBl;
	
	/** 总进球 */
	@ApiModelProperty(value = "总进球-胜赔率", required = true)
	public String zjqBetWinBl;
	
	@ApiModelProperty(value = "总进球-平赔率", required = true)
	public String zjqBetPingBl;
	
	@ApiModelProperty(value = "总进球-负赔率", required = true)
	public String zjqBetFuBl;
}

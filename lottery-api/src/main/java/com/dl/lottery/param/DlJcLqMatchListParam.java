package com.dl.lottery.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlJcLqMatchListParam implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "赛事玩法类型：1-让分胜负,2-胜负,3-胜分差,4-大小分, 6-混合投注", required = true)
    private String playType;
	@ApiModelProperty(value = "筛选条件id,e用‘,’分隔")
	private String leagueId;
}

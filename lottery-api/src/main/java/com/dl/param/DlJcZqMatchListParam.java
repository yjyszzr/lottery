package com.dl.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlJcZqMatchListParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "赛事玩法类型", required = true)
    private String playType;
	@ApiModelProperty(value = "筛选条件id")
	private Integer leagueId;
}

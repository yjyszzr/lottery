package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class LeagueListByGroupIdParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "联赛分组Id")
	private Integer groupId;
}

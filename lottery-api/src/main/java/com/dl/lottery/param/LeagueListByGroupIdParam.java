package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class LeagueListByGroupIdParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "联赛分组Id：0热门，1欧洲，2美洲，3亚洲，4国际")
	private Integer groupId;
}

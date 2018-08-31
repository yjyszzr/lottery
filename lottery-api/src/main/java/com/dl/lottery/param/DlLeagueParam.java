package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class DlLeagueParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "联赛国家Id")
	private Integer contryId;
}

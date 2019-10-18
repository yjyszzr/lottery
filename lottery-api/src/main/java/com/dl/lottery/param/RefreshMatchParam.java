package com.dl.lottery.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RefreshMatchParam implements Serializable{

	@ApiModelProperty(value="比赛场次id")
	private Integer changciId;
}

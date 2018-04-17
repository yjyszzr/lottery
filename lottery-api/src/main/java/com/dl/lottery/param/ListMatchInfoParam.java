package com.dl.lottery.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListMatchInfoParam implements Serializable{

	@ApiModelProperty(value="比赛场次id")
	private Integer changciId;
	@ApiModelProperty(value="页码")
	Integer page;
	@ApiModelProperty(value="每页显示数量，默认10")
	Integer size;
}

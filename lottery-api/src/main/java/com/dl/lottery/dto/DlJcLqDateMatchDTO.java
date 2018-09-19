package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("按日期分类的赛事信息")
@Data
public class DlJcLqDateMatchDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "当日比赛日期", required = true)
	public String matchDay;
	
	@ApiModelProperty(value = "用来排序的字段 ", required = true)
	public String sortMatchDay;
	
	@ApiModelProperty(value = "当日赛事列表", required = true)
	public List<DlJcLqMatchDTO> playList = new ArrayList<DlJcLqMatchDTO>();
}

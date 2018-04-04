package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("按日期分类的赛事信息")
@Data
public class DlJcZqMatchDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "当日比赛日期", required = true)
	public String matchDay;
	
	//@ApiModelProperty(value = "当日比赛场数", required = true)
	//public String matchCount;
	
	@ApiModelProperty(value = "当日赛事列表", required = true)
	public List<DlJcZqMatchPlayDTO> playList = new ArrayList<DlJcZqMatchPlayDTO>();
}

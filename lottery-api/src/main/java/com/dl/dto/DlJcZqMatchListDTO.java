package com.dl.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlJcZqMatchListDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "总比赛场数", required = true)
	public String allMatchCount;
	
	@ApiModelProperty(value = "热门赛事列表", required = true)
	public List<DlJcZqMatchCellDTO> hotPlayList = new ArrayList<DlJcZqMatchCellDTO>();

	@ApiModelProperty(value = "最近几天的赛事列表", required = true)
	public List<DlJcZqMatchDTO> playList = new ArrayList<DlJcZqMatchDTO>();
}

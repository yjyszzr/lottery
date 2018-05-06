package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("赛事列表页面返回信息")
@Data
public class DlJcZqMatchListDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "总比赛场数", required = true)
	public String allMatchCount;
	
	@ApiModelProperty(value = "热门赛事列表", required = true)
	public List<DlJcZqMatchDTO> hotPlayList = new ArrayList<DlJcZqMatchDTO>();

	@ApiModelProperty(value = "最近几天的赛事列表", required = true)
	public List<DlJcZqDateMatchDTO> playList = new ArrayList<DlJcZqDateMatchDTO>();
	
	/*@ApiModelProperty(value = "所有联赛列表，用来作筛选条件", required = true)
	public List<LeagueInfoDTO> leagueInfos = new ArrayList<LeagueInfoDTO>();*/
}

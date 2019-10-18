package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlMatchInfoFutureDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "未来赛事列表")
	private List<MatchInfoFutureDTO> matchInfoFutureList;

	@Data
	public static class MatchInfoFutureDTO {
		@ApiModelProperty(value = "赛事名称")
		private String matchName;
		@ApiModelProperty(value = "比赛时间")
		private String date;
		@ApiModelProperty(value = "主队")
		private String hTeam;
		@ApiModelProperty(value = "客队")
		private String vTeam;
	}
}

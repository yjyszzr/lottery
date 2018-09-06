package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlMatchInfoFutureDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "未来赛事列表")
	public List<MatchInfoFutureDTO> matchInfoFutureList;

	@Data
	public static class MatchInfoFutureDTO {
		@ApiModelProperty(value = "赛事名称")
		public String matchName;
		@ApiModelProperty(value = "比赛时间")
		public String date;
		@ApiModelProperty(value = "主队")
		public String hTeam;
		@ApiModelProperty(value = "客队")
		public String vTeam;
	}
}

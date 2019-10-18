package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlRecentRecordDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "比赛次数")
	private Integer matchCount;
	@ApiModelProperty(value = "主队名称")
	private String homeTeam;
	@ApiModelProperty(value = "胜")
	private Integer win;
	@ApiModelProperty(value = "平")
	private Integer flat;
	@ApiModelProperty(value = "负")
	private Integer negative;
	@ApiModelProperty(value = "战绩列表")
	private List<RecentRecordInfoDTO> recentRecordList;

	@Data
	public static class RecentRecordInfoDTO {
		@ApiModelProperty(value = "赛事")
		private String match;
		@ApiModelProperty(value = "比赛时间")
		private String date;
		@ApiModelProperty(value = "主队")
		private String hTeam;
		@ApiModelProperty(value = "比分")
		private String score;
		@ApiModelProperty(value = "客队")
		private String vTeam;
		@ApiModelProperty(value = "胜负状态")
		private String status;
	}
}

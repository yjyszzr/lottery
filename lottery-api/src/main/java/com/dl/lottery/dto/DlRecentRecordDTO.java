package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlRecentRecordDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "比赛次数")
	public Integer matchCount;
	@ApiModelProperty(value = "主队名称")
	public String homeTeam;
	@ApiModelProperty(value = "胜")
	public Integer win;
	@ApiModelProperty(value = "平")
	public Integer flat;
	@ApiModelProperty(value = "负")
	public Integer negative;
	@ApiModelProperty(value = "战绩列表")
	public List<RecentRecordInfoDTO> recentRecordList;

	@Data
	public static class RecentRecordInfoDTO {
		@ApiModelProperty(value = "赛事")
		public String match;
		@ApiModelProperty(value = "赛事")
		public String date;
		@ApiModelProperty(value = "主队")
		public String hTeam;
		@ApiModelProperty(value = "比分")
		public String score;
		@ApiModelProperty(value = "客队")
		public String vTeam;
		@ApiModelProperty(value = "胜负状态")
		public String status;
	}
}

package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlMatchGroupData500WDTO {

	private List<MatchGroupData> matchGroupData;

	@Data
	public static class MatchGroupData {
		@ApiModelProperty(value = "分组名称")
		private String groupName;
		@ApiModelProperty(value = "未来赛事")
		private List<DlFutureMatchInfoDTO> futureMatchDTOList;
	}
}

package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlMatchGroupData500WDTO {

	@ApiModelProperty(value = "轮次列表")
	private List<MatchTurnGroupData> matchTurnGroupList;

	@Data
	public static class MatchTurnGroupData {
		@ApiModelProperty(value = "轮次名称")
		private String turnGroupName;
		@ApiModelProperty(value = "是否分组0:不分组,1分组")
		private Integer groupType;
		@ApiModelProperty(value = "轮次下的分组")
		private List<MatchGroupData> groupDTOList;
	}

	@Data
	public static class MatchGroupData {
		@ApiModelProperty(value = "分组名称")
		private String groupName;
		@ApiModelProperty(value = "赛程")
		private List<DlFutureMatchInfoDTO> futureMatchDTOList;
	}
}

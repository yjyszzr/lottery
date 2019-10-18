package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlLeagueMatchDTO {

	@ApiModelProperty(value = "未来赛事")
	private List<DlFutureMatchInfoDTO> futureMatchDTOList;
}

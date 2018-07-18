package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryMatchResultDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("比赛集合")
	private List<LotteryMatchDTO> lotteryMatchDTOList = new ArrayList<>();
	
	@ApiModelProperty("已结束数")
	private String finishCount = "0";
	
	@ApiModelProperty("未结束数")
	private String notfinishCount = "0";
	
	@ApiModelProperty("我收藏的赛事数")
	private String matchCollectCount = "0";
	
}

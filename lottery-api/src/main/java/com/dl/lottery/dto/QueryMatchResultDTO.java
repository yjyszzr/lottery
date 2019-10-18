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
	
	@ApiModelProperty("比赛日期字符串")
	private String matchDateStr = "";
	
}

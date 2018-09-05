package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LeagueMatchResultDTO {
	
	@ApiModelProperty(value = "主队简称")
	private String homeTeamAbbr;
	
	@ApiModelProperty(value = "客队简称")
	private String visitTeamAbbr;
	
	@ApiModelProperty(value = "半场比分")
	private String half;
	
	@ApiModelProperty(value = "全场比分")
	private String whole;
	
	@ApiModelProperty(value = "场次id")
	private String changciId;
	
	@ApiModelProperty(value = "比赛时间:时分")
	private String matchTime;
	
	@ApiModelProperty(value = "杯赛名称")
	private String cupName;
	
	@ApiModelProperty(value = "竞彩开奖结果")
	private List<KeyValueDTO> jcList ;
}

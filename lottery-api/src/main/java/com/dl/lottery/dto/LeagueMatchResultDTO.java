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
	
	@ApiModelProperty(value = "场次")
	private String changci;
	
	@ApiModelProperty(value = "比赛时间:时分")
	private String matchTime;
	
	@ApiModelProperty(value = "杯赛名称")
	private String cupName;
		
	@ApiModelProperty(value = "让球胜平负")
	private String hhad;
	
	@ApiModelProperty(value = "胜平负")
	private String had;
	
	@ApiModelProperty(value = "比分")
	private String crs;
	
	@ApiModelProperty(value = "总进球")
	private String ttg;
	
	@ApiModelProperty(value = "半全场")
	private String hafu;
	
	@ApiModelProperty(value = "篮彩胜负")
	private String mnl;
	
	@ApiModelProperty(value = "篮彩让分胜负")
	private String hdc;
	
	@ApiModelProperty(value = "篮彩胜分差")
	private String wnm;
	
	@ApiModelProperty(value = "篮彩大小分")
	private String hilo;
	
}

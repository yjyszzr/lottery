package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class DlTeamDetailForDiscoveryDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "球队简称")
	private String teamAddr;
	@ApiModelProperty(value = "球队图标")
	private String teamPic;
	@ApiModelProperty(value = "球队Id")
	private Integer teamId;
	@ApiModelProperty(value = "成立时间")
	private String teamTime;
	@ApiModelProperty(value = "球场容量")
	private String teamCapacity;
	@ApiModelProperty(value = "国家")
	private String contry;
	@ApiModelProperty(value = "球场")
	private String court;
	@ApiModelProperty(value = "城市")
	private String city;
	@ApiModelProperty(value = "球队身价")
	private String teamValue;
	@ApiModelProperty(value = "球员名单")
	private DlPlayerDTO playerlist;
	@ApiModelProperty(value = "近期战绩")
	private DlRecentRecordDTO recentRecord;
	@ApiModelProperty(value = "未来赛事")
	private DlMatchInfoFutureDTO futureMatch;
}

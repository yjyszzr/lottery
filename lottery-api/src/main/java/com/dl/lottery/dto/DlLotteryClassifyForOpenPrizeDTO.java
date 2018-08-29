package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlLotteryClassifyForOpenPrizeDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "彩种id")
	private Integer lotteryId;

	@ApiModelProperty(value = "彩种icon")
	private String lotteryIcon;

	@ApiModelProperty(value = "彩种名称")
	private String lotteryName;

	@ApiModelProperty(value = "期次")
	private String period;

	@ApiModelProperty(value = "日期")
	private String date;

	@ApiModelProperty(value = "红色球")
	private List<String> redBall;

	@ApiModelProperty(value = "蓝色球")
	private List<String> blueBall;

	@ApiModelProperty(value = "主队")
	private String homeTeam;

	@ApiModelProperty(value = "比分")
	private String score;

	@ApiModelProperty(value = "客队")
	private String visitingTeam;

	@ApiModelProperty(value = "彩种 0:数字彩,1:竞彩")
	private Integer classifyStatus;

	@ApiModelProperty(value = "彩种 0:篮球的颜色,1:足球的颜色")
	private Integer ballColor;

}

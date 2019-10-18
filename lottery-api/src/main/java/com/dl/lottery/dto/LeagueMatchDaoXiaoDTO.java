package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("按日期分类的赛事信息")
@Data
public class LeagueMatchDaoXiaoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="赛事编号")
	private Integer changciId;

	@ApiModelProperty(value="id")
	private Integer daoxiaoId;

	@ApiModelProperty(value="公司名称")
	private String comName;

	@ApiModelProperty(value="排名")
	private String initWin;

	@ApiModelProperty(value="初始奖金平")
	private String initDraw;

	@ApiModelProperty(value="初始奖金负")
	private String initLose;

	@ApiModelProperty(value="即时奖金胜")
	private String realWin;

	@ApiModelProperty(value="即时奖金平")
	private String realDraw;

	@ApiModelProperty(value="即时奖金负")
	private String realLose;

	@ApiModelProperty(value="初始变化 时间")
	private String initTime;

	@ApiModelProperty(value="即时变化 时间")
	private String realTime;
	
	@ApiModelProperty(value="胜变化趋势:0equal,1up,2down")
	private Integer winChange;

	@ApiModelProperty(value="平变化趋势:0equal,1up,2down")
	private Integer drawChange;

	@ApiModelProperty(value="负变化趋势:0equal,1up,2down")
	private Integer loseChange;
	
}

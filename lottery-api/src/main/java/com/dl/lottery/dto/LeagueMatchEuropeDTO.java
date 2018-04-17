package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LeagueMatchEuropeDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="赛事编号")
	private Integer changciId;

	@ApiModelProperty(value="亚盘对应竞彩网id")
	private Integer europeId;

	@ApiModelProperty(value="公司名称")
	private String comName;

	@ApiModelProperty(value="排名")
	private Integer orderNum;

	@ApiModelProperty(value="排名")
	private Double initWin;

	@ApiModelProperty(value="初始奖金平")
	private Double initDraw;

	@ApiModelProperty(value="初始奖金负")
	private Double initLose;

	@ApiModelProperty(value="即时奖金胜")
	private Double realWin;

	@ApiModelProperty(value="即时奖金平")
	private Double realDraw;

	@ApiModelProperty(value="即时奖金负")
	private Double realLose;

	@ApiModelProperty(value="胜变化趋势:0equal,1up,2down")
	private Integer winChange;

	@ApiModelProperty(value="平变化趋势:0equal,1up,2down")
	private Integer drawChange;

	@ApiModelProperty(value="负变化趋势:0equal,1up,2down")
	private Integer loseChange;

	@ApiModelProperty(value="最新更新时间，以分为单位，如：大于一小时展示用")
	private Integer timeMinus;

	@ApiModelProperty(value="最新概率胜")
	private String winRatio;

	@ApiModelProperty(value="最新概率平")
	private String drawRatio;

	@ApiModelProperty(value="凯利指数负")
	private String loseRatio;

	private String per;

	@ApiModelProperty(value="凯利指数胜")
	private Double winIndex;

	@ApiModelProperty(value="凯利指数平")
	private Double drawIndex;

	@ApiModelProperty(value="凯利指数负")
	private Double loseIndex;



}

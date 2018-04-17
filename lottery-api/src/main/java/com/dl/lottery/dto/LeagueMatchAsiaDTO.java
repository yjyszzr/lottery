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
public class LeagueMatchAsiaDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	 @ApiModelProperty(value="赛事编号")
    private Integer changciId;

	 @ApiModelProperty(value="亚盘对应竞彩网id")
    private Integer asiaId;

	 @ApiModelProperty(value="公司名称")
    private String comName;

	 @ApiModelProperty(value="初始水位")
    private Double initOdds1;

	 @ApiModelProperty(value="初始盘口")
    private String initRule;

	 @ApiModelProperty(value="初始水位赔率")
    private Double initOdds2;

	 @ApiModelProperty(value="即时水位")
    private Double realOdds1;

	 @ApiModelProperty(value="即时盘口")
    private String realRule;

	 @ApiModelProperty(value="即时水位赔率")
    private Double realOdds2;

	 @ApiModelProperty(value="变化趋势:0equal,1up,2down")
    private Integer odds1Change;

	 @ApiModelProperty(value="变化趋势:0equal,1up,2down")
    private Integer odds2Change;

	 @ApiModelProperty(value= "最新更新时间，以分为单位，如：大于一小时展示用")
    private Integer timeMinus;

    @ApiModelProperty(value="最新概率主")
    private Double ratioH;

    @ApiModelProperty(value="最新概率客")
    private Double ratioA;

    @ApiModelProperty(value="凯利指数主")
    private Double indexH;

    @ApiModelProperty(value="凯利指数客")
    private Double indexA;

    public LeagueMatchAsiaDTO(){
    	
    }
	
}

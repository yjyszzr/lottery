package com.dl.lottery.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LotteryMatchDTO {
    @ApiModelProperty("赛事id")
    private Integer matchId;

    @ApiModelProperty("赛事编号")
    private String matchSn;

    @ApiModelProperty("联赛名称")
    private String leagueName;

    @ApiModelProperty("联赛简称")
    private String leagueAddr;
    
//    @ApiModelProperty("场次id")
//    private Integer changciId;

    @ApiModelProperty("场次")
    private String changci;

    @ApiModelProperty("主队名称")
    private String homeTeamName;

    @ApiModelProperty("主队简称")
    private String homeTeamAbbr;
    
    @ApiModelProperty("主队logo")
    private String homeTeamLogo;

    @ApiModelProperty("客队名称")
    private String visitingTeamName;

    @ApiModelProperty("客队简称")
    private String visitingTeamAbbr;
    
    @ApiModelProperty("客队logo")
    private String visitingTeamLogo;

    @ApiModelProperty("比赛时间")
    private String matchTime;

    @ApiModelProperty("比赛开始时间")
    private String matchTimeStart;
    
    @ApiModelProperty("显示时间")
    private Date showTime;

    @ApiModelProperty("上半场得分")
    private String firstHalf;
    
    @ApiModelProperty("整场得分")
    private String whole;
    
    @ApiModelProperty("比赛时长")
    private String minute;
    
    @ApiModelProperty("是否收藏：0-否 1-是")
    private String isCollect;
    
    @ApiModelProperty("比赛状态：0-未开赛 1-已结束 2-已取消  6-比赛进行中")
    private String matchFinish;

}
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

    @ApiModelProperty("客队名称")
    private String visitingTeamName;

    @ApiModelProperty("客队简称")
    private String visitingTeamAbbr;

    @ApiModelProperty("比赛时间")
    private String matchTime;

    @ApiModelProperty("显示时间")
    private Date showTime;

    @ApiModelProperty("上半场得分")
    private String firstHalf;
    
    @ApiModelProperty("整场得分")
    private String whole;
    
    @ApiModelProperty("比赛状态：0-未结束 1-已结束")
    private String matchFinish;

}
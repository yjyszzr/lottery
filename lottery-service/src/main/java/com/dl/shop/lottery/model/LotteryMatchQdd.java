package com.dl.shop.lottery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
public class LotteryMatchQdd {
    /**
     * 赛事id
     */
    private Integer matchId;

    /**
     * 联赛id
     */
    private Integer leagueId;

    /**
     * 联赛名称
     */
    private String leagueName;

    /**
     * 联赛简称
     */
    private String leagueAddr;

    /**
     * 场次id
     */
    private Integer changciId;

    /**
     * 场次
     */
    private String changci;

    /**
     * 主队id
     */
    private Integer homeTeamId;

    /**
     * 主队名称
     */
    private String homeTeamName;

    /**
     * 主队简称
     */
    private String homeTeamAbbr;

    /**
     * 主队排名
     */
    private String homeTeamRank;

    /**
     * 客队id
     */
    private Integer visitingTeamId;

    /**
     * 客队名称
     */
    private String visitingTeamName;

    /**
     * 客队简称
     */
    private String visitingTeamAbbr;

    /**
     * 客队排名
     */
    private String visitingTeamRank;

    /**
     * 比赛时间
     */
    private Date matchTime;

    /**
     * 显示时间
     */
    private Date showTime;

    /**
     * 创建时间
     */
    private Integer createTime;
    
    
    /**
     * 是否显示 0-不显示 1-显示
     */
    private Integer isShow;

    /**
     * 是否删除 0-未删除 1-删除
     */
    private Integer isDel;
    
    /**
     * 赛事编号，足球竞猜中就是期次
     */
    private String matchSn;

    /**
     * 上半场得分
     */
    private String firstHalf;
    
    /**
     * 整场得分
     */
    private String whole;
    
    /**
     * 比赛状态：0-未拉取 1-已拉取
     */
    private Integer status;
    
    private Integer isHot;

    private String matchLiveInfo;
    
   
}
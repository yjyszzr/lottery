package com.dl.shop.lottery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_match_basketball")
public class DlMatchBasketball {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchId;
	
    /**
     * 场次id
     */
    @Column(name = "changci_id")
    private Integer changciId;

    /**
     * 联赛id
     */
    @Column(name = "league_id")
    private Integer leagueId;

    /**
     * 联赛名称
     */
    @Column(name = "league_name")
    private String leagueName;

    /**
     * 联赛简称
     */
    @Column(name = "league_abbr")
    private String leagueAbbr;

    /**
     * 场次
     */
    private String changci;

    /**
     * 主队id
     */
    @Column(name = "home_team_id")
    private Integer homeTeamId;

    /**
     * 主队名称
     */
    @Column(name = "home_team_name")
    private String homeTeamName;

    /**
     * 主队简称
     */
    @Column(name = "home_team_abbr")
    private String homeTeamAbbr;

    /**
     * 主队排名
     */
    @Column(name = "home_team_rank")
    private String homeTeamRank;

    /**
     * 客队id
     */
    @Column(name = "visiting_team_id")
    private Integer visitingTeamId;

    /**
     * 客队名称
     */
    @Column(name = "visiting_team_name")
    private String visitingTeamName;

    /**
     * 客队简称
     */
    @Column(name = "visiting_team_abbr")
    private String visitingTeamAbbr;

    /**
     * 客队排名
     */
    @Column(name = "visiting_team_rank")
    private String visitingTeamRank;

    /**
     * 比赛时间
     */
    @Column(name = "match_time")
    private Date matchTime;

    /**
     * 显示时间
     */
    @Column(name = "show_time")
    private Date showTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 是否显示 0-不显示 1-显示
     */
    @Column(name = "is_show")
    private Integer isShow;

    /**
     * 是否删除 0-未删除 1-删除
     */
    @Column(name = "is_del")
    private Integer isDel;

    /**
     * 比赛号
     */
    @Column(name = "match_sn")
    private String matchSn;

    /**
     * 全场比分
     */
    @Column(name = "whole")
    private String whole;

    /**
     * 0-比赛未结束 1-比赛已结束
     */
    private String status;

    /**
     * 是否热门 0-非热门 1-热门
     */
    @Column(name = "is_hot")
    private Integer isHot;

    /**
     * 获取场次id
     *
     * @return changci_id - 场次id
     */
    public Integer getChangciId() {
        return changciId;
    }

    /**
     * 设置场次id
     *
     * @param changciId 场次id
     */
    public void setChangciId(Integer changciId) {
        this.changciId = changciId;
    }

    public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	/**
     * 获取联赛id
     *
     * @return league_id - 联赛id
     */
    public Integer getLeagueId() {
        return leagueId;
    }

    /**
     * 设置联赛id
     *
     * @param leagueId 联赛id
     */
    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    /**
     * 获取联赛名称
     *
     * @return league_name - 联赛名称
     */
    public String getLeagueName() {
        return leagueName;
    }

    /**
     * 设置联赛名称
     *
     * @param leagueName 联赛名称
     */
    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    /**
     * 获取联赛简称
     *
     * @return league_abbr - 联赛简称
     */
    public String getLeagueAbbr() {
        return leagueAbbr;
    }

    /**
     * 设置联赛简称
     *
     * @param leagueAbbr 联赛简称
     */
    public void setLeagueAbbr(String leagueAbbr) {
        this.leagueAbbr = leagueAbbr;
    }

    /**
     * 获取场次
     *
     * @return changci - 场次
     */
    public String getChangci() {
        return changci;
    }

    /**
     * 设置场次
     *
     * @param changci 场次
     */
    public void setChangci(String changci) {
        this.changci = changci;
    }

    /**
     * 获取主队id
     *
     * @return home_team_id - 主队id
     */
    public Integer getHomeTeamId() {
        return homeTeamId;
    }

    /**
     * 设置主队id
     *
     * @param homeTeamId 主队id
     */
    public void setHomeTeamId(Integer homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    /**
     * 获取主队名称
     *
     * @return home_team_name - 主队名称
     */
    public String getHomeTeamName() {
        return homeTeamName;
    }

    /**
     * 设置主队名称
     *
     * @param homeTeamName 主队名称
     */
    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    /**
     * 获取主队简称
     *
     * @return home_team_abbr - 主队简称
     */
    public String getHomeTeamAbbr() {
        return homeTeamAbbr;
    }

    /**
     * 设置主队简称
     *
     * @param homeTeamAbbr 主队简称
     */
    public void setHomeTeamAbbr(String homeTeamAbbr) {
        this.homeTeamAbbr = homeTeamAbbr;
    }

    /**
     * 获取主队排名
     *
     * @return home_team_rank - 主队排名
     */
    public String getHomeTeamRank() {
        return homeTeamRank;
    }

    /**
     * 设置主队排名
     *
     * @param homeTeamRank 主队排名
     */
    public void setHomeTeamRank(String homeTeamRank) {
        this.homeTeamRank = homeTeamRank;
    }

    /**
     * 获取客队id
     *
     * @return visiting_team_id - 客队id
     */
    public Integer getVisitingTeamId() {
        return visitingTeamId;
    }

    /**
     * 设置客队id
     *
     * @param visitingTeamId 客队id
     */
    public void setVisitingTeamId(Integer visitingTeamId) {
        this.visitingTeamId = visitingTeamId;
    }

    /**
     * 获取客队名称
     *
     * @return visiting_team_name - 客队名称
     */
    public String getVisitingTeamName() {
        return visitingTeamName;
    }

    /**
     * 设置客队名称
     *
     * @param visitingTeamName 客队名称
     */
    public void setVisitingTeamName(String visitingTeamName) {
        this.visitingTeamName = visitingTeamName;
    }

    /**
     * 获取客队简称
     *
     * @return visiting_team_abbr - 客队简称
     */
    public String getVisitingTeamAbbr() {
        return visitingTeamAbbr;
    }

    /**
     * 设置客队简称
     *
     * @param visitingTeamAbbr 客队简称
     */
    public void setVisitingTeamAbbr(String visitingTeamAbbr) {
        this.visitingTeamAbbr = visitingTeamAbbr;
    }

    /**
     * 获取客队排名
     *
     * @return visiting_team_rank - 客队排名
     */
    public String getVisitingTeamRank() {
        return visitingTeamRank;
    }

    /**
     * 设置客队排名
     *
     * @param visitingTeamRank 客队排名
     */
    public void setVisitingTeamRank(String visitingTeamRank) {
        this.visitingTeamRank = visitingTeamRank;
    }

    /**
     * 获取比赛时间
     *
     * @return match_time - 比赛时间
     */
    public Date getMatchTime() {
        return matchTime;
    }

    /**
     * 设置比赛时间
     *
     * @param matchTime 比赛时间
     */
    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    /**
     * 获取显示时间
     *
     * @return show_time - 显示时间
     */
    public Date getShowTime() {
        return showTime;
    }

    /**
     * 设置显示时间
     *
     * @param showTime 显示时间
     */
    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取是否显示 0-不显示 1-显示
     *
     * @return is_show - 是否显示 0-不显示 1-显示
     */
    public Integer getIsShow() {
        return isShow;
    }

    /**
     * 设置是否显示 0-不显示 1-显示
     *
     * @param isShow 是否显示 0-不显示 1-显示
     */
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    /**
     * 获取是否删除 0-未删除 1-删除
     *
     * @return is_del - 是否删除 0-未删除 1-删除
     */
    public Integer getIsDel() {
        return isDel;
    }

    /**
     * 设置是否删除 0-未删除 1-删除
     *
     * @param isDel 是否删除 0-未删除 1-删除
     */
    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    /**
     * 获取比赛号
     *
     * @return match_sn - 比赛号
     */
    public String getMatchSn() {
        return matchSn;
    }

    /**
     * 设置比赛号
     *
     * @param matchSn 比赛号
     */
    public void setMatchSn(String matchSn) {
        this.matchSn = matchSn;
    }

    /**
     * 获取全场比分
     *
     * @return whole - 全场比分
     */
    public String getWhole() {
        return whole;
    }

    /**
     * 设置全场比分
     *
     * @param whole 全场比分
     */
    public void setWhole(String whole) {
        this.whole = whole;
    }

    /**
     * 获取0-比赛未结束 1-比赛已结束
     *
     * @return status - 0-比赛未结束 1-比赛已结束
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置0-比赛未结束 1-比赛已结束
     *
     * @param status 0-比赛未结束 1-比赛已结束
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取是否热门 0-非热门 1-热门
     *
     * @return is_hot - 是否热门 0-非热门 1-热门
     */
    public Integer getIsHot() {
        return isHot;
    }

    /**
     * 设置是否热门 0-非热门 1-热门
     *
     * @param isHot 是否热门 0-非热门 1-热门
     */
    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }
}
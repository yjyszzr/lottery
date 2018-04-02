package com.dl.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_match")
public class LotteryMatchDTO {
    /**
     * 赛事id
     */
    @Id
    @Column(name = "match_id")
    private Integer matchId;

    /**
     * 赛事编号
     */
    @Column(name = "match_sn")
    private String matchSn;

    /**
     * 联赛名称
     */
    @Column(name = "league_name")
    private String leagueName;

    /**
     * 联赛简称
     */
    @Column(name = "league_addr")
    private String leagueAddr;

    
    /**
     * 场次id
     */
    @Column(name = "changci_id")
    private Integer changciId;

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
     * 比赛时间
     */
    @Column(name = "match_time")
    private String matchTime;


	/**
     * 显示时间
     */
    @Column(name = "show_time")
    private Date showTime;


    /**
     * 上半场得分
     */
    @Column(name = "first_half")
    private String firstHalf;
    
    /**
     * 整场得分
     */
    @Column(name = "whole")
    private String whole;
    
    

    public String getMatchSn() {
		return matchSn;
	}

	public void setMatchSn(String matchSn) {
		this.matchSn = matchSn;
	}

	public String getFirstHalf() {
		return firstHalf;
	}

	public void setFirstHalf(String firstHalf) {
		this.firstHalf = firstHalf;
	}

	public String getWhole() {
		return whole;
	}

	public void setWhole(String whole) {
		this.whole = whole;
	}

	/**
     * 获取赛事id
     *
     * @return match_id - 赛事id
     */
    public Integer getMatchId() {
        return matchId;
    }

    /**
     * 设置赛事id
     *
     * @param matchId 赛事id
     */
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
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
     * @return league_addr - 联赛简称
     */
    public String getLeagueAddr() {
        return leagueAddr;
    }

    /**
     * 设置联赛简称
     *
     * @param leagueAddr 联赛简称
     */
    public void setLeagueAddr(String leagueAddr) {
        this.leagueAddr = leagueAddr;
    }

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
    
    public String getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}

}
package com.dl.shop.lottery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_future_match")
public class DlFutureMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 竞猜赛事id
     */
    @Column(name = "match_id")
    private Integer matchId;

    /**
     * 比赛日期
     */
    @Column(name = "match_date")
    private String matchDate;

    /**
     * 比赛时间
     */
    @Column(name = "match_time")
    private String matchTime;

    @Column(name = "game_week")
    private String gameWeek;

    @Column(name = "sporttery_matchid")
    private Integer sportteryMatchid;

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
     * 联赛id
     */
    @Column(name = "league_id")
    private Integer leagueId;

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
     * 主队id
     */
    @Column(name = "home_team_id")
    private Integer homeTeamId;

    /**
     * 客队名称
     */
    @Column(name = "visitor_team_name")
    private String visitorTeamName;

    /**
     * 客队简称
     */
    @Column(name = "visitor_team_abbr")
    private String visitorTeamAbbr;

    /**
     * 客队id
     */
    @Column(name = "visitor_team_id")
    private Integer visitorTeamId;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 0竞猜1 500万
     */
    @Column(name = "league_from")
    private Boolean leagueFrom;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取竞猜赛事id
     *
     * @return match_id - 竞猜赛事id
     */
    public Integer getMatchId() {
        return matchId;
    }

    /**
     * 设置竞猜赛事id
     *
     * @param matchId 竞猜赛事id
     */
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    /**
     * 获取比赛日期
     *
     * @return match_date - 比赛日期
     */
    public String getMatchDate() {
        return matchDate;
    }

    /**
     * 设置比赛日期
     *
     * @param matchDate 比赛日期
     */
    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    /**
     * 获取比赛时间
     *
     * @return match_time - 比赛时间
     */
    public String getMatchTime() {
        return matchTime;
    }

    /**
     * 设置比赛时间
     *
     * @param matchTime 比赛时间
     */
    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    /**
     * @return game_week
     */
    public String getGameWeek() {
        return gameWeek;
    }

    /**
     * @param gameWeek
     */
    public void setGameWeek(String gameWeek) {
        this.gameWeek = gameWeek;
    }

    /**
     * @return sporttery_matchid
     */
    public Integer getSportteryMatchid() {
        return sportteryMatchid;
    }

    /**
     * @param sportteryMatchid
     */
    public void setSportteryMatchid(Integer sportteryMatchid) {
        this.sportteryMatchid = sportteryMatchid;
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
     * 获取客队名称
     *
     * @return visitor_team_name - 客队名称
     */
    public String getVisitorTeamName() {
        return visitorTeamName;
    }

    /**
     * 设置客队名称
     *
     * @param visitorTeamName 客队名称
     */
    public void setVisitorTeamName(String visitorTeamName) {
        this.visitorTeamName = visitorTeamName;
    }

    /**
     * 获取客队简称
     *
     * @return visitor_team_abbr - 客队简称
     */
    public String getVisitorTeamAbbr() {
        return visitorTeamAbbr;
    }

    /**
     * 设置客队简称
     *
     * @param visitorTeamAbbr 客队简称
     */
    public void setVisitorTeamAbbr(String visitorTeamAbbr) {
        this.visitorTeamAbbr = visitorTeamAbbr;
    }

    /**
     * 获取客队id
     *
     * @return visitor_team_id - 客队id
     */
    public Integer getVisitorTeamId() {
        return visitorTeamId;
    }

    /**
     * 设置客队id
     *
     * @param visitorTeamId 客队id
     */
    public void setVisitorTeamId(Integer visitorTeamId) {
        this.visitorTeamId = visitorTeamId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取0竞猜1 500万
     *
     * @return league_from - 0竞猜1 500万
     */
    public Boolean getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置0竞猜1 500万
     *
     * @param leagueFrom 0竞猜1 500万
     */
    public void setLeagueFrom(Boolean leagueFrom) {
        this.leagueFrom = leagueFrom;
    }
}
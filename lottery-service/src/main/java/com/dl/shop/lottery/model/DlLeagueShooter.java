package com.dl.shop.lottery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_league_shooter")
public class DlLeagueShooter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 联赛赛季id
     */
    @Column(name = "match_season_id")
    private Integer matchSeasonId;

    /**
     * 球员名称
     */
    @Column(name = "player_name")
    private String playerName;

    /**
     * 球队名称
     */
    @Column(name = "player_team")
    private String playerTeam;

    /**
     * 进球数
     */
    @Column(name = "in_num")
    private Integer inNum;

    /**
     * 排名
     */
    private Integer sort;

    /**
     * 0 竞彩网  1 500万
     */
    @Column(name = "league_from")
    private Integer leagueFrom;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取联赛赛季id
     *
     * @return match_season_id - 联赛赛季id
     */
    public Integer getMatchSeasonId() {
        return matchSeasonId;
    }

    /**
     * 设置联赛赛季id
     *
     * @param matchSeasonId 联赛赛季id
     */
    public void setMatchSeasonId(Integer matchSeasonId) {
        this.matchSeasonId = matchSeasonId;
    }

    /**
     * 获取球员名称
     *
     * @return player_name - 球员名称
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * 设置球员名称
     *
     * @param playerName 球员名称
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * 获取球队名称
     *
     * @return player_team - 球队名称
     */
    public String getPlayerTeam() {
        return playerTeam;
    }

    /**
     * 设置球队名称
     *
     * @param playerTeam 球队名称
     */
    public void setPlayerTeam(String playerTeam) {
        this.playerTeam = playerTeam;
    }

    /**
     * 获取进球数
     *
     * @return in_num - 进球数
     */
    public Integer getInNum() {
        return inNum;
    }

    /**
     * 设置进球数
     *
     * @param inNum 进球数
     */
    public void setInNum(Integer inNum) {
        this.inNum = inNum;
    }

    /**
     * 获取排名
     *
     * @return sort - 排名
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排名
     *
     * @param sort 排名
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取0 竞彩网  1 500万
     *
     * @return league_from - 0 竞彩网  1 500万
     */
    public Integer getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置0 竞彩网  1 500万
     *
     * @param leagueFrom 0 竞彩网  1 500万
     */
    public void setLeagueFrom(Integer leagueFrom) {
        this.leagueFrom = leagueFrom;
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
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
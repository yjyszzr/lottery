package com.dl.shop.lottery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_team_record_500w")
public class DlTeamRecord500W {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 联赛名字
     */
    @Column(name = "league_name")
    private String leagueName;

    /**
     * 比赛时间
     */
    @Column(name = "match_time")
    private String matchTime;

    /**
     * 主队名称
     */
    @Column(name = "home_team")
    private String homeTeam;

    /**
     * 客队名称
     */
    @Column(name = "visiting_team")
    private String visitingTeam;

    /**
     * 比分
     */
    private String score;

    /**
     * 赛果
     */
    private String result;

    /**
     * 球队id
     */
    @Column(name = "team_id")
    private Integer teamId;
    /**
     * 球队id
     */
    @Column(name = "home_id")
    private Integer homeId;
    /**
     * 球队id
     */
    @Column(name = "visiting_id")
    private Integer teavisitingIdmId;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取联赛名字
     *
     * @return league_name - 联赛名字
     */
    public String getLeagueName() {
        return leagueName;
    }

    /**
     * 设置联赛名字
     *
     * @param leagueName 联赛名字
     */
    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
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
     * 获取主队名称
     *
     * @return home_team - 主队名称
     */
    public String getHomeTeam() {
        return homeTeam;
    }

    /**
     * 设置主队名称
     *
     * @param homeTeam 主队名称
     */
    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    /**
     * 获取客队名称
     *
     * @return visiting_team - 客队名称
     */
    public String getVisitingTeam() {
        return visitingTeam;
    }

    /**
     * 设置客队名称
     *
     * @param visitingTeam 客队名称
     */
    public void setVisitingTeam(String visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    /**
     * 获取比分
     *
     * @return score - 比分
     */
    public String getScore() {
        return score;
    }

    /**
     * 设置比分
     *
     * @param score 比分
     */
    public void setScore(String score) {
        this.score = score;
    }

    /**
     * 获取赛果
     *
     * @return result - 赛果
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置赛果
     *
     * @param result 赛果
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 获取球队id
     *
     * @return team_id - 球队id
     */
    public Integer getTeamId() {
        return teamId;
    }

    /**
     * 设置球队id
     *
     * @param teamId 球队id
     */
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

	public Integer getHomeId() {
		return homeId;
	}

	public void setHomeId(Integer homeId) {
		this.homeId = homeId;
	}

	public Integer getTeavisitingIdmId() {
		return teavisitingIdmId;
	}

	public void setTeavisitingIdmId(Integer teavisitingIdmId) {
		this.teavisitingIdmId = teavisitingIdmId;
	}
    
}
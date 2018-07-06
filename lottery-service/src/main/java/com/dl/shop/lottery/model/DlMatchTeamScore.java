package com.dl.shop.lottery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_match_team_score")
public class DlMatchTeamScore {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 球队编号
     */
    @Column(name = "team_id")
    private Integer teamId;

    /**
     * 球队名称
     */
    @Column(name = "team_name")
    private String teamName;
    
    /**
     * 球队名称
     */
    @Column(name = "team_order")
    private String teamOrder;

    /**
     * 总比赛场次数
     */
    @Column(name = "l_match_num")
    private Integer lMatchNum;

    /**
     * 总胜场次数
     */
    @Column(name = "l_match_h")
    private Integer lMatchH;

    /**
     * 总平场次数
     */
    @Column(name = "l_match_d")
    private Integer lMatchD;

    /**
     * 总负场次数
     */
    @Column(name = "l_match_a")
    private Integer lMatchA;

    /**
     * 总进球数
     */
    @Column(name = "l_ball_in")
    private Integer lBallIn;

    /**
     * 总失球数
     */
    @Column(name = "l_ball_lose")
    private Integer lBallLose;

    /**
     * 总净球数
     */
    @Column(name = "l_ball_clean")
    private Integer lBallClean;

    /**
     * 总积分
     */
    @Column(name = "l_score")
    private Integer lScore;

    /**
     * 主比赛场次数
     */
    @Column(name = "h_match_num")
    private Integer hMatchNum;

    /**
     * 主胜场次数
     */
    @Column(name = "h_match_h")
    private Integer hMatchH;

    /**
     * 主平场次数
     */
    @Column(name = "h_match_d")
    private Integer hMatchD;

    /**
     * 主负场次数
     */
    @Column(name = "h_match_a")
    private Integer hMatchA;

    /**
     * 主进球数
     */
    @Column(name = "h_ball_in")
    private Integer hBallIn;

    /**
     * 主失球数
     */
    @Column(name = "h_ball_lose")
    private Integer hBallLose;

    /**
     * 主净球数
     */
    @Column(name = "h_ball_clean")
    private Integer hBallClean;

    /**
     * 主积分
     */
    @Column(name = "h_score")
    private Integer hScore;

    /**
     * 客总比赛场次数
     */
    @Column(name = "v_match_num")
    private Integer vMatchNum;

    /**
     * 客胜场次数
     */
    @Column(name = "v_match_h")
    private Integer vMatchH;

    /**
     * 客平场次数
     */
    @Column(name = "v_match_d")
    private Integer vMatchD;

    /**
     * 客负场次数
     */
    @Column(name = "v_match_a")
    private Integer vMatchA;

    /**
     * 客进球数
     */
    @Column(name = "v_ball_in")
    private Integer vBallIn;

    /**
     * 客失球数
     */
    @Column(name = "v_ball_lose")
    private Integer vBallLose;

    /**
     * 客净球数
     */
    @Column(name = "v_ball_clean")
    private Integer vBallClean;

    /**
     * 客积分
     */
    @Column(name = "v_score")
    private Integer vScore;

    /**
     * 拉取平台:0竞彩,1 500万
     */
    @Column(name = "league_from")
    private Boolean leagueFrom;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取球队编号
     *
     * @return team_id - 球队编号
     */
    public Integer getTeamId() {
        return teamId;
    }

    /**
     * 设置球队编号
     *
     * @param teamId 球队编号
     */
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    /**
     * 获取球队名称
     *
     * @return team_name - 球队名称
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * 设置球队名称
     *
     * @param teamName 球队名称
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * 获取总比赛场次数
     *
     * @return l_match_num - 总比赛场次数
     */
    public Integer getlMatchNum() {
        return lMatchNum;
    }

    /**
     * 设置总比赛场次数
     *
     * @param lMatchNum 总比赛场次数
     */
    public void setlMatchNum(Integer lMatchNum) {
        this.lMatchNum = lMatchNum;
    }

    /**
     * 获取总胜场次数
     *
     * @return l_match_h - 总胜场次数
     */
    public Integer getlMatchH() {
        return lMatchH;
    }

    /**
     * 设置总胜场次数
     *
     * @param lMatchH 总胜场次数
     */
    public void setlMatchH(Integer lMatchH) {
        this.lMatchH = lMatchH;
    }

    /**
     * 获取总平场次数
     *
     * @return l_match_d - 总平场次数
     */
    public Integer getlMatchD() {
        return lMatchD;
    }

    /**
     * 设置总平场次数
     *
     * @param lMatchD 总平场次数
     */
    public void setlMatchD(Integer lMatchD) {
        this.lMatchD = lMatchD;
    }

    /**
     * 获取总负场次数
     *
     * @return l_match_a - 总负场次数
     */
    public Integer getlMatchA() {
        return lMatchA;
    }

    /**
     * 设置总负场次数
     *
     * @param lMatchA 总负场次数
     */
    public void setlMatchA(Integer lMatchA) {
        this.lMatchA = lMatchA;
    }

    /**
     * 获取总进球数
     *
     * @return l_ball_in - 总进球数
     */
    public Integer getlBallIn() {
        return lBallIn;
    }

    /**
     * 设置总进球数
     *
     * @param lBallIn 总进球数
     */
    public void setlBallIn(Integer lBallIn) {
        this.lBallIn = lBallIn;
    }

    /**
     * 获取总失球数
     *
     * @return l_ball_lose - 总失球数
     */
    public Integer getlBallLose() {
        return lBallLose;
    }

    /**
     * 设置总失球数
     *
     * @param lBallLose 总失球数
     */
    public void setlBallLose(Integer lBallLose) {
        this.lBallLose = lBallLose;
    }

    /**
     * 获取总净球数
     *
     * @return l_ball_clean - 总净球数
     */
    public Integer getlBallClean() {
        return lBallClean;
    }

    /**
     * 设置总净球数
     *
     * @param lBallClean 总净球数
     */
    public void setlBallClean(Integer lBallClean) {
        this.lBallClean = lBallClean;
    }

    /**
     * 获取总积分
     *
     * @return l_score - 总积分
     */
    public Integer getlScore() {
        return lScore;
    }

    /**
     * 设置总积分
     *
     * @param lScore 总积分
     */
    public void setlScore(Integer lScore) {
        this.lScore = lScore;
    }

    /**
     * 获取主比赛场次数
     *
     * @return h_match_num - 主比赛场次数
     */
    public Integer gethMatchNum() {
        return hMatchNum;
    }

    /**
     * 设置主比赛场次数
     *
     * @param hMatchNum 主比赛场次数
     */
    public void sethMatchNum(Integer hMatchNum) {
        this.hMatchNum = hMatchNum;
    }

    /**
     * 获取主胜场次数
     *
     * @return h_match_h - 主胜场次数
     */
    public Integer gethMatchH() {
        return hMatchH;
    }

    /**
     * 设置主胜场次数
     *
     * @param hMatchH 主胜场次数
     */
    public void sethMatchH(Integer hMatchH) {
        this.hMatchH = hMatchH;
    }

    /**
     * 获取主平场次数
     *
     * @return h_match_d - 主平场次数
     */
    public Integer gethMatchD() {
        return hMatchD;
    }

    /**
     * 设置主平场次数
     *
     * @param hMatchD 主平场次数
     */
    public void sethMatchD(Integer hMatchD) {
        this.hMatchD = hMatchD;
    }

    /**
     * 获取主负场次数
     *
     * @return h_match_a - 主负场次数
     */
    public Integer gethMatchA() {
        return hMatchA;
    }

    /**
     * 设置主负场次数
     *
     * @param hMatchA 主负场次数
     */
    public void sethMatchA(Integer hMatchA) {
        this.hMatchA = hMatchA;
    }

    /**
     * 获取主进球数
     *
     * @return h_ball_in - 主进球数
     */
    public Integer gethBallIn() {
        return hBallIn;
    }

    /**
     * 设置主进球数
     *
     * @param hBallIn 主进球数
     */
    public void sethBallIn(Integer hBallIn) {
        this.hBallIn = hBallIn;
    }

    /**
     * 获取主失球数
     *
     * @return h_ball_lose - 主失球数
     */
    public Integer gethBallLose() {
        return hBallLose;
    }

    /**
     * 设置主失球数
     *
     * @param hBallLose 主失球数
     */
    public void sethBallLose(Integer hBallLose) {
        this.hBallLose = hBallLose;
    }

    /**
     * 获取主净球数
     *
     * @return h_ball_clean - 主净球数
     */
    public Integer gethBallClean() {
        return hBallClean;
    }

    /**
     * 设置主净球数
     *
     * @param hBallClean 主净球数
     */
    public void sethBallClean(Integer hBallClean) {
        this.hBallClean = hBallClean;
    }

    /**
     * 获取主积分
     *
     * @return h_score - 主积分
     */
    public Integer gethScore() {
        return hScore;
    }

    /**
     * 设置主积分
     *
     * @param hScore 主积分
     */
    public void sethScore(Integer hScore) {
        this.hScore = hScore;
    }

    /**
     * 获取客总比赛场次数
     *
     * @return v_match_num - 客总比赛场次数
     */
    public Integer getvMatchNum() {
        return vMatchNum;
    }

    /**
     * 设置客总比赛场次数
     *
     * @param vMatchNum 客总比赛场次数
     */
    public void setvMatchNum(Integer vMatchNum) {
        this.vMatchNum = vMatchNum;
    }

    /**
     * 获取客胜场次数
     *
     * @return v_match_h - 客胜场次数
     */
    public Integer getvMatchH() {
        return vMatchH;
    }

    /**
     * 设置客胜场次数
     *
     * @param vMatchH 客胜场次数
     */
    public void setvMatchH(Integer vMatchH) {
        this.vMatchH = vMatchH;
    }

    /**
     * 获取客平场次数
     *
     * @return v_match_d - 客平场次数
     */
    public Integer getvMatchD() {
        return vMatchD;
    }

    /**
     * 设置客平场次数
     *
     * @param vMatchD 客平场次数
     */
    public void setvMatchD(Integer vMatchD) {
        this.vMatchD = vMatchD;
    }

    /**
     * 获取客负场次数
     *
     * @return v_match_a - 客负场次数
     */
    public Integer getvMatchA() {
        return vMatchA;
    }

    /**
     * 设置客负场次数
     *
     * @param vMatchA 客负场次数
     */
    public void setvMatchA(Integer vMatchA) {
        this.vMatchA = vMatchA;
    }

    /**
     * 获取客进球数
     *
     * @return v_ball_in - 客进球数
     */
    public Integer getvBallIn() {
        return vBallIn;
    }

    /**
     * 设置客进球数
     *
     * @param vBallIn 客进球数
     */
    public void setvBallIn(Integer vBallIn) {
        this.vBallIn = vBallIn;
    }

    /**
     * 获取客失球数
     *
     * @return v_ball_lose - 客失球数
     */
    public Integer getvBallLose() {
        return vBallLose;
    }

    /**
     * 设置客失球数
     *
     * @param vBallLose 客失球数
     */
    public void setvBallLose(Integer vBallLose) {
        this.vBallLose = vBallLose;
    }

    /**
     * 获取客净球数
     *
     * @return v_ball_clean - 客净球数
     */
    public Integer getvBallClean() {
        return vBallClean;
    }

    /**
     * 设置客净球数
     *
     * @param vBallClean 客净球数
     */
    public void setvBallClean(Integer vBallClean) {
        this.vBallClean = vBallClean;
    }

    /**
     * 获取客积分
     *
     * @return v_score - 客积分
     */
    public Integer getvScore() {
        return vScore;
    }

    /**
     * 设置客积分
     *
     * @param vScore 客积分
     */
    public void setvScore(Integer vScore) {
        this.vScore = vScore;
    }

    /**
     * 获取拉取平台:0竞彩,1 500万
     *
     * @return league_from - 拉取平台:0竞彩,1 500万
     */
    public Boolean getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置拉取平台:0竞彩,1 500万
     *
     * @param leagueFrom 拉取平台:0竞彩,1 500万
     */
    public void setLeagueFrom(Boolean leagueFrom) {
        this.leagueFrom = leagueFrom;
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

	public String getTeamOrder() {
		return teamOrder;
	}

	public void setTeamOrder(String teamOrder) {
		this.teamOrder = teamOrder;
	}
    
}
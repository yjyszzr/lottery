package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_team_score")
public class DlLeagueTeamScore {
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
     * 比赛场次数
     */
    @Column(name = "match_num")
    private Integer matchNum;

    /**
     * 胜场次数
     */
    @Column(name = "match_h")
    private Integer matchH;

    /**
     * 平场次数
     */
    @Column(name = "match_d")
    private Integer matchD;

    /**
     * 负场次数
     */
    @Column(name = "match_l")
    private Integer matchL;

    /**
     * 进球数
     */
    @Column(name = "ball_in")
    private Integer ballIn;

    /**
     * 失球数
     */
    @Column(name = "ball_lose")
    private Integer ballLose;

    /**
     * 净球数
     */
    @Column(name = "ball_clean")
    private Integer ballClean;

    /**
     * 均得
     */
    @Column(name = "pre_h")
    private Double preH;

    /**
     * 均失
     */
    @Column(name = "pre_l")
    private Double preL;

    /**
     * 胜率
     */
    @Column(name = "ratio_h")
    private String ratioH;

    /**
     * 平率
     */
    @Column(name = "ratio_d")
    private String ratioD;

    /**
     * 负率
     */
    @Column(name = "ratio_l")
    private String ratioL;

    /**
     * 积分
     */
    private Integer score;

    /**
     * 0总1主2客
     */
    private Integer flag;

    /**
     * 拉取平台:0竞彩,1 500万
     */
    @Column(name = "league_from")
    private Integer leagueFrom;

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
     * 获取比赛场次数
     *
     * @return match_num - 比赛场次数
     */
    public Integer getMatchNum() {
        return matchNum;
    }

    /**
     * 设置比赛场次数
     *
     * @param matchNum 比赛场次数
     */
    public void setMatchNum(Integer matchNum) {
        this.matchNum = matchNum;
    }

    /**
     * 获取胜场次数
     *
     * @return match_h - 胜场次数
     */
    public Integer getMatchH() {
        return matchH;
    }

    /**
     * 设置胜场次数
     *
     * @param matchH 胜场次数
     */
    public void setMatchH(Integer matchH) {
        this.matchH = matchH;
    }

    /**
     * 获取平场次数
     *
     * @return match_d - 平场次数
     */
    public Integer getMatchD() {
        return matchD;
    }

    /**
     * 设置平场次数
     *
     * @param matchD 平场次数
     */
    public void setMatchD(Integer matchD) {
        this.matchD = matchD;
    }

    /**
     * 获取负场次数
     *
     * @return match_l - 负场次数
     */
    public Integer getMatchL() {
        return matchL;
    }

    /**
     * 设置负场次数
     *
     * @param matchL 负场次数
     */
    public void setMatchL(Integer matchL) {
        this.matchL = matchL;
    }

    /**
     * 获取进球数
     *
     * @return ball_in - 进球数
     */
    public Integer getBallIn() {
        return ballIn;
    }

    /**
     * 设置进球数
     *
     * @param ballIn 进球数
     */
    public void setBallIn(Integer ballIn) {
        this.ballIn = ballIn;
    }

    /**
     * 获取失球数
     *
     * @return ball_lose - 失球数
     */
    public Integer getBallLose() {
        return ballLose;
    }

    /**
     * 设置失球数
     *
     * @param ballLose 失球数
     */
    public void setBallLose(Integer ballLose) {
        this.ballLose = ballLose;
    }

    /**
     * 获取净球数
     *
     * @return ball_clean - 净球数
     */
    public Integer getBallClean() {
        return ballClean;
    }

    /**
     * 设置净球数
     *
     * @param ballClean 净球数
     */
    public void setBallClean(Integer ballClean) {
        this.ballClean = ballClean;
    }

    /**
     * 获取均得
     *
     * @return pre_h - 均得
     */
    public Double getPreH() {
        return preH;
    }

    /**
     * 设置均得
     *
     * @param preH 均得
     */
    public void setPreH(Double preH) {
        this.preH = preH;
    }

    /**
     * 获取均失
     *
     * @return pre_l - 均失
     */
    public Double getPreL() {
        return preL;
    }

    /**
     * 设置均失
     *
     * @param preL 均失
     */
    public void setPreL(Double preL) {
        this.preL = preL;
    }

    /**
     * 获取胜率
     *
     * @return ratio_h - 胜率
     */
    public String getRatioH() {
        return ratioH;
    }

    /**
     * 设置胜率
     *
     * @param ratioH 胜率
     */
    public void setRatioH(String ratioH) {
        this.ratioH = ratioH;
    }

    /**
     * 获取平率
     *
     * @return ratio_d - 平率
     */
    public String getRatioD() {
        return ratioD;
    }

    /**
     * 设置平率
     *
     * @param ratioD 平率
     */
    public void setRatioD(String ratioD) {
        this.ratioD = ratioD;
    }

    /**
     * 获取负率
     *
     * @return ratio_l - 负率
     */
    public String getRatioL() {
        return ratioL;
    }

    /**
     * 设置负率
     *
     * @param ratioL 负率
     */
    public void setRatioL(String ratioL) {
        this.ratioL = ratioL;
    }

    /**
     * 获取积分
     *
     * @return score - 积分
     */
    public Integer getScore() {
        return score;
    }

    /**
     * 设置积分
     *
     * @param score 积分
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * 获取0总1主2客
     *
     * @return flag - 0总1主2客
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * 设置0总1主2客
     *
     * @param flag 0总1主2客
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * 获取拉取平台:0竞彩,1 500万
     *
     * @return league_from - 拉取平台:0竞彩,1 500万
     */
    public Integer getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置拉取平台:0竞彩,1 500万
     *
     * @param leagueFrom 拉取平台:0竞彩,1 500万
     */
    public void setLeagueFrom(Integer leagueFrom) {
        this.leagueFrom = leagueFrom;
    }
}
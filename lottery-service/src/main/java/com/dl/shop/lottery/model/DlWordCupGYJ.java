package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_world_cup_gyj")
public class DlWordCupGYJ {
    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 国家id
     */
    @Column(name = "home_country_id")
    private Integer homeCountryId;

    /**
     * 国家名称
     */
    @Column(name = "home_contry_name")
    private String homeContryName;

    /**
     * 国家图标
     */
    @Column(name = "home_contry_pic")
    private String homeContryPic;

    /**
     * 国家id
     */
    @Column(name = "visitor_country_id")
    private Integer visitorCountryId;

    /**
     * 国家名称
     */
    @Column(name = "visitor_contry_name")
    private String visitorContryName;

    /**
     * 国家图标
     */
    @Column(name = "visitor_contry_pic")
    private String visitorContryPic;

    /**
     * 状态0开售，1停售
     */
    @Column(name = "bet_status")
    private Integer betStatus;

    /**
     * 奖金
     */
    @Column(name = "bet_odds")
    private String betOdds;

    /**
     * 概率
     */
    @Column(name = "bet_prob")
    private String betProb;

    /**
     * id
     */
    @Column(name = "p_id")
    private Integer pId;

    /**
     * 编号
     */
    @Column(name = "sort_id")
    private Integer sortId;

    /**
     * 比赛号
     */
    @Column(name = "play_code")
    private Integer playCode;

    /**
     * 联赛id，如竞彩网2018世界杯对应id
     */
    @Column(name = "league_id")
    private Integer leagueId;

    /**
     * 联赛名称，如：2018世界杯
     */
    @Column(name = "league_name")
    private String leagueName;

    /**
     * 玩法，投注用
     */
    private String game;

    /**
     * 期次号，投注用
     */
    private String issue;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Integer updateTime;

    /**
     * 拉取平台:0竞彩
     */
    @Column(name = "league_from")
    private Integer leagueFrom;

    /**
     * 获取编号
     *
     * @return id - 编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取国家id
     *
     * @return home_country_id - 国家id
     */
    public Integer getHomeCountryId() {
        return homeCountryId;
    }

    /**
     * 设置国家id
     *
     * @param homeCountryId 国家id
     */
    public void setHomeCountryId(Integer homeCountryId) {
        this.homeCountryId = homeCountryId;
    }

    /**
     * 获取国家名称
     *
     * @return home_contry_name - 国家名称
     */
    public String getHomeContryName() {
        return homeContryName;
    }

    /**
     * 设置国家名称
     *
     * @param homeContryName 国家名称
     */
    public void setHomeContryName(String homeContryName) {
        this.homeContryName = homeContryName;
    }

    /**
     * 获取国家图标
     *
     * @return home_contry_pic - 国家图标
     */
    public String getHomeContryPic() {
        return homeContryPic;
    }

    /**
     * 设置国家图标
     *
     * @param homeContryPic 国家图标
     */
    public void setHomeContryPic(String homeContryPic) {
        this.homeContryPic = homeContryPic;
    }

    /**
     * 获取国家id
     *
     * @return visitor_country_id - 国家id
     */
    public Integer getVisitorCountryId() {
        return visitorCountryId;
    }

    /**
     * 设置国家id
     *
     * @param visitorCountryId 国家id
     */
    public void setVisitorCountryId(Integer visitorCountryId) {
        this.visitorCountryId = visitorCountryId;
    }

    /**
     * 获取国家名称
     *
     * @return visitor_contry_name - 国家名称
     */
    public String getVisitorContryName() {
        return visitorContryName;
    }

    /**
     * 设置国家名称
     *
     * @param visitorContryName 国家名称
     */
    public void setVisitorContryName(String visitorContryName) {
        this.visitorContryName = visitorContryName;
    }

    /**
     * 获取国家图标
     *
     * @return visitor_contry_pic - 国家图标
     */
    public String getVisitorContryPic() {
        return visitorContryPic;
    }

    /**
     * 设置国家图标
     *
     * @param visitorContryPic 国家图标
     */
    public void setVisitorContryPic(String visitorContryPic) {
        this.visitorContryPic = visitorContryPic;
    }

    /**
     * 获取状态0开售，1停售
     *
     * @return bet_status - 状态0开售，1停售
     */
    public Integer getBetStatus() {
        return betStatus;
    }

    /**
     * 设置状态0开售，1停售
     *
     * @param betStatus 状态0开售，1停售
     */
    public void setBetStatus(Integer betStatus) {
        this.betStatus = betStatus;
    }

    /**
     * 获取奖金
     *
     * @return bet_odds - 奖金
     */
    public String getBetOdds() {
        return betOdds;
    }

    /**
     * 设置奖金
     *
     * @param betOdds 奖金
     */
    public void setBetOdds(String betOdds) {
        this.betOdds = betOdds;
    }

    /**
     * 获取概率
     *
     * @return bet_prob - 概率
     */
    public String getBetProb() {
        return betProb;
    }

    /**
     * 设置概率
     *
     * @param betProb 概率
     */
    public void setBetProb(String betProb) {
        this.betProb = betProb;
    }

    /**
     * 获取id
     *
     * @return p_id - id
     */
    public Integer getpId() {
        return pId;
    }

    /**
     * 设置id
     *
     * @param pId id
     */
    public void setpId(Integer pId) {
        this.pId = pId;
    }

    /**
     * 获取编号
     *
     * @return sort_id - 编号
     */
    public Integer getSortId() {
        return sortId;
    }

    /**
     * 设置编号
     *
     * @param sortId 编号
     */
    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    /**
     * 获取比赛号
     *
     * @return play_code - 比赛号
     */
    public Integer getPlayCode() {
        return playCode;
    }

    /**
     * 设置比赛号
     *
     * @param playCode 比赛号
     */
    public void setPlayCode(Integer playCode) {
        this.playCode = playCode;
    }

    /**
     * 获取联赛id，如竞彩网2018世界杯对应id
     *
     * @return league_id - 联赛id，如竞彩网2018世界杯对应id
     */
    public Integer getLeagueId() {
        return leagueId;
    }

    /**
     * 设置联赛id，如竞彩网2018世界杯对应id
     *
     * @param leagueId 联赛id，如竞彩网2018世界杯对应id
     */
    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    /**
     * 获取联赛名称，如：2018世界杯
     *
     * @return league_name - 联赛名称，如：2018世界杯
     */
    public String getLeagueName() {
        return leagueName;
    }

    /**
     * 设置联赛名称，如：2018世界杯
     *
     * @param leagueName 联赛名称，如：2018世界杯
     */
    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    /**
     * 获取玩法，投注用
     *
     * @return game - 玩法，投注用
     */
    public String getGame() {
        return game;
    }

    /**
     * 设置玩法，投注用
     *
     * @param game 玩法，投注用
     */
    public void setGame(String game) {
        this.game = game;
    }

    /**
     * 获取期次号，投注用
     *
     * @return issue - 期次号，投注用
     */
    public String getIssue() {
        return issue;
    }

    /**
     * 设置期次号，投注用
     *
     * @param issue 期次号，投注用
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Integer getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Integer getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取拉取平台:0竞彩
     *
     * @return league_from - 拉取平台:0竞彩
     */
    public Integer getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置拉取平台:0竞彩
     *
     * @param leagueFrom 拉取平台:0竞彩
     */
    public void setLeagueFrom(Integer leagueFrom) {
        this.leagueFrom = leagueFrom;
    }
}
package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_world_cup_gj")
public class DlWordCupGJ {
    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 国家id
     */
    @Column(name = "country_id")
    private Integer countryId;

    /**
     * 国家名称
     */
    @Column(name = "contry_name")
    private String contryName;

    /**
     * 国家图标
     */
    @Column(name = "contry_pic")
    private String contryPic;

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
    private String pId;

    /**
     * 编号
     */
    @Column(name = "sort_id")
    private Integer sortId;

    /**
     * 比赛号
     */
    @Column(name = "play_code")
    private String playCode;

    /**
     * 联赛id，如竞彩网2018世界杯对应id
     */
    @Column(name = "league_id")
    private String leagueId;

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
     * 期次，投注用
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

    @Column(name = "sell")
    private Integer isSell;

    public Integer getIsSell() {
		return isSell;
	}

	public void setIsSell(Integer isSell) {
		this.isSell = isSell;
	}

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
     * @return country_id - 国家id
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     * 设置国家id
     *
     * @param countryId 国家id
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    /**
     * 获取国家名称
     *
     * @return contry_name - 国家名称
     */
    public String getContryName() {
        return contryName;
    }

    /**
     * 设置国家名称
     *
     * @param contryName 国家名称
     */
    public void setContryName(String contryName) {
        this.contryName = contryName;
    }

    /**
     * 获取国家图标
     *
     * @return contry_pic - 国家图标
     */
    public String getContryPic() {
        return contryPic;
    }

    /**
     * 设置国家图标
     *
     * @param contryPic 国家图标
     */
    public void setContryPic(String contryPic) {
        this.contryPic = contryPic;
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
    public String getpId() {
        return pId;
    }

    /**
     * 设置id
     *
     * @param pId id
     */
    public void setpId(String pId) {
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
    public String getPlayCode() {
        return playCode;
    }

    /**
     * 设置比赛号
     *
     * @param playCode 比赛号
     */
    public void setPlayCode(String playCode) {
        this.playCode = playCode;
    }

    /**
     * 获取联赛id，如竞彩网2018世界杯对应id
     *
     * @return league_id - 联赛id，如竞彩网2018世界杯对应id
     */
    public String getLeagueId() {
        return leagueId;
    }

    /**
     * 设置联赛id，如竞彩网2018世界杯对应id
     *
     * @param leagueId 联赛id，如竞彩网2018世界杯对应id
     */
    public void setLeagueId(String leagueId) {
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
     * 获取期次，投注用
     *
     * @return issue - 期次，投注用
     */
    public String getIssue() {
        return issue;
    }

    /**
     * 设置期次，投注用
     *
     * @param issue 期次，投注用
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
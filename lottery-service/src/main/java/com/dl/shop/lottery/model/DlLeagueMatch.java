package com.dl.shop.lottery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_league_match")
public class DlLeagueMatch {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 赛事id
     */
    @Column(name = "match_id")
    private Integer matchId;

    /**
     * 开赛时间
     */
    @Column(name = "match_time")
    private Date matchTime;

    /**
     * 赛事编号:周一001
     */
    @Column(name = "match_sn")
    private String matchSn;

    /**
     * 赛事展示分组
     */
    @Column(name = "show_time")
    private Date showTime;

    /**
     * 赛事编码
     */
    @Column(name = "play_code")
    private String playCode;

    /**
     * 联赛id
     */
    @Column(name = "league_id")
    private Integer leagueId;

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
     * 负队id
     */
    @Column(name = "visiting_team_id")
    private Integer visitingTeamId;

    /**
     * 负队名称
     */
    @Column(name = "visiting_team_name")
    private String visitingTeamName;

    /**
     * 是否热门，0非，1是
     */
    @Column(name = "is_hot")
    private Boolean isHot;

    /**
     * 半场比分(结果)
     */
    @Column(name = "half_rst")
    private String halfRst;

    /**
     * 全场比分(结果)
     */
    @Column(name = "final_rst")
    private String finalRst;

    /**
     * 胜赔率(结果)
     */
    @Column(name = "h_odds")
    private Double hOdds;

    /**
     * 平赔率(结果)
     */
    @Column(name = "d_odds")
    private Double dOdds;

    /**
     * 负赔率(结果)
     */
    @Column(name = "a_odds")
    private Double aOdds;

    /**
     * 是否拉取结果:0未1完成
     */
    private Byte status;

    /**
     * 比赛状态:0未开始，1比赛中，2完成
     */
    @Column(name = "is_del")
    private Byte isDel;

    /**
     * 比赛状态:0未开始，1比赛中，2完成
     */
    @Column(name = "is_show")
    private Byte isShow;

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
     * 拉取平台
     */
    @Column(name = "league_from")
    private Byte leagueFrom;

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
     * 获取开赛时间
     *
     * @return match_time - 开赛时间
     */
    public Date getMatchTime() {
        return matchTime;
    }

    /**
     * 设置开赛时间
     *
     * @param matchTime 开赛时间
     */
    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    /**
     * 获取赛事编号:周一001
     *
     * @return match_sn - 赛事编号:周一001
     */
    public String getMatchSn() {
        return matchSn;
    }

    /**
     * 设置赛事编号:周一001
     *
     * @param matchSn 赛事编号:周一001
     */
    public void setMatchSn(String matchSn) {
        this.matchSn = matchSn;
    }

    /**
     * 获取赛事展示分组
     *
     * @return show_time - 赛事展示分组
     */
    public Date getShowTime() {
        return showTime;
    }

    /**
     * 设置赛事展示分组
     *
     * @param showTime 赛事展示分组
     */
    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }

    /**
     * 获取赛事编码
     *
     * @return play_code - 赛事编码
     */
    public String getPlayCode() {
        return playCode;
    }

    /**
     * 设置赛事编码
     *
     * @param playCode 赛事编码
     */
    public void setPlayCode(String playCode) {
        this.playCode = playCode;
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
     * 获取负队id
     *
     * @return visiting_team_id - 负队id
     */
    public Integer getVisitingTeamId() {
        return visitingTeamId;
    }

    /**
     * 设置负队id
     *
     * @param visitingTeamId 负队id
     */
    public void setVisitingTeamId(Integer visitingTeamId) {
        this.visitingTeamId = visitingTeamId;
    }

    /**
     * 获取负队名称
     *
     * @return visiting_team_name - 负队名称
     */
    public String getVisitingTeamName() {
        return visitingTeamName;
    }

    /**
     * 设置负队名称
     *
     * @param visitingTeamName 负队名称
     */
    public void setVisitingTeamName(String visitingTeamName) {
        this.visitingTeamName = visitingTeamName;
    }

    /**
     * 获取是否热门，0非，1是
     *
     * @return is_hot - 是否热门，0非，1是
     */
    public Boolean getIsHot() {
        return isHot;
    }

    /**
     * 设置是否热门，0非，1是
     *
     * @param isHot 是否热门，0非，1是
     */
    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    /**
     * 获取半场比分(结果)
     *
     * @return half_rst - 半场比分(结果)
     */
    public String getHalfRst() {
        return halfRst;
    }

    /**
     * 设置半场比分(结果)
     *
     * @param halfRst 半场比分(结果)
     */
    public void setHalfRst(String halfRst) {
        this.halfRst = halfRst;
    }

    /**
     * 获取全场比分(结果)
     *
     * @return final_rst - 全场比分(结果)
     */
    public String getFinalRst() {
        return finalRst;
    }

    /**
     * 设置全场比分(结果)
     *
     * @param finalRst 全场比分(结果)
     */
    public void setFinalRst(String finalRst) {
        this.finalRst = finalRst;
    }

    /**
     * 获取胜赔率(结果)
     *
     * @return h_odds - 胜赔率(结果)
     */
    public Double gethOdds() {
        return hOdds;
    }

    /**
     * 设置胜赔率(结果)
     *
     * @param hOdds 胜赔率(结果)
     */
    public void sethOdds(Double hOdds) {
        this.hOdds = hOdds;
    }

    /**
     * 获取平赔率(结果)
     *
     * @return d_odds - 平赔率(结果)
     */
    public Double getdOdds() {
        return dOdds;
    }

    /**
     * 设置平赔率(结果)
     *
     * @param dOdds 平赔率(结果)
     */
    public void setdOdds(Double dOdds) {
        this.dOdds = dOdds;
    }

    /**
     * 获取负赔率(结果)
     *
     * @return a_odds - 负赔率(结果)
     */
    public Double getaOdds() {
        return aOdds;
    }

    /**
     * 设置负赔率(结果)
     *
     * @param aOdds 负赔率(结果)
     */
    public void setaOdds(Double aOdds) {
        this.aOdds = aOdds;
    }

    /**
     * 获取是否拉取结果:0未1完成
     *
     * @return status - 是否拉取结果:0未1完成
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置是否拉取结果:0未1完成
     *
     * @param status 是否拉取结果:0未1完成
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取比赛状态:0未开始，1比赛中，2完成
     *
     * @return is_del - 比赛状态:0未开始，1比赛中，2完成
     */
    public Byte getIsDel() {
        return isDel;
    }

    /**
     * 设置比赛状态:0未开始，1比赛中，2完成
     *
     * @param isDel 比赛状态:0未开始，1比赛中，2完成
     */
    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }

    /**
     * 获取比赛状态:0未开始，1比赛中，2完成
     *
     * @return is_show - 比赛状态:0未开始，1比赛中，2完成
     */
    public Byte getIsShow() {
        return isShow;
    }

    /**
     * 设置比赛状态:0未开始，1比赛中，2完成
     *
     * @param isShow 比赛状态:0未开始，1比赛中，2完成
     */
    public void setIsShow(Byte isShow) {
        this.isShow = isShow;
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
     * 获取拉取平台
     *
     * @return league_from - 拉取平台
     */
    public Byte getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置拉取平台
     *
     * @param leagueFrom 拉取平台
     */
    public void setLeagueFrom(Byte leagueFrom) {
        this.leagueFrom = leagueFrom;
    }
}
package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_basketball")
public class DlLeagueBasketBall {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 联赛id
     */
    @Column(name = "league_id")
    private Integer leagueId;

    /**
     * 联赛全称
     */
    @Column(name = "league_name")
    private String leagueName;

    /**
     * 联赛简称
     */
    @Column(name = "league_abbr")
    private String leagueAbbr;

    /**
     * 国家id
     */
    @Column(name = "contry_id")
    private Integer contryId;

    /**
     * 联赛图标
     */
    @Column(name = "league_pic")
    private String leaguePic;

    /**
     * 1 联赛 0 杯赛
     */
    @Column(name = "is_league")
    private Integer isLeague;

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
     * 获取联赛全称
     *
     * @return league_name - 联赛全称
     */
    public String getLeagueName() {
        return leagueName;
    }

    /**
     * 设置联赛全称
     *
     * @param leagueName 联赛全称
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
     * 获取国家id
     *
     * @return contry_id - 国家id
     */
    public Integer getContryId() {
        return contryId;
    }

    /**
     * 设置国家id
     *
     * @param contryId 国家id
     */
    public void setContryId(Integer contryId) {
        this.contryId = contryId;
    }

    /**
     * 获取联赛图标
     *
     * @return league_pic - 联赛图标
     */
    public String getLeaguePic() {
        return leaguePic;
    }

    /**
     * 设置联赛图标
     *
     * @param leaguePic 联赛图标
     */
    public void setLeaguePic(String leaguePic) {
        this.leaguePic = leaguePic;
    }

    /**
     * 获取1 联赛 0 杯赛
     *
     * @return is_league - 1 联赛 0 杯赛
     */
    public Integer getIsLeague() {
        return isLeague;
    }

    /**
     * 设置1 联赛 0 杯赛
     *
     * @param isLeague 1 联赛 0 杯赛
     */
    public void setIsLeague(Integer isLeague) {
        this.isLeague = isLeague;
    }
}
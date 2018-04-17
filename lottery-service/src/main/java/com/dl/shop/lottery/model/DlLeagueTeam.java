package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_team")
public class DlLeagueTeam {
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
    
    @Column(name = "sporttery_teamid")
    private Integer sportteryTeamid;

    /**
     * 球队名称
     */
    @Column(name = "team_name")
    private String teamName;

    /**
     * 球队简称
     */
    @Column(name = "team_addr")
    private String teamAddr;

    /**
     * 球队图标
     */
    @Column(name = "team_pic")
    private String teamPic;

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
     * 获取球队简称
     *
     * @return team_addr - 球队简称
     */
    public String getTeamAddr() {
        return teamAddr;
    }

    /**
     * 设置球队简称
     *
     * @param teamAddr 球队简称
     */
    public void setTeamAddr(String teamAddr) {
        this.teamAddr = teamAddr;
    }

    /**
     * 获取球队图标
     *
     * @return team_pic - 球队图标
     */
    public String getTeamPic() {
        return teamPic;
    }

    /**
     * 设置球队图标
     *
     * @param teamPic 球队图标
     */
    public void setTeamPic(String teamPic) {
        this.teamPic = teamPic;
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

	public Integer getSportteryTeamid() {
		return sportteryTeamid;
	}

	public void setSportteryTeamid(Integer sportteryTeamid) {
		this.sportteryTeamid = sportteryTeamid;
	}
}
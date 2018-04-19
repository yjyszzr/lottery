package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_info")
public class DlLeagueInfo {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 联赛编号
     */
    @Column(name = "league_id")
    private Integer leagueId;

    /**
     * 联赛名称
     */
    @Column(name = "league_name")
    private String leagueName;

    /**
     * 联赛简称
     */
    @Column(name = "league_addr")
    private String leagueAddr;

    @Column(name = "contry_id")
    private String contryId;
    
    @Column(name = "league_type")
    private String leagueType;
    
    @Column(name = "league_from")
    private String leagueFrom;
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
     * 获取联赛编号
     *
     * @return league_id - 联赛编号
     */
    public Integer getLeagueId() {
        return leagueId;
    }

    /**
     * 设置联赛编号
     *
     * @param leagueId 联赛编号
     */
    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
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
     * @return league_addr - 联赛简称
     */
    public String getLeagueAddr() {
        return leagueAddr;
    }

    /**
     * 设置联赛简称
     *
     * @param leagueAddr 联赛简称
     */
    public void setLeagueAddr(String leagueAddr) {
        this.leagueAddr = leagueAddr;
    }

	public String getContryId() {
		return contryId;
	}

	public void setContryId(String contryId) {
		this.contryId = contryId;
	}

	public String getLeagueType() {
		return leagueType;
	}

	public void setLeagueType(String leagueType) {
		this.leagueType = leagueType;
	}

	public String getLeagueFrom() {
		return leagueFrom;
	}

	public void setLeagueFrom(String leagueFrom) {
		this.leagueFrom = leagueFrom;
	}
    
}
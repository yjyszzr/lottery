package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_match_result")
public class DlLeagueMatchResult {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 赛事编号
     */
    @Column(name = "changci_id")
    private Integer changciId;

    /**
     * 玩法
     */
    @Column(name = "play_type")
    private Integer playType;
    @Column(name = "play_code")
    private String playCode;

    /**
     * 结果编码
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * 结果名称
     */
    @Column(name = "cell_name")
    private String cellName;

    /**
     * 单关：0非单关，1单关
     */
    private Integer single;
    
    private String goalline;

    /**
     * 赔率
     */
    private Double odds;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 拉取平台
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

    
    public Integer getChangciId() {
		return changciId;
	}

	public void setChangciId(Integer changciId) {
		this.changciId = changciId;
	}

	/**
     * 获取玩法
     *
     * @return play_type - 玩法
     */
    public Integer getPlayType() {
        return playType;
    }

    /**
     * 设置玩法
     *
     * @param playType 玩法
     */
    public void setPlayType(Integer playType) {
        this.playType = playType;
    }

    /**
     * 获取结果编码
     *
     * @return cell_code - 结果编码
     */
    public String getCellCode() {
        return cellCode;
    }

    /**
     * 设置结果编码
     *
     * @param cellCode 结果编码
     */
    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    /**
     * 获取结果名称
     *
     * @return cell_name - 结果名称
     */
    public String getCellName() {
        return cellName;
    }

    /**
     * 设置结果名称
     *
     * @param cellName 结果名称
     */
    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    /**
     * 获取单关：0非单关，1单关
     *
     * @return gingle - 单关：0非单关，1单关
     */
    public Integer getSingle() {
        return single;
    }

    /**
     * 设置单关：0非单关，1单关
     *
     * @param gingle 单关：0非单关，1单关
     */
    public void setSingle(Integer single) {
        this.single = single;
    }

    /**
     * 获取赔率
     *
     * @return odds - 赔率
     */
    public Double getOdds() {
        return odds;
    }

    /**
     * 设置赔率
     *
     * @param odds 赔率
     */
    public void setOdds(Double odds) {
        this.odds = odds;
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
     * 获取拉取平台
     *
     * @return league_from - 拉取平台
     */
    public Integer getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置拉取平台
     *
     * @param leagueFrom 拉取平台
     */
    public void setLeagueFrom(Integer leagueFrom) {
        this.leagueFrom = leagueFrom;
    }

	public String getGoalline() {
		return goalline;
	}

	public void setGoalline(String goalline) {
		this.goalline = goalline;
	}

	public String getPlayCode() {
		return playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

}
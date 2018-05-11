package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_match_play")
public class DlLeagueMatchPlay {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 场次id
     */
    @Column(name = "changci_id")
    private Integer changciId;

    /**
     * 玩法类型
     */
    @Column(name = "play_type")
    private Boolean playType;

    /**
     * 赛事编号:周一001
     */
    @Column(name = "play_content")
    private String playContent;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

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
    public Integer getChangciId() {
		return changciId;
	}


	/**
     * 设置赛事id
     *
     * @param matchId 赛事id
     */
    public void setChangciId(Integer changciId) {
		this.changciId = changciId;
	}

    /**
     * 获取玩法类型
     *
     * @return play_type - 玩法类型
     */
    public Boolean getPlayType() {
        return playType;
    }

    /**
     * 设置玩法类型
     *
     * @param playType 玩法类型
     */
    public void setPlayType(Boolean playType) {
        this.playType = playType;
    }

    /**
     * 获取赛事编号:周一001
     *
     * @return play_content - 赛事编号:周一001
     */
    public String getPlayContent() {
        return playContent;
    }

    /**
     * 设置赛事编号:周一001
     *
     * @param playContent 赛事编号:周一001
     */
    public void setPlayContent(String playContent) {
        this.playContent = playContent;
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
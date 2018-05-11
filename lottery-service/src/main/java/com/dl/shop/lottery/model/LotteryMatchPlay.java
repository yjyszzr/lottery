package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_match_play")
public class LotteryMatchPlay {
    /**
     * 赛事玩法表id
     */
    @Id
    @Column(name = "match_play_id")
    private Integer matchPlayId;

    /**
     * 赛事id
     */
    @Column(name = "changci_id")
    private Integer changciId;

    /**
     * 玩法内容
     */
    @Column(name = "play_content")
    private String playContent;
    
    /**
     * 玩法类型
     */
    private Integer playType;

    /**
     * 状态 0-售卖中 1-售卖结束
     */
    private Integer status;

    /**
     * 是否热门 0-非热门 1-热门
     */
    @Column(name = "is_hot")
    private Integer isHot;

    /**
     * 是否删除 0-未删除 1-删除
     */
    @Column(name = "is_del")
    private Integer isDel;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 创建时间
     */
    @Column(name = "update_time")
    private Integer updateTime;

    /**
     * 获取赛事玩法表id
     *
     * @return match_play_id - 赛事玩法表id
     */
    public Integer getMatchPlayId() {
        return matchPlayId;
    }

    /**
     * 设置赛事玩法表id
     *
     * @param matchPlayId 赛事玩法表id
     */
    public void setMatchPlayId(Integer matchPlayId) {
        this.matchPlayId = matchPlayId;
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
     * 获取玩法内容
     *
     * @return play_content - 玩法内容
     */
    public String getPlayContent() {
        return playContent;
    }

    /**
     * 设置玩法内容
     *
     * @param playContent 玩法内容
     */
    public void setPlayContent(String playContent) {
        this.playContent = playContent;
    }
    
    public Integer getPlayType() {
		return playType;
	}

	public void setPlayType(Integer playType) {
		this.playType = playType;
	}

	/**
     * 获取状态 0-售卖中 1-售卖结束
     *
     * @return status - 状态 0-售卖中 1-售卖结束
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态 0-售卖中 1-售卖结束
     *
     * @param status 状态 0-售卖中 1-售卖结束
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取是否热门 0-非热门 1-热门
     *
     * @return is_hot - 是否热门 0-非热门 1-热门
     */
    public Integer getIsHot() {
        return isHot;
    }

    /**
     * 设置是否热门 0-非热门 1-热门
     *
     * @param isHot 是否热门 0-非热门 1-热门
     */
    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    /**
     * 获取是否删除 0-未删除 1-删除
     *
     * @return is_del - 是否删除 0-未删除 1-删除
     */
    public Integer getIsDel() {
        return isDel;
    }

    /**
     * 设置是否删除 0-未删除 1-删除
     *
     * @param isDel 是否删除 0-未删除 1-删除
     */
    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
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
     * 获取创建时间
     *
     * @return update_time - 创建时间
     */
    public Integer getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置创建时间
     *
     * @param updateTime 创建时间
     */
    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}
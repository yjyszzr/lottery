package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_match_play_basketball")
public class DlMatchPlayBasketball {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 场次id
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
    @Column(name = "play_type")
    private Integer playType;

    /**
     * 状态 0-售卖中 1-售卖结束
     */
    private Integer status;

    /**
     * 是否删除 0-未删除 1-删除
     */
    @Column(name = "is_del")
    private Integer isDel;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "is_hot")
    private Integer isHot;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取场次id
     *
     * @return changci_id - 场次id
     */
    public Integer getChangciId() {
        return changciId;
    }

    /**
     * 设置场次id
     *
     * @param changciId 场次id
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

    /**
     * 获取玩法类型
     *
     * @return play_type - 玩法类型
     */
    public Integer getPlayType() {
        return playType;
    }

    /**
     * 设置玩法类型
     *
     * @param playType 玩法类型
     */
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
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return is_hot
     */
    public Integer getIsHot() {
        return isHot;
    }

    /**
     * @param isHot
     */
    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }
}
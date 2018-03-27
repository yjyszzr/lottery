package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_lottery_play_classify")
public class LotteryPlayClassify {
    /**
     * 彩票玩法分类id
     */
    @Id
    @Column(name = "lottery_play_classify_id")
    private Integer lotteryPlayClassifyId;

    /**
     * 彩票分类id
     */
    @Column(name = "lottery_classify_id")
    private Integer lotteryClassifyId;

    /**
     * 玩法名称
     */
    @Column(name = "play_name")
    private String playName;

    /**
     * 玩法图片
     */
    @Column(name = "play_img")
    private String playImg;

    /**
     * 玩法标签id
     */
    @Column(name = "play_label_id")
    private Integer playLabelId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 玩法状态 0-售卖 1-停售
     */
    private Integer status;

    /**
     * 是否显示 0-不显示 1-显示
     */
    @Column(name = "is_show")
    private Integer isShow;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 获取彩票玩法分类id
     *
     * @return lottery_play_classify_id - 彩票玩法分类id
     */
    public Integer getLotteryPlayClassifyId() {
        return lotteryPlayClassifyId;
    }

    /**
     * 设置彩票玩法分类id
     *
     * @param lotteryPlayClassifyId 彩票玩法分类id
     */
    public void setLotteryPlayClassifyId(Integer lotteryPlayClassifyId) {
        this.lotteryPlayClassifyId = lotteryPlayClassifyId;
    }

    /**
     * 获取彩票分类id
     *
     * @return lottery_classify_id - 彩票分类id
     */
    public Integer getLotteryClassifyId() {
        return lotteryClassifyId;
    }

    /**
     * 设置彩票分类id
     *
     * @param lotteryClassifyId 彩票分类id
     */
    public void setLotteryClassifyId(Integer lotteryClassifyId) {
        this.lotteryClassifyId = lotteryClassifyId;
    }

    /**
     * 获取玩法名称
     *
     * @return play_name - 玩法名称
     */
    public String getPlayName() {
        return playName;
    }

    /**
     * 设置玩法名称
     *
     * @param playName 玩法名称
     */
    public void setPlayName(String playName) {
        this.playName = playName;
    }

    /**
     * 获取玩法图片
     *
     * @return play_img - 玩法图片
     */
    public String getPlayImg() {
        return playImg;
    }

    /**
     * 设置玩法图片
     *
     * @param playImg 玩法图片
     */
    public void setPlayImg(String playImg) {
        this.playImg = playImg;
    }

    /**
     * 获取玩法标签id
     *
     * @return play_label_id - 玩法标签id
     */
    public Integer getPlayLabelId() {
        return playLabelId;
    }

    /**
     * 设置玩法标签id
     *
     * @param playLabelId 玩法标签id
     */
    public void setPlayLabelId(Integer playLabelId) {
        this.playLabelId = playLabelId;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取玩法状态 0-售卖 1-停售
     *
     * @return status - 玩法状态 0-售卖 1-停售
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置玩法状态 0-售卖 1-停售
     *
     * @param status 玩法状态 0-售卖 1-停售
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取是否显示 0-不显示 1-显示
     *
     * @return is_show - 是否显示 0-不显示 1-显示
     */
    public Integer getIsShow() {
        return isShow;
    }

    /**
     * 设置是否显示 0-不显示 1-显示
     *
     * @param isShow 是否显示 0-不显示 1-显示
     */
    public void setIsShow(Integer isShow) {
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
}
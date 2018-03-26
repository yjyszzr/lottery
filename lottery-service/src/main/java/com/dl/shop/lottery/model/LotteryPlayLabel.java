package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_play_label")
public class LotteryPlayLabel {
    /**
     * 玩法标签id
     */
    @Id
    @Column(name = "play_label_id")
    private Integer playLabelId;

    /**
     * 标签名称
     */
    @Column(name = "label_name")
    private String labelName;

    /**
     * 标签图片
     */
    @Column(name = "label_img")
    private String labelImg;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

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
     * 获取标签名称
     *
     * @return label_name - 标签名称
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * 设置标签名称
     *
     * @param labelName 标签名称
     */
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    /**
     * 获取标签图片
     *
     * @return label_img - 标签图片
     */
    public String getLabelImg() {
        return labelImg;
    }

    /**
     * 设置标签图片
     *
     * @param labelImg 标签图片
     */
    public void setLabelImg(String labelImg) {
        this.labelImg = labelImg;
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
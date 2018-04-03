package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_period_reward_detail")
public class PeriodRewardDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 期次中奖id
     */
    @Column(name = "peroid_id")
    private String peroidId;

    /**
     * 中心平台订单号
     */
    @Column(name = "platform_id")
    private String platformId;

    /**
     * 商品订单号，即彩票id
     */
    @Column(name = "ticket_id")
    private String ticketId;

    /**
     * 中奖金额,单位分
     */
    private Integer reward;

    /**
     * 8-小奖 9-大奖 10-未中奖
     */
    private String status;

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
     * 获取期次中奖id
     *
     * @return peroid_id - 期次中奖id
     */
    public String getPeroidId() {
        return peroidId;
    }

    /**
     * 设置期次中奖id
     *
     * @param peroidId 期次中奖id
     */
    public void setPeroidId(String peroidId) {
        this.peroidId = peroidId;
    }

    /**
     * 获取中心平台订单号
     *
     * @return platform_id - 中心平台订单号
     */
    public String getPlatformId() {
        return platformId;
    }

    /**
     * 设置中心平台订单号
     *
     * @param platformId 中心平台订单号
     */
    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    /**
     * 获取订单号
     *
     * @return ticketId - 订单号
     */
    public String getTicketId() {
        return ticketId;
    }

    /**
     * 设置订单号
     *
     * @param orderSn 订单号
     */
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * 获取中奖金额,单位分
     *
     * @return reward - 中奖金额,单位分
     */
    public Integer getReward() {
        return reward;
    }

    /**
     * 设置中奖金额,单位分
     *
     * @param reward 中奖金额,单位分
     */
    public void setReward(Integer reward) {
        this.reward = reward;
    }

    /**
     * 获取8-小奖 9-大奖 10-未中奖
     *
     * @return status - 8-小奖 9-大奖 10-未中奖
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置8-小奖 9-大奖 10-未中奖
     *
     * @param status 8-小奖 9-大奖 10-未中奖
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
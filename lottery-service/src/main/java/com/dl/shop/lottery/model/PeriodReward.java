package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_period_reward")
public class PeriodReward {
    @Id
    @Column(name = "period_id")
    private Integer periodId;

    /**
     * 商户编号
     */
    private String merchant;

    /**
     * 游戏编号
     */
    private String game;

    /**
     * 期次编号
     */
    private String issue;

    /**
     * 小奖票总票数
     */
    @Column(name = "small_reward_num")
    private Integer smallRewardNum;

    /**
     * 小奖金额,单位分
     */
    @Column(name = "small_reward")
    private Integer smallReward;

    /**
     * 大奖票票数
     */
    @Column(name = "big_reward_num")
    private Integer bigRewardNum;

    /**
     * 大奖票金额,单位分
     */
    @Column(name = "big_reward")
    private Integer bigReward;

    /**
     * 未中奖票票数
     */
    @Column(name = "un_reward_num")
    private Integer unRewardNum;

    /**
     * 未中奖票金额,单位分
     */
    @Column(name = "un_reward")
    private Integer unReward;
    
    /**
     * 中奖的url
     */
    @Column(name = "prize_url")
    private String prizeUrl;
    
    /**
     * 添加时间
     */
    @Column(name = "status")
    private String status;
    
    /**
     * 添加时间
     */
    @Column(name = "add_time")
    private Integer addTime;
    

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getAddTime() {
		return addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	public String getPrizeUrl() {
		return prizeUrl;
	}

	public void setPrizeUrl(String prizeUrl) {
		this.prizeUrl = prizeUrl;
	}

	/**
     * @return period_id
     */
    public Integer getPeriodId() {
        return periodId;
    }

    /**
     * @param periodId
     */
    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    /**
     * 获取商户编号
     *
     * @return merchant - 商户编号
     */
    public String getMerchant() {
        return merchant;
    }

    /**
     * 设置商户编号
     *
     * @param merchant 商户编号
     */
    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    /**
     * 获取游戏编号
     *
     * @return game - 游戏编号
     */
    public String getGame() {
        return game;
    }

    /**
     * 设置游戏编号
     *
     * @param game 游戏编号
     */
    public void setGame(String game) {
        this.game = game;
    }

    /**
     * 获取期次编号
     *
     * @return issue - 期次编号
     */
    public String getIssue() {
        return issue;
    }

    /**
     * 设置期次编号
     *
     * @param issue 期次编号
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }

    /**
     * 获取小奖票总票数
     *
     * @return small_reward_num - 小奖票总票数
     */
    public Integer getSmallRewardNum() {
        return smallRewardNum;
    }

    /**
     * 设置小奖票总票数
     *
     * @param smallRewardNum 小奖票总票数
     */
    public void setSmallRewardNum(Integer smallRewardNum) {
        this.smallRewardNum = smallRewardNum;
    }

    /**
     * 获取小奖金额,单位分
     *
     * @return small_reward - 小奖金额,单位分
     */
    public Integer getSmallReward() {
        return smallReward;
    }

    /**
     * 设置小奖金额,单位分
     *
     * @param smallReward 小奖金额,单位分
     */
    public void setSmallReward(Integer smallReward) {
        this.smallReward = smallReward;
    }

    /**
     * 获取大奖票票数
     *
     * @return big_reward_num - 大奖票票数
     */
    public Integer getBigRewardNum() {
        return bigRewardNum;
    }

    /**
     * 设置大奖票票数
     *
     * @param bigRewardNum 大奖票票数
     */
    public void setBigRewardNum(Integer bigRewardNum) {
        this.bigRewardNum = bigRewardNum;
    }

    /**
     * 获取大奖票金额,单位分
     *
     * @return big_reward - 大奖票金额,单位分
     */
    public Integer getBigReward() {
        return bigReward;
    }

    /**
     * 设置大奖票金额,单位分
     *
     * @param bigReward 大奖票金额,单位分
     */
    public void setBigReward(Integer bigReward) {
        this.bigReward = bigReward;
    }

    /**
     * 获取未中奖票票数
     *
     * @return un_reward_num - 未中奖票票数
     */
    public Integer getUnRewardNum() {
        return unRewardNum;
    }

    /**
     * 设置未中奖票票数
     *
     * @param unRewardNum 未中奖票票数
     */
    public void setUnRewardNum(Integer unRewardNum) {
        this.unRewardNum = unRewardNum;
    }

    /**
     * 获取未中奖票金额,单位分
     *
     * @return un_reward - 未中奖票金额,单位分
     */
    public Integer getUnReward() {
        return unReward;
    }

    /**
     * 设置未中奖票金额,单位分
     *
     * @param unReward 未中奖票金额,单位分
     */
    public void setUnReward(Integer unReward) {
        this.unReward = unReward;
    }
}
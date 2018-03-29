package com.dl.shop.lottery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_reward")
public class LotteryReward {
    /**
     * 开奖信息表id
     */
    @Id
    @Column(name = "reward_id")
    private Integer rewardId;

    /**
     * 赛事id
     */
    @Column(name = "match_id")
    private Integer matchId;

    /**
     * 场次id
     */
    @Column(name = "changci_id")
    private Integer changciId;

    /**
     * 场次
     */
    private String changci;

    /**
     * 比赛时间
     */
    @Column(name = "match_time")
    private Date matchTime;

    /**
     * 中奖数据
     */
    @Column(name = "reward_data")
    private String rewardData;

    /**
     * 审核状态： 0-未审核 1-审核通过
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 获取开奖信息表id
     *
     * @return reward_id - 开奖信息表id
     */
    public Integer getRewardId() {
        return rewardId;
    }

    /**
     * 设置开奖信息表id
     *
     * @param rewardId 开奖信息表id
     */
    public void setRewardId(Integer rewardId) {
        this.rewardId = rewardId;
    }

    /**
     * 获取赛事id
     *
     * @return match_id - 赛事id
     */
    public Integer getMatchId() {
        return matchId;
    }

    /**
     * 设置赛事id
     *
     * @param matchId 赛事id
     */
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
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
     * 获取场次
     *
     * @return changci - 场次
     */
    public String getChangci() {
        return changci;
    }

    /**
     * 设置场次
     *
     * @param changci 场次
     */
    public void setChangci(String changci) {
        this.changci = changci;
    }

    /**
     * 获取比赛时间
     *
     * @return match_time - 比赛时间
     */
    public Date getMatchTime() {
        return matchTime;
    }

    /**
     * 设置比赛时间
     *
     * @param matchTime 比赛时间
     */
    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    /**
     * 获取中奖数据
     *
     * @return reward_data - 中奖数据
     */
    public String getRewardData() {
        return rewardData;
    }

    /**
     * 设置中奖数据
     *
     * @param rewardData 中奖数据
     */
    public void setRewardData(String rewardData) {
        this.rewardData = rewardData;
    }

    /**
     * 获取审核状态： 0-未审核 1-审核通过
     *
     * @return status - 审核状态： 0-未审核 1-审核通过
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置审核状态： 0-未审核 1-审核通过
     *
     * @param status 审核状态： 0-未审核 1-审核通过
     */
    public void setStatus(Integer status) {
        this.status = status;
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
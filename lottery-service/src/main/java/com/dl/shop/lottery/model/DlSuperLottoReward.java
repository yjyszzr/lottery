package com.dl.shop.lottery.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_super_lotto_result")
public class DlSuperLottoReward {

	@Id
	@Column(name = "id")
	private Integer id;
	/**
	 * 期次
	 */
	@Column(name = "term_num")
	private Integer termNum;
	/**
	 * 奖金级别
	 */
	@Column(name = "reward_level")
	private Integer rewardLevel;

	/**
	 * 基本中奖注数
	 */
	@Column(name = "reward_num1")
	private Integer rewardNum1;

	/**
	 * 基本单注奖金
	 */
	@Column(name = "reward_price1")
	private Integer rewardPrice1;

	/**
	 * 追加中奖注数
	 */
	@Column(name = "reward_num2")
	private Integer rewardNum2;

	/**
	 * 追加单注奖金
	 */
	@Column(name = "reward_price2")
	private Integer rewardPrice2;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Integer createTime;

	/**
	 * 数据源
	 */
	@Column(name = "plat_from")
	private Integer platFrom;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTermNum() {
		return termNum;
	}

	public void setTermNum(Integer termNum) {
		this.termNum = termNum;
	}

	public Integer getRewardLevel() {
		return rewardLevel;
	}

	public void setRewardLevel(Integer rewardLevel) {
		this.rewardLevel = rewardLevel;
	}

	public Integer getRewardNum1() {
		return rewardNum1;
	}

	public void setRewardNum1(Integer rewardNum1) {
		this.rewardNum1 = rewardNum1;
	}

	public Integer getRewardPrice1() {
		return rewardPrice1;
	}

	public void setRewardPrice1(Integer rewardPrice1) {
		this.rewardPrice1 = rewardPrice1;
	}

	public Integer getRewardNum2() {
		return rewardNum2;
	}

	public void setRewardNum2(Integer rewardNum2) {
		this.rewardNum2 = rewardNum2;
	}

	public Integer getRewardPrice2() {
		return rewardPrice2;
	}

	public void setRewardPrice2(Integer rewardPrice2) {
		this.rewardPrice2 = rewardPrice2;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getPlatFrom() {
		return platFrom;
	}

	public void setPlatFrom(Integer platFrom) {
		this.platFrom = platFrom;
	}

}

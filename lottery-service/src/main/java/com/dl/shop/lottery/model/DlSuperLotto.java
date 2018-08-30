package com.dl.shop.lottery.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_super_lotto")
public class DlSuperLotto {
	/**
	 * 期号
	 */
	@Id
	@Column(name = "term_num")
	private Integer termNum;

	/**
	 * 开奖日期
	 */
	@Column(name = "prize_date")
	private String prizeDate;

	/**
	 * 中奖号码
	 */
	@Column(name = "prize_num")
	private String prizeNum;

	/**
	 * 奖池
	 */
	private String prizes;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private String createTime;

	/**
	 * 获取期号
	 *
	 * @return term_num - 期号
	 */
	public Integer getTermNum() {
		return termNum;
	}

	/**
	 * 设置期号
	 *
	 * @param termNum
	 *            期号
	 */
	public void setTermNum(Integer termNum) {
		this.termNum = termNum;
	}

	/**
	 * 获取开奖日期
	 *
	 * @return prize_date - 开奖日期
	 */
	public String getPrizeDate() {
		return prizeDate;
	}

	/**
	 * 设置开奖日期
	 *
	 * @param prizeDate
	 *            开奖日期
	 */
	public void setPrizeDate(String prizeDate) {
		this.prizeDate = prizeDate;
	}

	/**
	 * 获取中奖号码
	 *
	 * @return prize_num - 中奖号码
	 */
	public String getPrizeNum() {
		return prizeNum;
	}

	/**
	 * 设置中奖号码
	 *
	 * @param prizeNum
	 *            中奖号码
	 */
	public void setPrizeNum(String prizeNum) {
		this.prizeNum = prizeNum;
	}

	/**
	 * 获取奖池
	 *
	 * @return prizes - 奖池
	 */
	public String getPrizes() {
		return prizes;
	}

	/**
	 * 设置奖池
	 *
	 * @param prizes
	 *            奖池
	 */
	public void setPrizes(String prizes) {
		this.prizes = prizes;
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
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
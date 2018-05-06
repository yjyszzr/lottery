package com.dl.shop.lottery.model;

import lombok.Data;
/**
 * 用来临时记录投注彩票计算过程数据
 * @author 007
 *
 */
@Data
public class BetResultInfo {

	//投注彩票数
	private Integer ticketNum = 0;
	//彩票投注注数
	private Integer betNum = 0;
	//投注最小预测奖金
	private Double minBonus = 0.0;
	//投注最大预测奖金
	private Double maxBonus = 0.0;
	/*//单张彩票投注注数
	private Integer singleBetNum = 0;*/
}

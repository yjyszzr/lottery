package com.dl.shop.lottery.dao;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.LotteryMatch;

public interface LotteryMatchMapper extends Mapper<LotteryMatch> {
	
	/**
	 * 插入赛事数据，返回赛事id
	 * @param lotteryMatch
	 */
	public void insertMatch(LotteryMatch lotteryMatch);
}
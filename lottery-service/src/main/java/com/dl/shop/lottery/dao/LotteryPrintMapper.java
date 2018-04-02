package com.dl.shop.lottery.dao;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.LotteryPrint;

public interface LotteryPrintMapper extends Mapper<LotteryPrint> {
	
	/**
	 * 根据订单
	 * @param lotteryPrint
	 */
	public void updateByTicketId(LotteryPrint lotteryPrint);
}
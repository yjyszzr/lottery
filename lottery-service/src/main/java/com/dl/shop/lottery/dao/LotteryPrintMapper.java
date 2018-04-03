package com.dl.shop.lottery.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.LotteryPrint;

public interface LotteryPrintMapper extends Mapper<LotteryPrint> {
	
	/**
	 * 根据订单
	 * @param lotteryPrint
	 */
	public void updateByTicketId(LotteryPrint lotteryPrint);
	
	/**
	 * 查询出票列表（每次最多查询50条）
	 * @return
	 */
	public List<LotteryPrint> getPrintLotteryList();
}
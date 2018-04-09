package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.order.dto.DlOrderDataDTO;
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
	
	/**
	 * 根据条件查询出票集合
	 * @param lp
	 * @return
	 */
	List<LotteryPrint> selectPrintLotteryBySelective(LotteryPrint lp);
	
	/**
	 * 批量更新出票返回状态
	 * @param lotteryPrints
	 */
	public void updateBatchByTicketId(List<LotteryPrint> lotteryPrints);
	
	/**
	 * 查询期次相等的出票订单
	 * @param lotteryPrint
	 * @return
	 */
	public List<LotteryPrint> selectEqualsIssuePrint(LotteryPrint lotteryPrint);
	
	/**
	 * 查询当前期次小于数据库期次的出票订单
	 * @param lotteryPrint
	 * @return
	 */
	public List<LotteryPrint> selectLessThanIssuePrint(LotteryPrint lotteryPrint);
	
	List<LotteryPrint> selectTodayPrints();
	
	/**
	 * 查询中奖金额
	 * @param issue
	 * @return
	 */
	public List<DlOrderDataDTO> getRealRewardMoney(@Param("issue") String issue);
}
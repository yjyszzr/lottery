package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.lottery.dto.DlOrderDataDTO;
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
	 * 查询期次相等的出票订单
	 * @param lotteryPrint
	 * @return
	 */
	public List<LotteryPrint> selectEqualsIssuePrint(LotteryPrint lotteryPrint);
	
	/**
	 * 查询当前期在出的彩票中的数据集合
	 * @param issue
	 * @return
	 */
	public List<LotteryPrint> selectPrintsIncludeCurIssue(@Param("issue") String issue);
	
	List<LotteryPrint> selectTodayPrints();
	
	/**
	 * 查询中奖金额
	 * @param issue
	 * @return
	 */
	public List<DlOrderDataDTO> getRealRewardMoney(@Param("orderSns") List<String> orderSns);
	
	public List<LotteryPrint> getByOrderSn(@Param("orderSn")String orderSn);
	
	public List<LotteryPrint> getPrintLotteryListByOrderSns(@Param("orderSns")List<String> orderSns);

	public List<LotteryPrint> lotteryPrintsByUnCompare();

	public void updatePrintStatusByTicketId(LotteryPrint lotteryPrint);
}
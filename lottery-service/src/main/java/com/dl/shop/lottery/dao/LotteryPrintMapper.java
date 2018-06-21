package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.lottery.dto.DlOrderDataDTO;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.model.LotteryThirdApiLog;

public interface LotteryPrintMapper extends Mapper<LotteryPrint> {
	
	/**
	 * 根据订单
	 * @param lotteryPrint
	 */
	public void updateLotteryPrintByCallBack(LotteryPrint lotteryPrint);
	
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
	
	public List<LotteryPrint> getByOrderSn(@Param("orderSn")String orderSn);
	
	public List<LotteryPrint> getPrintLotteryListByOrderSns(@Param("orderSns")List<String> orderSns);

	public List<LotteryPrint> lotteryPrintsByUnCompare();

	public int updatePrintStatusByTicketId(LotteryPrint lotteryPrint);
	
	public int updateBatchLotteryPrint(LotteryPrint lotteryPrint);
	
	public List<LotteryPrint> lotteryPrintsByUnPrint();

	public List<LotteryPrint> getPrintIngLotterys();

	/**
	 * 添加日志
	 * @param thirdApiLog
	 */
	public void saveLotteryThirdApiLog(LotteryThirdApiLog thirdApiLog);
	//获取可投注金额
	public int canBetMoney(@Param("startTime") Integer startTime);
	//获取是否可投注
	public int shutDownBetValue();
}
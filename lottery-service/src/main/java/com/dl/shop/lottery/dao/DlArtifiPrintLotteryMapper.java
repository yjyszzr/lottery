package com.dl.shop.lottery.dao;

import java.util.List;
import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlArtifiPrintLottery;

public interface DlArtifiPrintLotteryMapper extends Mapper<DlArtifiPrintLottery> {
	
	/**
	 * 获取今天所有未分配的票
	 * @return
	 */
	public List<DlArtifiPrintLottery> listLotteryTodayUnAlloc();
	/**
	 * 获取今天新用户未分配的票
	 * @return
	 */
	public List<DlArtifiPrintLottery> listLotteryTodayUnAllocByNewUser();
	
	/**
	 * 获取今天老用户未分配的票
	 * @return
	 */
	public List<DlArtifiPrintLottery> listLotteryTodayUnAllocByOldUser();
	
	/**
	 * 获取今天未分配的票
	 * @return
	 */
	public List<DlArtifiPrintLottery> listLotteryTodayUnAllocNoLotto();
	
	/**
	 * 获取票
	 * @param dlPrintLottery
	 * @return
	 */
	public int updateArtifiLotteryPrint(DlArtifiPrintLottery dlPrintLottery);
	
	
	/**
	 * 根据orderSn获取列表数据
	 * @param dlPrintLottery
	 * @return
	 */
	public List<DlArtifiPrintLottery> selectArtifiLotteryPrintByOrderSn(DlArtifiPrintLottery dlPrintLottery);
}
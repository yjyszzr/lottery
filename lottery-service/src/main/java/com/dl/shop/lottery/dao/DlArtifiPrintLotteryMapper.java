package com.dl.shop.lottery.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlArtifiPrintLottery;

public interface DlArtifiPrintLotteryMapper extends Mapper<DlArtifiPrintLottery> {
	
	int batchInsert(@Param("dlArtifiLotterys") List<DlArtifiPrintLottery> dlArtifiLotterys);

	/**
	 * 获取今天未分配的票
	 * @return
	 */
	public List<DlArtifiPrintLottery> listLotteryTodayUnAlloc();
	

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
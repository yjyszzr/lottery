package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.lottery.dto.DlPlayClassifyDetailDTO;
import com.dl.shop.lottery.model.LotteryPlayClassify;

public interface LotteryPlayClassifyMapper extends Mapper<LotteryPlayClassify> {
	
	/**
	 * 
	 * @param lotteryClassifyId
	 * @return
	 */
	public List<DlPlayClassifyDetailDTO> selectAllData(@Param("lotteryClassifyId")Integer lotteryClassifyId);
	
	public List<LotteryPlayClassify> getAllPlays(@Param("lotteryClassifyId")Integer lotteryClassifyId);
}
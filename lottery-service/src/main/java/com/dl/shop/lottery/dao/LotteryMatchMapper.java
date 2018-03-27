package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.dto.DlJcZqMatchPlayDTO;
import com.dl.shop.lottery.model.LotteryMatch;

public interface LotteryMatchMapper extends Mapper<LotteryMatch> {
	
	/**
	 * 插入赛事数据，返回赛事id
	 * @param lotteryMatch
	 */
	public void insertMatch(LotteryMatch lotteryMatch);

	/**
	 * 
	 * @param playType获取赛事列表
	 * @return
	 */
	public List<DlJcZqMatchPlayDTO> getMatchList(@Param("playType")String playType);
}
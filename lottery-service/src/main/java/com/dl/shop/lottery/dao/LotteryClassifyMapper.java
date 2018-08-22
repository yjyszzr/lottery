package com.dl.shop.lottery.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.LotteryClassify;

public interface LotteryClassifyMapper extends Mapper<LotteryClassify> {
	
	List<LotteryClassify> selectAllLotteryClassData();
	
	List<LotteryClassify> selectAllLotteryClasses();
}
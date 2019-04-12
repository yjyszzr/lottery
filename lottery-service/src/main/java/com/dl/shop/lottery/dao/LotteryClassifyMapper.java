package com.dl.shop.lottery.dao;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.LotteryClassify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LotteryClassifyMapper extends Mapper<LotteryClassify> {
	
	List<LotteryClassify> selectAllLotteryClassData();
	
	List<LotteryClassify> selectAllLotteryClasses(@Param("appCodeName") Integer appCodeName);
	
	LotteryClassify selectLotteryClassesById(@Param("lotteryClassifyId") Integer lotteryClassifyId);
}
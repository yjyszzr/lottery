package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.enums.MatchPlayTypeEnum;
import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.LotteryMatchPlay;

public interface LotteryMatchPlayMapper extends Mapper<LotteryMatchPlay> {

	List<LotteryMatchPlay> matchPlayListByMatchIds(@Param("matchIds")Integer[] matchIds, @Param("playType")String playType);

	void updatePlayContent(LotteryMatchPlay play);

	LotteryMatchPlay lotteryMatchPlayByMatchIdAndPlayType(@Param("matchId")Integer matchId, @Param("playType")int playType);
}
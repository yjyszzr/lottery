package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlMatchPlayBasketball;

public interface DlMatchPlayBasketballMapper extends Mapper<DlMatchPlayBasketball> {
	
	List<DlMatchPlayBasketball> matchPlayListByChangciIds(@Param("changciIds")Integer[] matchIds, @Param("playType")String playType);

	void updatePlayContent(DlMatchPlayBasketball play);

	DlMatchPlayBasketball lotteryMatchPlayByChangciIdAndPlayType(@Param("changciId")Integer matchId, @Param("playType")int playType);
	
}
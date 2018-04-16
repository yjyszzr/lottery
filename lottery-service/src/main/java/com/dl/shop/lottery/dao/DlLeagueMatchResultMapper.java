package com.dl.shop.lottery.dao;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueMatchResult;

public interface DlLeagueMatchResultMapper extends Mapper<DlLeagueMatchResult> {

	int getCountByMatchId(@Param("changciId")Integer matchId);
}
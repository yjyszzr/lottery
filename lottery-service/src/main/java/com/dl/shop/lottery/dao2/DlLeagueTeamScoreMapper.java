package com.dl.shop.lottery.dao2;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueTeamScore;

public interface DlLeagueTeamScoreMapper extends Mapper<DlLeagueTeamScore> {

	DlLeagueTeamScore  getTeamScoresByFlag(@Param("teamId") Integer teamId, @Param("flag")int flag);
}
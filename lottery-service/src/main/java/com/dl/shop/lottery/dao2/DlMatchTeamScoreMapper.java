package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlMatchTeamScore;

public interface DlMatchTeamScoreMapper extends Mapper<DlMatchTeamScore> {

	DlMatchTeamScore getByTeamId(@Param("teamId") Integer teamId, @Param("leagueId") Integer leagueId);

	List<DlMatchTeamScore> getByleagueId(@Param("leagueId") Integer leagueId);

	List<DlMatchTeamScore> getBySeasonId(@Param("seasonId") Integer seasonId);
}
package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlSeason500w;

public interface DlSeason500wMapper extends Mapper<DlSeason500w> {

	List<DlSeason500w> findAllSeason();

	Integer getlastSeasonByLeagueId(@Param("leagueId") Integer leagueId);

	List<DlSeason500w> findSeasonByLeagueId(@Param("leagueId") Integer leagueId);

	List<DlSeason500w> getSeasonBy5LeagueId(@Param("list") List<Integer> leaguesList);
}
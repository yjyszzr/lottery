package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueInfo;
import com.dl.shop.lottery.model.DlLeagueInfo500W;
import com.dl.shop.lottery.model.DlLeagueInfoTeamRef;

public interface DlLeagueInfoMapper extends Mapper<DlLeagueInfo> {

	List<DlLeagueInfo> getFilterConditions();

	void saveLeagueTeamRef(DlLeagueInfoTeamRef ref);

	List<DlLeagueInfo> getAll();

	List<DlLeagueInfo500W> getHotLeagues();

	DlLeagueInfo getByLeagueId(@Param("leagueId") Integer leagueId);

	List<DlLeagueInfo500W> getInternationalLeagues();

	List<DlLeagueInfo> getHotLeaguesForLD();

	DlLeagueInfo500W getLeagueInfo500wByLeagueId(@Param("leagueId") Integer leagueId);

	List<DlLeagueInfo500W> getByGroupId(@Param("groupId") Integer groupId);

	List<DlLeagueInfo500W> getCupMatchs(@Param("groupId") Integer groupId);

}
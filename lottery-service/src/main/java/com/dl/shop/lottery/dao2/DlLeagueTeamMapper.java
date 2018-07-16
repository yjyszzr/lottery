package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueTeam;

public interface DlLeagueTeamMapper extends Mapper<DlLeagueTeam> {

	DlLeagueTeam getBySportteryTeamid(@Param("sportteryTeamid")Integer sportteryTeamid);

	DlLeagueTeam getByTeamId(@Param("teamId")Integer teamId);
	
	List<DlLeagueTeam> queryLeagueTeamByTeamIds(@Param("teamIdList")List teamIdList);

	Integer queryTeamId(@Param("sportteryTeamid")Integer sportteryTeamid);

}
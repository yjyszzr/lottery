package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeaguePlayer;

public interface DlLeaguePlayerMapper extends Mapper<DlLeaguePlayer> {

	List<DlLeaguePlayer> findByTeamId(@Param("teamId") Integer teamId);
}
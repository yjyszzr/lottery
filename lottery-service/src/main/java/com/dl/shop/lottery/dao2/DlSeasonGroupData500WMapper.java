package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.lottery.dto.DlMatchGroupDTO;
import com.dl.shop.lottery.model.DlSeasonGroupData500W;

public interface DlSeasonGroupData500WMapper extends Mapper<DlSeasonGroupData500W> {

	List<DlSeasonGroupData500W> findByLeagueIdAndSeasonId(@Param("seasonId") Integer seasonId, @Param("leagueId") Integer leagueId);

	List<DlMatchGroupDTO> findMatchGroupsByLeagueIdAndSeasonId(@Param("seasonId") Integer seasonId, @Param("leagueId") Integer leagueId);
}
package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlScore500W;

public interface DlScore500WMapper extends Mapper<DlScore500W> {

	List<DlScore500W> findByLeagueIdAndSeasonId(@Param("seasonId") Integer seasonId, @Param("leagueId") Integer leagueId);
}
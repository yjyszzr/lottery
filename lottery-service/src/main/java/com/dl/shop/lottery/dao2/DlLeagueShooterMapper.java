package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueShooter;

public interface DlLeagueShooterMapper extends Mapper<DlLeagueShooter> {

	List<DlLeagueShooter> findByLeagueId(@Param("leagueId") Integer leagueId);
}
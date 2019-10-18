package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueMatchEurope;

public interface DlLeagueMatchEuropeMapper extends Mapper<DlLeagueMatchEurope> {

	int getCountByChangciId(@Param("changciId")Integer changciId);

	int updateMatchEurope(DlLeagueMatchEurope europe);
	
	List<DlLeagueMatchEurope> getAllByChangciId(@Param("changciId")Integer changciId);
}
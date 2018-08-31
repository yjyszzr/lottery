package com.dl.shop.lottery.dao2;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueInfo;
import com.dl.shop.lottery.model.DlLeagueInfoTeamRef;

public interface DlLeagueInfoMapper extends Mapper<DlLeagueInfo> {

	List<DlLeagueInfo> getFilterConditions();
	
	void saveLeagueTeamRef(DlLeagueInfoTeamRef ref);
	
	List<DlLeagueInfo>  getAll();
	
	List<DlLeagueInfo>  getHotLeagues();
}
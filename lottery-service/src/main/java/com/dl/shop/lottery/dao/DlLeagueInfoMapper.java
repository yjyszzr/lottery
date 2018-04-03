package com.dl.shop.lottery.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueInfo;

public interface DlLeagueInfoMapper extends Mapper<DlLeagueInfo> {

	List<DlLeagueInfo> getFilterConditions();
}
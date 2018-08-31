package com.dl.shop.lottery.dao2;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueGroup;

public interface DlLeagueGroupMapper extends Mapper<DlLeagueGroup> {

	List<DlLeagueGroup> getAll();
}
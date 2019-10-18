package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueContry;
import com.dl.shop.lottery.model.DlLeagueContry500W;

public interface DlLeagueContryMapper extends Mapper<DlLeagueContry> {

	List<DlLeagueContry> getAll();

	List<DlLeagueContry> getContrysByGroupId(@Param("groupId") Integer groupId);

	List<DlLeagueContry500W> getContrysFrom500WByGroupId(@Param("groupId") Integer groupId);
}
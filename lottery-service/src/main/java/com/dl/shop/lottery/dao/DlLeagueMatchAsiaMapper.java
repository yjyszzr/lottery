package com.dl.shop.lottery.dao;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueMatchAsia;

public interface DlLeagueMatchAsiaMapper extends Mapper<DlLeagueMatchAsia> {

	int getCountByChangciId(@Param("changciId")Integer changciId);
	
	int updateMatchAsia(DlLeagueMatchAsia asia);
}
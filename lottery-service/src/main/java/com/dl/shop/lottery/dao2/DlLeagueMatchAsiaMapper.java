package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueMatchAsia;

public interface DlLeagueMatchAsiaMapper extends Mapper<DlLeagueMatchAsia> {

	int getCountByChangciId(@Param("changciId")Integer changciId);
	
	int updateMatchAsia(DlLeagueMatchAsia asia);
	
	List<DlLeagueMatchAsia> getListByChangciId(@Param("changciId")Integer changciId);
}
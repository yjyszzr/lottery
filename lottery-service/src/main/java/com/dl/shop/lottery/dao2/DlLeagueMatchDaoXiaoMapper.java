package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlLeagueMatchDaoXiao;

public interface DlLeagueMatchDaoXiaoMapper extends Mapper<DlLeagueMatchDaoXiao> {
	
	List<DlLeagueMatchDaoXiao> getAllByChangciId(@Param("changciId") Integer changciId);
}
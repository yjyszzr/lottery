package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlTeamRecord500W;

public interface DlTeamRecord500WMapper extends Mapper<DlTeamRecord500W> {

	List<DlTeamRecord500W> findByTeamId(@Param("teamId") Integer teamId);
}
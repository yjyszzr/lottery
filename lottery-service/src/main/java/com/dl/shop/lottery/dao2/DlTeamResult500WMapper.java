package com.dl.shop.lottery.dao2;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlTeamResult500W;

public interface DlTeamResult500WMapper extends Mapper<DlTeamResult500W> {

	DlTeamResult500W findByTeamId(@Param("teamId") Integer teamId);
}
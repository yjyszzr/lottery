package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlTeamFuture500W;

public interface DlTeamFuture500WMapper extends Mapper<DlTeamFuture500W> {

	List<DlTeamFuture500W> findByTeamId(@Param("teamId") Integer teamId);
}
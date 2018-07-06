package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlFutureMatch;

public interface DlFutureMatchMapper extends Mapper<DlFutureMatch> {
	
	List<DlFutureMatch> getList(@Param("teamId")Integer teamId);
}
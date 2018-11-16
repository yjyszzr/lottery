package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlOpLog;

public interface DlOpLogMapper extends Mapper<DlOpLog> {
	
	List<DlOpLog> queryLogByTime(@Param("phone") String phone,@Param("startTime") Integer startTime,@Param("endTime") Integer endTime);
	
	DlOpLog queryLogByOrderSn(@Param("orderSn") String orderSn);
}
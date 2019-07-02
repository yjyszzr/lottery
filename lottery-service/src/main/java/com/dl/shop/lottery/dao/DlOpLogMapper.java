package com.dl.shop.lottery.dao;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlOpLog;
import com.dl.shop.lottery.model.NewDlOpLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DlOpLogMapper extends Mapper<DlOpLog> {
	
	List<NewDlOpLog> queryLogByTime(@Param("phone") String phone, @Param("startTime") Integer startTime, @Param("endTime") Integer endTime);
	
	DlOpLog queryLogByOrderSn(@Param("orderSn") String orderSn);
}
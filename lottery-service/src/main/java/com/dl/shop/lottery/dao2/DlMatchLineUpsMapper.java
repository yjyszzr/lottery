package com.dl.shop.lottery.dao2;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlMatchLineUps;

public interface DlMatchLineUpsMapper extends Mapper<DlMatchLineUps> {
	
	DlMatchLineUps getByChangciId(@Param("changciId")Integer changciId);
}
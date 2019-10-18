package com.dl.shop.lottery.dao2;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlMatchLive;

public interface DlMatchLiveMapper extends Mapper<DlMatchLive> {
	
	DlMatchLive getByChangciId(@Param("changciId")Integer changciId);
}
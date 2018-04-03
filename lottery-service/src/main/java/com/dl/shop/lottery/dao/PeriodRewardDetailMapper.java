package com.dl.shop.lottery.dao;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.PeriodRewardDetail;

public interface PeriodRewardDetailMapper extends Mapper<PeriodRewardDetail> {
	
	int insertPeriodRewardDetail(PeriodRewardDetail periodRewardDetail);
}
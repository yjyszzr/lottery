package com.dl.shop.lottery.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.PeriodRewardDetail;

public interface PeriodRewardDetailMapper extends Mapper<PeriodRewardDetail> {
	
	int insertPeriodRewardDetail(PeriodRewardDetail periodRewardDetail);
	
	List<PeriodRewardDetail> queryPeriodRewardDetailBySelective(PeriodRewardDetail periodRewardDetail);
}
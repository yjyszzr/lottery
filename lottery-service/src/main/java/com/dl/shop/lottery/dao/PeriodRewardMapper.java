package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.PeriodReward;

public interface PeriodRewardMapper extends Mapper<PeriodReward> {
	
	int insertPeriodReward(PeriodReward periodReward);
	
	List<String> queryPeriodRewardByIssues(@Param("list") List<String> matchSnList);
	
}
package com.dl.shop.lottery.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.LotteryReward;

public interface LotteryRewardMapper extends Mapper<LotteryReward> {
	
	List<LotteryReward> queryRewardToday();
	
	List<LotteryReward> queryRewardByIssue(LotteryReward lr);
	
}
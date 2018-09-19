package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.lottery.dto.BasketBallLeagueInfoDTO;
import com.dl.shop.lottery.model.DlMatchBasketball;

public interface DlMatchBasketballMapper extends Mapper<DlMatchBasketball> {
	
	/**
	 * 
	 * @param leagueId 
	 * @param playType获取赛事列表
	 * @return
	 */
	public List<DlMatchBasketball> getMatchList(@Param("leagueIds")String leagueId);
	
	/**
	 * 获取赛事列表的联赛信息
	 * @return
	 */
	public List<BasketBallLeagueInfoDTO> getBasketBallFilterConditions();
	
}
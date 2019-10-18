package com.dl.shop.lottery.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.shop.lottery.model.DlLeagueBasketBall;

public interface DlLeagueBasketBallMapper extends Mapper<DlLeagueBasketBall> {
	
	/**
	 * 获取赛事列表的联赛信息
	 * @return
	 */
	public List<LeagueInfoDTO> getFilterConditions();
}
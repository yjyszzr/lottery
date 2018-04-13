package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.LotteryMatch;

public interface LotteryMatchMapper extends Mapper<LotteryMatch> {
	
	/**
	 * 插入赛事数据，返回赛事id
	 * @param lotteryMatch
	 */
	public void insertMatch(LotteryMatch lotteryMatch);

	/**
	 * 
	 * @param leagueId 
	 * @param playType获取赛事列表
	 * @return
	 */
	public List<LotteryMatch> getMatchList(@Param("leagueIds")String leagueId);
	
	/**
	 * 获取当天的所有比赛
	 * @return
	 */
	List<LotteryMatch> getMatchListToday();
	
	/**
	 * 获取当天未知分数的比赛
	 * @return
	 */
	List<LotteryMatch> getMatchListUnknowScoreToday();
	
	
	public int updateMatchBatch(@Param("list") List<LotteryMatch> matchList);
	
	
	/**
	 * 根据查询条件查询赛事结果
	 * @param dateStr
	 * @return
	 */
	public List<LotteryMatch> queryMatchByQueryCondition(@Param("dateStr") String dateStr,@Param("issueArr") String[] issueArr,@Param("leagueIdArr") String[] leagueIdArr);
	
}
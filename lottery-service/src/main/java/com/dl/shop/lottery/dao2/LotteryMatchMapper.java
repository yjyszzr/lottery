package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.lottery.dto.LeagueInfoDTO;
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
	
	public List<LotteryMatch> getMatchListByPlayCodes(@Param("playCodes")List<String> playCodes);
	
	public List<LotteryMatch> refreshInfos();
	public int updateteaminfo(LotteryMatch match);
	List<LotteryMatch> allmatches();
	
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
	public List<LotteryMatch> queryMatchByQueryCondition(@Param("dateStr") String dateStr,@Param("matchIdArr") String[] matchIdArr,
			@Param("leagueIdArr") String[] leagueIdArr,@Param("matchFinish") String matchFinish);
	
	/**
	 * 根据查询条件查询赛事结果最新
	 * @param dateStr
	 * @return
	 */
	public List<LotteryMatch> queryMatchByQueryConditionNew(@Param("dateStr") String dateStr,@Param("matchIdArr") String[] matchIdArr,
			@Param("leagueIdArr") String[] leagueIdArr,@Param("type") String type);		
	
	/**
	 * 通过changciId获取对象
	 * @param matchId
	 * @return
	 */
	public LotteryMatch getByChangciId(@Param("changciId")Integer changciId);


	public LotteryMatch getByMatchId(@Param("matchId")Integer matchId);

	//历史交锋
	public List<LotteryMatch> getByTeamId(@Param("homeTeamId")Integer homeTeamId, @Param("visitingTeamId")Integer visitingTeamId, @Param("num")int num);
	//球队主场战绩
	public List<LotteryMatch> getByTeamIdForhh(@Param("teamId")Integer teamId,  @Param("num")int i);
	//球队客场战绩
	public List<LotteryMatch> getByTeamIdForvv(@Param("teamId")Integer teamId,  @Param("num")int i);
	//球队主客占绩
	public List<LotteryMatch> getByTeamIdForhv(@Param("teamId")Integer teamId,  @Param("num")int i);

	public void updateMatchResult(LotteryMatch match);

	/**
	 * 获取未结束赛事的场次id,供摘取赔率信息用
	 * @return
	 */
	public List<Integer> getChangcidIsUnEnd();
	/**
	 * 获取已结束赛事去拉详情
	 * @return
	 */
	public List<LotteryMatch> matchListEnded();

	/**
	 * 获取赛事列表的联赛信息
	 * @return
	 */
	public List<LeagueInfoDTO> getFilterConditions();

	/**
	 * 批量入库历史赛事
	 * @return
	 */
	public int batchInsertHistoryMatch(@Param("list") List<LotteryMatch> list);
	
  	/**
	 * 筛选当天的比赛的league信息
	 */
	public List<LeagueInfoDTO> getFilterConditionsSomeDay(@Param("dateStr") String dateStr);

	/**
	 * 获取取消的赛事编码
	 * @param playCodes
	 * @return
	 */
	public List<String> getCancelMatches(@Param("playCodes") List<String> playCodes);
	
}
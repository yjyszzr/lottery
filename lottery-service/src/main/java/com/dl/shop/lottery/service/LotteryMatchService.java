package com.dl.shop.lottery.service;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.NetWorkUtil;
import com.dl.dto.DlJcZqMatchListDTO;
import com.dl.enums.MatchPlayTypeEnum;
import com.dl.param.DlJcZqMatchListParam;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryMatchMapper;
import com.dl.shop.lottery.dao.LotteryMatchPlayMapper;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.model.LotteryMatchPlay;
import com.dl.shop.lottery.utils.PlayTypeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LotteryMatchService extends AbstractService<LotteryMatch> {
    
	@Resource
    private LotteryMatchMapper lotteryMatchMapper;
	
	@Resource
	private LotteryMatchPlayMapper lotteryMatchPlayMapper;
	
	@Value("${match.url}")
	private String matchUrl;

    /**
     * 获取赛事列表
     * @param param
     * @return
     */
	public DlJcZqMatchListDTO getMatchList(DlJcZqMatchListParam param) {
		DlJcZqMatchListDTO dlJcZqMatchListDTO = new DlJcZqMatchListDTO();
	    return dlJcZqMatchListDTO;
	}
	
	/**
	 * 抓取赛事列表并保存
	 */
	@Transactional
	public void saveMatchList() {
		//赛事、玩法汇总列表
		Map<String, Object> map = new HashMap<String, Object>();
		//赛事列表
		Map<String, JSONObject> matchs = new HashMap<String, JSONObject>();
		//各赛事的玩法列表
		List<Map<String, Object>> matchPlays = new LinkedList<Map<String, Object>>();
		map.put("matchs", matchs);
		map.put("matchPlays", matchPlays);
		//抓取胜负彩数据
		map = getCollectMatchData(map, MatchPlayTypeEnum.PLAY_TYPE_HAD.getMsg());
		//抓取让球胜负彩数据
		map = getCollectMatchData(map, MatchPlayTypeEnum.PLAY_TYPE_HHAD.getMsg());
		//抓取半全场数据
		map = getCollectMatchData(map, MatchPlayTypeEnum.PLAY_TYPE_HAFU.getMsg());
		//抓取总进球数据
		map = getCollectMatchData(map, MatchPlayTypeEnum.PLAY_TYPE_TTG.getMsg());
		
		List<LotteryMatch> lotteryMatchs = getLotteryMatchData(matchs);
		log.info(lotteryMatchs.toString());
		
		//保存赛事数据
		saveMatchData(lotteryMatchs, matchPlays);
	}
	
	/**
	 * 逐个玩法组装数据
	 * @param map
	 * @param playType
	 * @return
	 */
	private Map<String, Object> getCollectMatchData(Map<String, Object> map, String playType) {
		@SuppressWarnings("unchecked")
		Map<String, JSONObject> matchs = (Map<String, JSONObject>) map.get("matchs");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> matchPlays = (List<Map<String, Object>>) map.get("matchPlays");
		Map<String, Object> backDataMap = getBackMatchData(playType);
		if(null != backDataMap && backDataMap.size() > 0) {
			Map<String, Object> matchPlay = new HashMap<String, Object>();
	    	for(Map.Entry<String, Object> entry : backDataMap.entrySet()) {
	    		JSONObject jo = (JSONObject) entry.getValue();
	    		Set<String> keys = matchs.keySet();
	    		if(!keys.contains(jo.getString("id"))) {
	    			matchs.put(jo.getString("id"), jo);
	    		}
	    		matchPlay.put(jo.getString("id"), jo);
	    		matchPlay.put("playType", playType);
	    	}
	    	matchPlays.add(matchPlay);
	    }
		map.put("matchs", matchs);
		map.put("matchPlays", matchPlays);
		return map;
	}
	
	/**
	 * 获取返回的赛事数据
	 * @return
	 */
	private Map<String, Object> getBackMatchData(String playType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("poolcode[]", playType);
		String json = NetWorkUtil.doGet(matchUrl, map, "UTF-8");
	    if (json.contains("error")) {
	        throw new ServiceException(RespStatusEnum.FAIL.getCode(), playType + "赛事查询失败");
	    }
	    JSONObject jsonObject = JSONObject.parseObject(json);
	    JSONObject jo = jsonObject.getJSONObject("data");
	    map = jo;
	    return map;
	}
	
	/**
	 * 组装赛事列表数据
	 * @param matchs
	 * @return
	 */
	private List<LotteryMatch> getLotteryMatchData(Map<String, JSONObject> matchs){
		List<LotteryMatch> lotteryMatchs = new LinkedList<LotteryMatch>();
		if(null != matchs && matchs.size() > 0) {
			for (Map.Entry<String, JSONObject> entry : matchs.entrySet()) {
				LotteryMatch lotteryMatch = new LotteryMatch();
				JSONObject jo = entry.getValue();
				lotteryMatch.setLeagueId(Integer.parseInt(jo.getString("l_id")));
				lotteryMatch.setLeagueName(jo.getString("l_cn"));
				lotteryMatch.setLeagueAddr(jo.getString("l_cn_abbr"));
				lotteryMatch.setChangciId(Integer.parseInt(jo.getString("id")));
				lotteryMatch.setChangci(jo.getString("num"));
				lotteryMatch.setHomeTeamId(Integer.parseInt(jo.getString("h_id")));
				lotteryMatch.setHomeTeamName(jo.getString("h_cn"));
				lotteryMatch.setHomeTeamRank(jo.getString("h_order"));
				lotteryMatch.setHomeTeamAbbr(jo.getString("h_cn_abbr"));
				lotteryMatch.setVisitingTeamId(Integer.parseInt(jo.getString("a_id")));
				lotteryMatch.setVisitingTeamName(jo.getString("a_cn"));
				lotteryMatch.setVisitingTeamRank(jo.getString("a_order"));
				lotteryMatch.setVisitingTeamAbbr(jo.getString("a_cn_abbr"));
				lotteryMatch.setMatchTime(jo.getString("date") + " " + jo.getString("time"));
				lotteryMatch.setShowTime(jo.getString("b_date"));
				lotteryMatch.setCreateTime(DateUtil.getCurrentTimeLong());
				lotteryMatch.setIsShow(ProjectConstant.IS_SHOW);
				lotteryMatch.setIsDel(ProjectConstant.IS_NOT_DEL);
				lotteryMatchs.add(lotteryMatch);
			}   
		}
		return lotteryMatchs;
	}
	
	/**
	 * 组装赛事玩法数据
	 * @param matchPlays
	 * @return
	 */
	private LotteryMatchPlay getLotteryMatchPlayData(JSONObject matchPlay, Integer matchId, String playType){
		if(null == matchPlay) return null;
		LotteryMatchPlay lotteryMatchPlay = new LotteryMatchPlay();
		lotteryMatchPlay.setMatchId(matchId);
		lotteryMatchPlay.setPlayContent(matchPlay.getString(playType));
		lotteryMatchPlay.setPlayType(PlayTypeUtil.getPlayTypeCode(playType));
		lotteryMatchPlay.setStatus(ProjectConstant.MATCH_PLAY_STATUS_SELLING);
		lotteryMatchPlay.setIsHot(ProjectConstant.MATCH_PLAY_NOT_HOT);
		lotteryMatchPlay.setIsDel(ProjectConstant.IS_NOT_DEL);
		lotteryMatchPlay.setCreateTime(DateUtil.getCurrentTimeLong());
		lotteryMatchPlay.setUpdateTime(DateUtil.getCurrentTimeLong());
		return lotteryMatchPlay;
	}
	
	/**
	 * 保存赛事数据
	 * @param lotteryMatchs
	 * @param matchPlays
	 */
	private void saveMatchData(List<LotteryMatch> lotteryMatchs, List<Map<String, Object>> matchPlays) {
		if(CollectionUtils.isNotEmpty(lotteryMatchs)) {
			for(LotteryMatch lotteryMatch : lotteryMatchs) {
				List<LotteryMatchPlay> lotteryMatchPlays = new LinkedList<LotteryMatchPlay>();
				lotteryMatchMapper.insertMatch(lotteryMatch);
				if(CollectionUtils.isNotEmpty(matchPlays)) {
					for(Map<String, Object> map : matchPlays) {
						for(Map.Entry<String, Object> entry : map.entrySet()) {
							if(lotteryMatch.getChangciId().toString().equals(entry.getKey())) {
								LotteryMatchPlay lotteryMatchPlay = getLotteryMatchPlayData((JSONObject)map.get(lotteryMatch.getChangciId().toString()), lotteryMatch.getMatchId(), map.get("playType").toString());
								if(null != lotteryMatchPlay) {
									lotteryMatchPlays.add(lotteryMatchPlay);
								}
								break;
							}
						}
					}
					if(CollectionUtils.isNotEmpty(lotteryMatchPlays)) {
						lotteryMatchPlayMapper.insertList(lotteryMatchPlays);
					}
				}
			}
		}
	}
	
}

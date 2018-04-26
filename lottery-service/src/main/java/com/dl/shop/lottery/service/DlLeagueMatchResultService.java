package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.MatchPlayTypeEnum;
import com.dl.base.enums.MatchResultHadEnum;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.NetWorkUtil;
import com.dl.shop.lottery.dao.DlLeagueMatchResultMapper;
import com.dl.shop.lottery.dao.LotteryMatchMapper;
import com.dl.shop.lottery.model.DlLeagueMatchResult;
import com.dl.shop.lottery.model.DlLeagueMatchResultStringDTO;
import com.dl.shop.lottery.model.LotteryMatch;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DlLeagueMatchResultService extends AbstractService<DlLeagueMatchResult> {
	
	private final static Logger logger = Logger.getLogger(DlLeagueMatchAsiaService.class);
	//竞彩网抓取赛事结果数据地址
	private final static String MATCH_RESULT_URL = "http://i.sporttery.cn/api/fb_match_info/get_pool_rs/?mid=";
	
    @Resource
    private DlLeagueMatchResultMapper dlLeagueMatchResultMapper;
    @Resource
    private LotteryMatchMapper dlMatchMapper;

    public void refreshMatchResultsFromZC(List<String> changciIds) {
		if(Collections.isEmpty(changciIds)) {
			return;
		}
		for(String changciId: changciIds) {
			if(StringUtils.isBlank(changciId)) {
				continue;
			}
			List<DlLeagueMatchResult> results = this.refreshMatchResultFromZC(Integer.valueOf(changciId));
		}
	}
    /**
     * 从竞彩网拉取比赛结果到数据库
     * @param matchId
     */
    public List<DlLeagueMatchResult> refreshMatchResultFromZC(Integer changciId) {
    	int num = dlLeagueMatchResultMapper.getCountByChangciId(changciId);
    	if(num == 0) {
    		List<DlLeagueMatchResult> rsts = this.getMatchResultFromZC(changciId);
    		if(null != rsts && rsts.size() > 0) {
    			super.save(rsts);
    			return rsts;
    		}
    	}
    	return null;
    }
    
    
    /**
     * 竞彩网拉取数据
     * @param matchId
     * @return
     */
	private List<DlLeagueMatchResult> getMatchResultFromZC(Integer changciId) {
		String reqUrl = MATCH_RESULT_URL + changciId;
    	String json = NetWorkUtil.doGet(reqUrl, new HashMap<String, Object>(), "utf-8");
	    if(StringUtils.isBlank(json)) {
	    	logger.info("");
	    	return null;
	    }
	    JSONObject jsonObject = JSONObject.parseObject(json);
	    JSONObject resultObj = jsonObject.getJSONObject("result");
	    if(null == resultObj) {
	    	logger.info("");
	    	return null;
	    }
	    JSONObject poolRsObj = null;
	    try {
			poolRsObj = resultObj.getJSONObject("pool_rs");
		} catch (Exception e) {
		}
	    if(null == poolRsObj) {
	    	logger.info("");
	    	return null;
	    }
	    LotteryMatch lotteryMatch = dlMatchMapper.getByChangciId(changciId);
	    if(null == lotteryMatch) {
	    	logger.info("");
	    	return null;
	    }
	    String playCode = lotteryMatch.getMatchSn();
	    List<DlLeagueMatchResult> list = new ArrayList<DlLeagueMatchResult>(5);
	    DlLeagueMatchResult crsMatchResult = this.crsMatchResult(changciId, poolRsObj);
	    if(null != crsMatchResult) {
	    	crsMatchResult.setPlayCode(playCode);
	    	crsMatchResult.setLeagueFrom(0);
	    	list.add(crsMatchResult);
	    }
	    DlLeagueMatchResult hadMatchResult = this.hadMatchResult(changciId, poolRsObj);
	    if(null != hadMatchResult) {
	    	hadMatchResult.setPlayCode(playCode);
	    	crsMatchResult.setLeagueFrom(0);
	    	list.add(hadMatchResult);
	    }
	    DlLeagueMatchResult hhadMatchResult = this.hhadMatchResult(changciId, poolRsObj);
	    if(null != hhadMatchResult) {
	    	hhadMatchResult.setPlayCode(playCode);
	    	crsMatchResult.setLeagueFrom(0);
	    	list.add(hhadMatchResult);
	    }
	    DlLeagueMatchResult ttgMatchResult = this.ttgMatchResult(changciId, poolRsObj);
	    if(null != ttgMatchResult) {
	    	ttgMatchResult.setPlayCode(playCode);
	    	crsMatchResult.setLeagueFrom(0);
	    	list.add(ttgMatchResult);
	    }
	    DlLeagueMatchResult hafuMatchResult = this.hafuMatchResult(changciId, poolRsObj);
	    if(null != hafuMatchResult) {
	    	hafuMatchResult.setPlayCode(playCode);
	    	crsMatchResult.setLeagueFrom(0);
	    	list.add(hafuMatchResult);
	    }
		return list;
	}

	/**
	 * 胜平负结果
	 * @return
	 */
	private DlLeagueMatchResult hadMatchResult(Integer changciId, JSONObject poolRsObj) {
		JSONObject hadObj = poolRsObj.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_HAD.getMsg());
		if(null == hadObj)return null;
		DlLeagueMatchResult matchResult = new DlLeagueMatchResult();
		matchResult.setChangciId(changciId);
		String rsCode = hadObj.getString("pool_rs");
		if("h".equals(rsCode)) {
			rsCode = MatchResultHadEnum.HAD_H.getCode().toString();
		}else if("a".equals(rsCode)) {
			rsCode = MatchResultHadEnum.HAD_A.getCode().toString();
		}else {
			rsCode = MatchResultHadEnum.HAD_D.getCode().toString();
		}
		matchResult.setCellCode(rsCode);
		String rsName = hadObj.getString("prs_name");
		matchResult.setCellName(rsName);
		String goalline = hadObj.getString("goalline");
		matchResult.setGoalline(goalline);
		Integer single = hadObj.getInteger("single");
		matchResult.setSingle(single);
		Double odds = hadObj.getDouble("odds");
		matchResult.setOdds(odds);
		matchResult.setCreateTime(DateUtil.getCurrentTimeLong());
		matchResult.setPlayType(MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode());
		return matchResult;
	}
	/**
	 * 半全场结果
	 * @return
	 */
	private DlLeagueMatchResult hafuMatchResult(Integer changciId, JSONObject poolRsObj) {
		 JSONObject hafuObj = poolRsObj.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_HAFU.getMsg());
		 if(null == hafuObj)return null;
		DlLeagueMatchResult matchResult = new DlLeagueMatchResult();
		matchResult.setChangciId(changciId);
		String rsCode = hafuObj.getString("pool_rs");
		if(StringUtils.isNotBlank(rsCode)) {
			rsCode = rsCode.replaceAll("h", "3").replaceAll("a", "0").replaceAll("d", "1");
		}
		matchResult.setCellCode(rsCode);
		String rsName = hafuObj.getString("prs_name");
		matchResult.setCellName(rsName);
		String goalline = hafuObj.getString("goalline");
		matchResult.setGoalline(goalline);
		Integer single = hafuObj.getInteger("single");
		matchResult.setSingle(single);
		Double odds = hafuObj.getDouble("odds");
		matchResult.setOdds(odds);
		matchResult.setCreateTime(DateUtil.getCurrentTimeLong());
		matchResult.setPlayType(MatchPlayTypeEnum.PLAY_TYPE_HAFU.getcode());
		return matchResult;
	}
	/**
	 * 让球胜平负结果
	 * @return
	 */
	private DlLeagueMatchResult hhadMatchResult(Integer changciId, JSONObject poolRsObj) {
		JSONObject hhadObj = poolRsObj.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getMsg());
		if(null == hhadObj)return null;
		DlLeagueMatchResult matchResult = new DlLeagueMatchResult();
		matchResult.setChangciId(changciId);
		String rsCode = hhadObj.getString("pool_rs");
		if("h".equals(rsCode)) {
			rsCode = MatchResultHadEnum.HAD_H.getCode().toString();
		}else if("a".equals(rsCode)) {
			rsCode = MatchResultHadEnum.HAD_A.getCode().toString();
		}else {
			rsCode = MatchResultHadEnum.HAD_D.getCode().toString();
		}
		String rsName = hhadObj.getString("prs_name");
		matchResult.setCellCode(rsCode);
		matchResult.setCellName(rsName);
		String goalline = hhadObj.getString("goalline");
		matchResult.setGoalline(goalline);
		Integer single = hhadObj.getInteger("single");
		matchResult.setSingle(single);
		Double odds = hhadObj.getDouble("odds");
		matchResult.setOdds(odds);
		matchResult.setCreateTime(DateUtil.getCurrentTimeLong());
		matchResult.setPlayType(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode());
		return matchResult;
	}
	/**
	 * 总进球
	 * @return
	 */
	private DlLeagueMatchResult ttgMatchResult(Integer changciId, JSONObject poolRsObj) {
		 JSONObject ttgObj = poolRsObj.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_TTG.getMsg());
		 if(null == ttgObj)return null;
		DlLeagueMatchResult matchResult = new DlLeagueMatchResult();
		matchResult.setChangciId(changciId);
		String rsCode = ttgObj.getString("pool_rs");
		if(StringUtils.isNotBlank(rsCode) && rsCode.length() > 1) {
			rsCode = rsCode.substring(1);
		}
		matchResult.setCellCode(rsCode);
		String rsName = ttgObj.getString("prs_name");
		matchResult.setCellName(rsName);
		String goalline = ttgObj.getString("goalline");
		matchResult.setGoalline(goalline);
		Integer single = ttgObj.getInteger("single");
		matchResult.setSingle(single);
		Double odds = ttgObj.getDouble("odds");
		matchResult.setOdds(odds);
		matchResult.setCreateTime(DateUtil.getCurrentTimeLong());
		matchResult.setPlayType(MatchPlayTypeEnum.PLAY_TYPE_TTG.getcode());
		return matchResult;
	}
	/**
	 * 比分结果
	 * @return
	 */
	private DlLeagueMatchResult crsMatchResult(Integer changciId, JSONObject poolRsObj) {
		JSONObject crsObj = poolRsObj.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_CRS.getMsg());
		if(null == crsObj)return null;
	    DlLeagueMatchResult matchResult = new DlLeagueMatchResult();
	    matchResult.setChangciId(changciId);
	    String rsCode = crsObj.getString("pool_rs");
	    if(StringUtils.isNotBlank(rsCode) && rsCode.length() == 4) {
	    	rsCode = String.valueOf(new char[] {rsCode.charAt(1),rsCode.charAt(3)});
	    }
	    matchResult.setCellCode(rsCode);
	    String rsName = crsObj.getString("prs_name");
	    matchResult.setCellName(rsName);
	    String goalline = crsObj.getString("goalline");
	    matchResult.setGoalline(goalline);
	    Integer single = crsObj.getInteger("single");
	    matchResult.setSingle(single);
	    Double odds = crsObj.getDouble("odds");
	    matchResult.setOdds(odds);
	    matchResult.setCreateTime(DateUtil.getCurrentTimeLong());
	    matchResult.setPlayType(MatchPlayTypeEnum.PLAY_TYPE_CRS.getcode());
	    return matchResult;
	}

	/**
	 * 
	 * @param playCode
	 * @return
	 */
	public List<DlLeagueMatchResult> queryMatchResultByPlayCode(String playCode){
		List<DlLeagueMatchResult> matchResultList = dlLeagueMatchResultMapper.queryMatchResultByPlayCode(playCode);
		if(matchResultList.size() == 0) {
			return new ArrayList<DlLeagueMatchResult>();
		}
		
		/*if(matchResultList.size() != 5) {
			log.error("期次为:"+playCode+"的开赛结果不是5个,请检查数据库");
			return new ArrayList<DlLeagueMatchResult>();
		}*/
		return matchResultList;
	}
	
	public List<DlLeagueMatchResult> queryMatchResultsByPlayCodes(List<String> playCodes){
		List<DlLeagueMatchResult> matchResultList = dlLeagueMatchResultMapper.queryMatchResultsByPlayCodes(playCodes);
		if(matchResultList.size() == 0) {
			return new ArrayList<DlLeagueMatchResult>();
		}
		return matchResultList;
	}
}

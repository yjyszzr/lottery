package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.dl.shop.lottery.model.LotteryMatch;

@Service
@Transactional
public class DlLeagueMatchResultService extends AbstractService<DlLeagueMatchResult> {
	
	private final static Logger logger = Logger.getLogger(DlLeagueMatchAsiaService.class);
	//竞彩网抓取赛事结果数据地址
	private final static String MATCH_RESULT_URL = "http://i.sporttery.cn/api/fb_match_info/get_pool_rs/?mid=";
	
    @Resource
    private DlLeagueMatchResultMapper dlLeagueMatchResultMapper;
    @Resource
    private LotteryMatchMapper dlMatchMapper;

    /**
     * 从竞彩网拉取亚盘数据到数据库
     * @param matchId
     */
    public void refreshMatchResultFromZC(Integer matchId) {
    	int num = dlLeagueMatchResultMapper.getCountByMatchId(matchId);
    	if(num == 0) {
    		List<DlLeagueMatchResult> rsts = this.getMatchResultFromZC(matchId);
    		if(null != rsts && rsts.size() > 0) {
    			super.save(rsts);
    		}
    	}
    }
    
    
    /**
     * 竞彩网拉取数据
     * @param matchId
     * @return
     */
	private List<DlLeagueMatchResult> getMatchResultFromZC(Integer matchId) {
		String reqUrl = MATCH_RESULT_URL + matchId;
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
	    LotteryMatch byMatchId = dlMatchMapper.getByMatchId(matchId);
	    if(null == byMatchId) {
	    	logger.info("");
	    	return null;
	    }
	    String playCode = byMatchId.getMatchSn();
	    Integer changciId = byMatchId.getChangciId();
	    List<DlLeagueMatchResult> list = new ArrayList<DlLeagueMatchResult>(5);
	    DlLeagueMatchResult crsMatchResult = this.crsMatchResult(changciId, poolRsObj);
	    if(null != crsMatchResult) {
	    	crsMatchResult.setPlayCode(playCode);
	    	list.add(crsMatchResult);
	    }
	    DlLeagueMatchResult hadMatchResult = this.hadMatchResult(changciId, poolRsObj);
	    if(null != hadMatchResult) {
	    	hadMatchResult.setPlayCode(playCode);
	    	list.add(hadMatchResult);
	    }
	    DlLeagueMatchResult hhadMatchResult = this.hhadMatchResult(changciId, poolRsObj);
	    if(null != hhadMatchResult) {
	    	hhadMatchResult.setPlayCode(playCode);
	    	list.add(hhadMatchResult);
	    }
	    DlLeagueMatchResult ttgMatchResult = this.ttgMatchResult(changciId, poolRsObj);
	    if(null != ttgMatchResult) {
	    	ttgMatchResult.setPlayCode(playCode);
	    	list.add(ttgMatchResult);
	    }
	    DlLeagueMatchResult hafuMatchResult = this.hafuMatchResult(changciId, poolRsObj);
	    if(null != hafuMatchResult) {
	    	hafuMatchResult.setPlayCode(playCode);
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
		DlLeagueMatchResult matchResult = new DlLeagueMatchResult();
		matchResult.setChangciId(changciId);
		String rsCode = hafuObj.getString("pool_rs");
		if(StringUtils.isNotBlank(rsCode)) {
			rsCode.replaceAll("h", "3").replaceAll("a", "0").replaceAll("d", "1");
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
}

package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.DlLeagueMatchAsia;
import com.dl.shop.lottery.model.DlMatchSupport;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.dao.DlMatchSupportMapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.MatchPlayTypeEnum;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.NetWorkUtil;
import com.dl.lottery.dto.TeamSupportDTO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

@Service
@Transactional
public class DlMatchSupportService extends AbstractService<DlMatchSupport> {
    @Resource
    private DlMatchSupportMapper dlMatchSupportMapper;
    

    private final static String SUPPORT_URL = "http://i.sporttery.cn/odds_calculator/get_proportion?i_format=json";
    
    public void refreshMatchSupports() {
    	List<DlMatchSupport> supports = this.getMatchSupportsFromZC();
    	if(CollectionUtils.isNotEmpty(supports)) {
    		for(DlMatchSupport support: supports) {
    			DlMatchSupport exitObj = dlMatchSupportMapper.getByChangciIdAndPlayType(support.getChangciId(), support.getPlayType());
    			if(null != exitObj) {
    				support.setId(exitObj.getId());
    				support.setCreateTime(exitObj.getCreateTime());
    				dlMatchSupportMapper.updateByPrimaryKey(support);
    			}else {
    				dlMatchSupportMapper.insert(support);
    			}
    		}
    	}
    }
    /**
     * 获取当前赛事的支持率
     * @return
     */
	private List<DlMatchSupport> getMatchSupportsFromZC() {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
//		hashMap.put("pool[]", new String[]{"had","hhad"});
		String json = NetWorkUtil.doGet(SUPPORT_URL, hashMap, "utf-8");
	    if(StringUtils.isBlank(json)) {
	    	return null;
	    }
	    JSONObject jsonObject = JSONObject.parseObject(json);
	    JSONObject jsonObj = jsonObject.getJSONObject("data");
	    if(null == jsonObj || jsonObj.size() == 0) {
	    	return null;
	    }
	    int time = DateUtil.getCurrentTimeLong();
	    List<DlMatchSupport> supports = new ArrayList<DlMatchSupport>(jsonObj.size()*2);
	    Set<String> keySet = jsonObj.keySet();
	   for(String key: keySet) {
		   JSONObject supportObj = jsonObj.getJSONObject(key);
		   if(null == supportObj) {
			   continue;
		   }
		   JSONObject hadObj = supportObj.getJSONObject("had");
		   if(null != hadObj) {
			   DlMatchSupport dto = this.matchSupportFromJson(hadObj, time);
			   supports.add(dto);
		   }
		   JSONObject hhadObj = supportObj.getJSONObject("hhad");
		   if(null != hhadObj) {
			   DlMatchSupport dto = this.matchSupportFromJson(hhadObj, time);
			   supports.add(dto);
		   }
	   }
		return supports;
	}
	private DlMatchSupport matchSupportFromJson(JSONObject hadObj, int time) {
		DlMatchSupport dto = new DlMatchSupport();
		   dto.setChangciId(hadObj.getInteger("mid"));
		   dto.setDrawNum(hadObj.getInteger("draw"));
		   dto.setLeagueFrom(0);
		   dto.setLoseNum(hadObj.getInteger("lose"));
		   String type = hadObj.getString("type");
		   if(type.equals(MatchPlayTypeEnum.PLAY_TYPE_HAD.getMsg())) {
			   dto.setPlayType(MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode());
		   }else if(type.equals(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getMsg())) {
			   dto.setPlayType(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode());
		   }
		   dto.setPreDraw(hadObj.getString("pre_draw"));
		   dto.setPreLose(hadObj.getString("pre_lose"));
		   dto.setPreWin(hadObj.getString("pre_win"));
		   dto.setSupportId(hadObj.getInteger("id"));
		   dto.setTotal(hadObj.getInteger("total"));
		   dto.setWinNum(hadObj.getInteger("win"));
		   dto.setUpdateTime(time);
		   dto.setCreateTime(time);
		return dto;
	}

	public TeamSupportDTO matchSupports(LotteryMatch lotteryMatch, int playType) {
		Integer changciId = lotteryMatch.getChangciId();
		DlMatchSupport matchSupport = dlMatchSupportMapper.getByChangciIdAndPlayType(changciId, playType);
		TeamSupportDTO dto = new TeamSupportDTO();
		if(null != matchSupport) {
			dto.setASupport(matchSupport.getPreLose());
			dto.setDSupport(matchSupport.getPreDraw());
			dto.setHSupport(matchSupport.getPreWin());
			dto.setFixedOdds("");
		}
		return dto;
	}

}

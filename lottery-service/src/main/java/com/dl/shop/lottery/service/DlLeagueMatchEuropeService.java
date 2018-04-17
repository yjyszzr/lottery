package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.NetWorkUtil;
import com.dl.lottery.dto.LeagueMatchEuropeDTO;
import com.dl.shop.lottery.core.MatchChangeEnum;
import com.dl.shop.lottery.dao.DlLeagueMatchEuropeMapper;
import com.dl.shop.lottery.model.DlLeagueMatchAsia;
import com.dl.shop.lottery.model.DlLeagueMatchEurope;

import tk.mybatis.mapper.entity.Condition;

@Service
@Transactional
public class DlLeagueMatchEuropeService extends AbstractService<DlLeagueMatchEurope> {
	
	private final static Logger logger = Logger.getLogger(DlLeagueMatchEuropeService.class);
	//竞彩网抓取亚盘数据地址
	private final static String MATCH_EUROPE_URL = "http://i.sporttery.cn/api/fb_match_info/get_europe/?mid=";
	
	@Resource
    private DlLeagueMatchEuropeMapper dlLeagueMatchEuropeMapper;

	public void refreshMatchEuropeInfoFromZC(Integer changciId) {
		List<DlLeagueMatchEurope> europes = this.getMatchEuropeFromZC(changciId);
    	if(null != europes && europes.size() > 0) {
    		this.saveMatchEurope(changciId, europes);
    	}
	}

	private void saveMatchEurope(Integer changciId, List<DlLeagueMatchEurope> europes) {
		int num = dlLeagueMatchEuropeMapper.getCountByChangciId(changciId);
		if(num == 0) {
			super.save(europes);
		}else {
			for(DlLeagueMatchEurope europe: europes) {
				int srt = dlLeagueMatchEuropeMapper.updateMatchEurope(europe);
			}
		}
	}

	/**
	 * 竞彩网摘取数据
	 * @param changciId
	 * @return
	 */
	private List<DlLeagueMatchEurope> getMatchEuropeFromZC(Integer changciId) {
		String reqUrl = MATCH_EUROPE_URL + changciId;
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
	    JSONArray jsonArray = resultObj.getJSONArray("data");
	    if(null == jsonArray || jsonArray.size() == 0) {
	    	logger.info("");
	    	return null;
	    }
	    List<DlLeagueMatchEurope> europes = new ArrayList<DlLeagueMatchEurope>(jsonArray.size());
	    for(int i=0; i< jsonArray.size(); i++) {
	    	JSONObject europeObj = jsonArray.getJSONObject(i);
	    	if(null == europeObj) {
	    		continue;
	    	}
	    	DlLeagueMatchEurope europe = new DlLeagueMatchEurope();
	    	europe.setChangciId(changciId);
	    	europe.setComName(europeObj.getString("cn"));
	    	String o1Change = europeObj.getString("draw_change");
	    	Integer drawChange = MatchChangeEnum.getCode(o1Change);
	    	europe.setDrawChange(drawChange);
	    	europe.setDrawIndex(europeObj.getDouble("draw_index"));
	    	europe.setDrawRatio(europeObj.getString("draw_ratio"));
	    	europe.setEuropeId(europeObj.getInteger("id"));
	    	europe.setInitDraw(europeObj.getDouble("draw_s"));
	    	europe.setInitLose(europeObj.getDouble("lose_s"));
	    	europe.setInitWin(europeObj.getDouble("win_s"));
	    	europe.setLeagueFrom(0);
	    	String o2Change = europeObj.getString("lose_change");
	    	Integer loseChange = MatchChangeEnum.getCode(o2Change);
	    	europe.setLoseChange(loseChange);
	    	europe.setLoseIndex(europeObj.getDouble("lose_index"));
	    	europe.setLoseRatio(europeObj.getString("lose_ratio"));
	    	europe.setOrderNum(europeObj.getInteger("order"));
	    	europe.setPer(europeObj.getString("per"));
	    	europe.setRealDraw(europeObj.getDouble("draw"));
	    	europe.setRealLose(europeObj.getDouble("lose"));
	    	europe.setRealWin(europeObj.getDouble("win"));
	    	europe.setTimeMinus(europeObj.getInteger("time_minus"));
	    	String o3Change = europeObj.getString("win_change");
	    	Integer winChange = MatchChangeEnum.getCode(o3Change);
	    	europe.setWinChange(winChange);
	    	europe.setWinIndex(europeObj.getDouble("win_index"));
	    	europe.setWinRatio(europeObj.getString("win_ratio"));
	    	Integer currentTimeLong = DateUtil.getCurrentTimeLong();
	    	europe.setUpdateTime(currentTimeLong);
	    	europe.setCreateTime(currentTimeLong);
	    	europes.add(europe);
	    }
		return europes;
	}

	/**
	 * 获取
	 * @param changciId
	 * @return
	 */
	public List<LeagueMatchEuropeDTO> leagueMatchEuropes(Integer changciId) {
		Condition condition = new Condition(DlLeagueMatchAsia.class);
        condition.and().andEqualTo("changciId", changciId);
        List<DlLeagueMatchEurope> list = super.findByCondition(condition);
        if(list.size() == 0) {
        	 this.refreshMatchEuropeInfoFromZC(changciId);
        	 list = super.findByCondition(condition);
        }
        List<LeagueMatchEuropeDTO> dtos = new ArrayList<LeagueMatchEuropeDTO>(0);
        if(null != list) {
        	dtos = new ArrayList<LeagueMatchEuropeDTO>(list.size());
        	for(DlLeagueMatchEurope europe: list) {
        		LeagueMatchEuropeDTO dto = new LeagueMatchEuropeDTO();
        		dto.setChangciId(changciId);
        		dto.setComName(europe.getComName());
        		dto.setDrawChange(europe.getDrawChange());
        		dto.setDrawIndex(europe.getDrawIndex());
        		dto.setDrawRatio(europe.getDrawRatio());
        		dto.setEuropeId(europe.getEuropeId());
        		dto.setInitDraw(europe.getInitDraw());
        		dto.setInitLose(europe.getInitLose());
        		dto.setInitWin(europe.getInitWin());
        		dto.setLoseChange(europe.getLoseChange());
        		dto.setLoseIndex(europe.getLoseIndex());
        		dto.setLoseRatio(europe.getLoseRatio());
        		dto.setOrderNum(europe.getOrderNum());
        		dto.setPer(europe.getPer());
        		dto.setRealDraw(europe.getRealDraw());
        		dto.setRealLose(europe.getRealLose());
        		dto.setRealWin(europe.getRealWin());
        		dto.setTimeMinus(europe.getTimeMinus());
        		dto.setWinChange(europe.getWinChange());
        		dto.setWinIndex(europe.getWinIndex());
        		dto.setWinRatio(europe.getWinRatio());
        		dtos.add(dto);
        	}
        }
		return dtos;
	}

}

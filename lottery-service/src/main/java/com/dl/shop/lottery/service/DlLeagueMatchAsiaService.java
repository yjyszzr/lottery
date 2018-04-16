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
import com.dl.lottery.dto.LeagueMatchAsiaDTO;
import com.dl.shop.lottery.core.MatchChangeEnum;
import com.dl.shop.lottery.dao.DlLeagueMatchAsiaMapper;
import com.dl.shop.lottery.model.DlLeagueMatchAsia;

import tk.mybatis.mapper.entity.Condition;

@Service
@Transactional
public class DlLeagueMatchAsiaService extends AbstractService<DlLeagueMatchAsia> {
	
	private final static Logger logger = Logger.getLogger(DlLeagueMatchAsiaService.class);
	//竞彩网抓取亚盘数据地址
	private final static String MATCH_ASIA_URL = "http://i.sporttery.cn/api/fb_match_info/get_asia/?mid=";
	
    @Resource
    private DlLeagueMatchAsiaMapper dlLeagueMatchAsiaMapper;

    public List<LeagueMatchAsiaDTO> leagueMatchAsias(Integer changciId) {
    	Condition condition = new Condition(DlLeagueMatchAsia.class);
        condition.and().andEqualTo("changciId", changciId);
        List<DlLeagueMatchAsia> list = super.findByCondition(condition);
        if(list.size() == 0) {
        	 this.refreshMatchAsiaInfoFromZC(changciId);
        	 list = super.findByCondition(condition);
        }
        List<LeagueMatchAsiaDTO> dtos = new ArrayList<LeagueMatchAsiaDTO>(0);
        if(null != list) {
        	dtos = new ArrayList<LeagueMatchAsiaDTO>(list.size());
        	for(DlLeagueMatchAsia asia: list) {
        		LeagueMatchAsiaDTO dto = new LeagueMatchAsiaDTO();
        		dto.setAsiaId(asia.getAsiaId());
        		dto.setComName(asia.getComName());
        		dto.setIndexA(asia.getIndexA());
        		dto.setIndexH(asia.getIndexH());
        		dto.setInitOdds1(asia.getInitOdds1());
        		dto.setInitOdds2(asia.getInitOdds2());
        		dto.setInitRule(asia.getInitRule());
        		dto.setChangciId(asia.getChangciId());
        		dto.setOdds1Change(asia.getOdds1Change());
        		dto.setOdds2Change(asia.getOdds2Change());
        		dto.setRatioA(asia.getRatioA());
        		dto.setRatioH(asia.getRatioH());
        		dto.setRealOdds1(asia.getRealOdds1());
        		dto.setRealOdds2(asia.getRealOdds2());
        		dto.setRealRule(asia.getRealRule());
        		dto.setTimeMinus(asia.getTimeMinus());
        		dtos.add(dto);
        	}
        }
        return dtos;
    }
    /**
     * 从竞彩网拉取亚盘数据到数据库
     * @param matchId
     */
    public void refreshMatchAsiaInfoFromZC(Integer changciId) {
    	List<DlLeagueMatchAsia> asias = this.getMatchAsiasFromZC(changciId);
    	if(null != asias && asias.size() > 0) {
    		this.saveMatchAsias(changciId, asias);
    	}
    }
    /**
     * 竞彩网拉取数据
     * @param matchId
     * @return
     */
	private List<DlLeagueMatchAsia> getMatchAsiasFromZC(Integer changciId) {
		String reqUrl = MATCH_ASIA_URL + changciId;
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
	    List<DlLeagueMatchAsia> asias = new ArrayList<DlLeagueMatchAsia>(jsonArray.size());
	   for(int i=0; i< jsonArray.size(); i++) {
		   JSONObject asiaObj = jsonArray.getJSONObject(i);
		   if(null == asiaObj) {
			   continue;
		   }
		   DlLeagueMatchAsia leagueMatchAsia = new DlLeagueMatchAsia();
		   leagueMatchAsia.setChangciId(changciId);
		   leagueMatchAsia.setAsiaId(asiaObj.getInteger("id"));
		   leagueMatchAsia.setComName(asiaObj.getString("cn"));
		   leagueMatchAsia.setIndexA(asiaObj.getDouble("o2_index"));
		   leagueMatchAsia.setIndexH(asiaObj.getDouble("o1_index"));
		   leagueMatchAsia.setInitOdds1(asiaObj.getDouble("o1_s"));
		   leagueMatchAsia.setInitOdds2(asiaObj.getDouble("o2_s"));
		   leagueMatchAsia.setInitRule(asiaObj.getString("o3_s"));
		   leagueMatchAsia.setLeagueFrom(0);
		   String o1Change = asiaObj.getString("o1_change");
		   Integer code1 = MatchChangeEnum.getCode(o1Change);
		   String o21Change = asiaObj.getString("o2_change");
		   Integer code2 = MatchChangeEnum.getCode(o21Change);
		   leagueMatchAsia.setOdds1Change(null == code1?0:code1);
		   leagueMatchAsia.setOdds2Change(null == code2?0:code2);
		   leagueMatchAsia.setRatioA(asiaObj.getDouble("o1_ratio"));
		   leagueMatchAsia.setRatioH(asiaObj.getDouble("o2_ratio"));
		   leagueMatchAsia.setRealOdds1(asiaObj.getDouble("o1"));
		   leagueMatchAsia.setRealOdds2(asiaObj.getDouble("o2"));
		   leagueMatchAsia.setRealRule(asiaObj.getString("o3"));
		   leagueMatchAsia.setTimeMinus(asiaObj.getInteger("time_minus"));
		   Integer currentTimeLong = DateUtil.getCurrentTimeLong();
		   leagueMatchAsia.setCreateTime(currentTimeLong);
		   leagueMatchAsia.setUpdateTime(currentTimeLong);
		   asias.add(leagueMatchAsia);
	   }
		return asias;
	}
    /**
     * 保存亚盘信息
     * @param matchId
     * @param asias
     */
	private void saveMatchAsias(Integer changciId, List<DlLeagueMatchAsia> asias) {
		int num = dlLeagueMatchAsiaMapper.getCountByChangciId(changciId);
		if(num == 0) {
			super.save(asias);
		}else {
			for(DlLeagueMatchAsia asia: asias) {
				int srt = dlLeagueMatchAsiaMapper.updateMatchAsia(asia);
			}
		}
	}
}

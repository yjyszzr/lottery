package com.dl.shop.lottery.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.exception.ServiceException;
import com.dl.dto.DlJcZqMatchDTO;
import com.dl.dto.DlJcZqMatchListDTO;
import com.dl.param.DlJcZqMatchListParam;

@Service
public class LotteryMatchService {

	@Resource
    private RestTemplate restTemplate;
	
	public DlJcZqMatchListDTO getMatchList(DlJcZqMatchListParam param) {
		DlJcZqMatchListDTO dlJcZqMatchListDTO = new DlJcZqMatchListDTO();
		String url = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&poolcode[]=" + param.getPlayType();
	    String json = restTemplate.getForObject(url, String.class);
	    if (json.contains("error")) {
	        throw new ServiceException(RespStatusEnum.FAIL.getCode(), "赛事查询失败");
	    }
	    JSONObject jsonObject = JSONObject.parseObject(json);
	    JSONArray jsonArray = jsonObject.getJSONArray("data");
	    if(null != jsonArray) {
	    	for(int i=0;i<jsonArray.size();i++) {
	    		JSONObject jo = (JSONObject) jsonArray.get(i);
	    		DlJcZqMatchDTO DlJcZqMatchDTO = new DlJcZqMatchDTO();
	    	}
	    }
	    
	    return dlJcZqMatchListDTO;
	}
	
}

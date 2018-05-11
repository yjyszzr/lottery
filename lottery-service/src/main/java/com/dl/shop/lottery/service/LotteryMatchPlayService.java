package com.dl.shop.lottery.service;
import com.dl.shop.lottery.dao2.LotteryMatchPlayMapper;
import com.dl.shop.lottery.model.LotteryMatchPlay;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.MatchPlayTypeEnum;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(value="transactionManager2")
public class LotteryMatchPlayService extends AbstractService<LotteryMatchPlay> {
    @Resource
    private LotteryMatchPlayMapper lotteryMatchPlayMapper;

    public String fixedOddsByChangciId(Integer changciId) {
    	LotteryMatchPlay matchPlay = lotteryMatchPlayMapper.lotteryMatchPlayByChangciIdAndPlayType(changciId, MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode());
    	if(matchPlay != null) {
    		String playContent = matchPlay.getPlayContent();
    		JSONObject jsonObj = JSON.parseObject(playContent);
    		return jsonObj.getString("fixedodds");
    	}
    	return "";
    }
}

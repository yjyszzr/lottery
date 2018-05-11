package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.PeriodReward;
import com.dl.shop.lottery.dao.PeriodRewardMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional(value="transactionManager1")
public class PeriodRewardService extends AbstractService<PeriodReward> {
    @Resource
    private PeriodRewardMapper periodRewardMapper;
    
    public List<PeriodReward> queryValidPeriodReward(){
    	
    	return null;
    }

}

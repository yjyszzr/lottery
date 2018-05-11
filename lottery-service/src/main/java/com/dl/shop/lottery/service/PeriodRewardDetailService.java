package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.PeriodRewardDetail;
import com.dl.shop.lottery.dao.PeriodRewardDetailMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(value="transactionManager1")
public class PeriodRewardDetailService extends AbstractService<PeriodRewardDetail> {
    @Resource
    private PeriodRewardDetailMapper periodRewardDetailMapper;

}

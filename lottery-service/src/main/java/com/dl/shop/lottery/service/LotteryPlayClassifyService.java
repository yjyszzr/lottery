package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.LotteryPlayClassify;
import com.dl.shop.lottery.dao.LotteryPlayClassifyMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(value="transactionManager1")
public class LotteryPlayClassifyService extends AbstractService<LotteryPlayClassify> {
    @Resource
    private LotteryPlayClassifyMapper lotteryPlayClassifyMapper;

}

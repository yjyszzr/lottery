package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.LotteryNavBanner;
import com.dl.shop.lottery.dao.LotteryNavBannerMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(value="transactionManager1")
public class LotteryNavBannerService extends AbstractService<LotteryNavBanner> {
    @Resource
    private LotteryNavBannerMapper lotteryNavBannerMapper;

}

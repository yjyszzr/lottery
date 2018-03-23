package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.LotteryActivity;
import com.dl.shop.lottery.dao.LotteryActivityMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class LotteryActivityService extends AbstractService<LotteryActivity> {
    @Resource
    private LotteryActivityMapper lotteryActivityMapper;

}

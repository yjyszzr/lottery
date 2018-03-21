package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.LotteryMatchPlay;
import com.dl.shop.lottery.dao.LotteryMatchPlayMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class LotteryMatchPlayService extends AbstractService<LotteryMatchPlay> {
    @Resource
    private LotteryMatchPlayMapper lotteryMatchPlayMapper;

}

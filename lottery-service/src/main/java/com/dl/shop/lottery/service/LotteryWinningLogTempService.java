package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.LotteryWinningLogTemp;
import com.dl.shop.lottery.dao.LotteryWinningLogTempMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class LotteryWinningLogTempService extends AbstractService<LotteryWinningLogTemp> {
    @Resource
    private LotteryWinningLogTempMapper lotteryWinningLogTempMapper;

}

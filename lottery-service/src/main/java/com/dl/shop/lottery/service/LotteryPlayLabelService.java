package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.LotteryPlayLabel;
import com.dl.shop.lottery.dao.LotteryPlayLabelMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class LotteryPlayLabelService extends AbstractService<LotteryPlayLabel> {
    @Resource
    private LotteryPlayLabelMapper lotteryPlayLabelMapper;

}

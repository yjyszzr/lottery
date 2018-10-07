package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.DlResultBasketball;
import com.dl.shop.lottery.dao2.DlResultBasketballMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlResultBasketballService extends AbstractService<DlResultBasketball> {
    @Resource
    private DlResultBasketballMapper dlResultBasketballMapper;

}

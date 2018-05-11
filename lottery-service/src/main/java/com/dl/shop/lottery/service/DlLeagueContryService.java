package com.dl.shop.lottery.service;
import com.dl.shop.lottery.dao2.DlLeagueContryMapper;
import com.dl.shop.lottery.model.DlLeagueContry;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(value="transactionManager2")
public class DlLeagueContryService extends AbstractService<DlLeagueContry> {
    @Resource
    private DlLeagueContryMapper dlLeagueContryMapper;

}

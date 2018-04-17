package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.DlLeagueContry;
import com.dl.shop.lottery.dao.DlLeagueContryMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlLeagueContryService extends AbstractService<DlLeagueContry> {
    @Resource
    private DlLeagueContryMapper dlLeagueContryMapper;

}

package com.dl.shop.lottery.service;
import com.dl.shop.lottery.dao2.DlLeagueGroupMapper;
import com.dl.shop.lottery.model.DlLeagueGroup;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(value="transactionManager2")
public class DlLeagueGroupService extends AbstractService<DlLeagueGroup> {
    @Resource
    private DlLeagueGroupMapper dlLeagueGroupMapper;

}

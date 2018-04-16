package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.DlLeagueGroup;
import com.dl.shop.lottery.dao.DlLeagueGroupMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlLeagueGroupService extends AbstractService<DlLeagueGroup> {
    @Resource
    private DlLeagueGroupMapper dlLeagueGroupMapper;

}

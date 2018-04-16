package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.DlLeagueTeam;
import com.dl.shop.lottery.dao.DlLeagueTeamMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlLeagueTeamService extends AbstractService<DlLeagueTeam> {
    @Resource
    private DlLeagueTeamMapper dlLeagueTeamMapper;

}

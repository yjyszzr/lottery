package com.dl.shop.lottery.service;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao2.DlLeaguePlayerMapper;
import com.dl.shop.lottery.model.DlLeaguePlayer;

@Service
@Transactional(value = "transactionManager2")
@Slf4j
public class DlLeaguePlayerService extends AbstractService<DlLeaguePlayer> {
	@Resource
	private DlLeaguePlayerMapper dlLeaguePlayerMapper;

	public List<DlLeaguePlayer> findByTeamId(Integer teamId) {
		return dlLeaguePlayerMapper.findByTeamId(teamId);
	}

}

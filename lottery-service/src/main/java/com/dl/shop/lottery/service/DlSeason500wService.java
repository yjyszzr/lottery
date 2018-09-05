package com.dl.shop.lottery.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao2.DlSeason500wMapper;
import com.dl.shop.lottery.model.DlSeason500w;

@Service
@Transactional(value = "transactionManager2")
public class DlSeason500wService extends AbstractService<DlSeason500w> {
	@Resource
	private DlSeason500wMapper dlSeason500wMapper;

	public List<DlSeason500w> findAllSeason() {
		return dlSeason500wMapper.findAllSeason();
	}

	public Integer getlastSeasonByLeagueId(Integer leagueId) {
		return dlSeason500wMapper.getlastSeasonByLeagueId(leagueId);
	}

	public List<DlSeason500w> findSeasonByLeagueId(Integer leagueId) {
		return dlSeason500wMapper.findSeasonByLeagueId(leagueId);
	}

}

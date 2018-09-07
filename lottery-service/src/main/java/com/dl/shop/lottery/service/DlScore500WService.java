package com.dl.shop.lottery.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao2.DlScore500WMapper;
import com.dl.shop.lottery.model.DlScore500W;

@Service
@Transactional(value = "transactionManager2")
public class DlScore500WService extends AbstractService<DlScore500W> {
	@Resource
	private DlScore500WMapper dlScore500WMapper;

	public List<DlScore500W> findByLeagueIdAndSeasonId(Integer seasonId, Integer leagueId) {
		return dlScore500WMapper.findByLeagueIdAndSeasonId(seasonId, leagueId);
	}
}

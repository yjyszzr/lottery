package com.dl.shop.lottery.service;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao2.DlTeamRecord500WMapper;
import com.dl.shop.lottery.model.DlTeamRecord500W;

@Service
@Transactional(value = "transactionManager2")
@Slf4j
public class DlTeamRecord500WService extends AbstractService<DlTeamRecord500W> {
	@Resource
	private DlTeamRecord500WMapper dlTeamRecord500WMapper;

	public List<DlTeamRecord500W> findByTeamId(Integer teamId) {
		return dlTeamRecord500WMapper.findByTeamId(teamId);
	}

}

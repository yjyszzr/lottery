package com.dl.shop.lottery.service;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao2.DlTeamFuture500WMapper;
import com.dl.shop.lottery.model.DlTeamFuture500W;

@Service
@Transactional(value = "transactionManager2")
@Slf4j
public class DlTeamFuture500WService extends AbstractService<DlTeamFuture500W> {
	@Resource
	private DlTeamFuture500WMapper dlTeamFuture500WMapper;

	public List<DlTeamFuture500W> findByTeamId(Integer teamId) {
		return dlTeamFuture500WMapper.findByTeamId(teamId);
	}

}

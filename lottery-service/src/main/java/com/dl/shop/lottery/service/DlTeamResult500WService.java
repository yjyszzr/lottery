package com.dl.shop.lottery.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao.DlTeamResult500WMapper;
import com.dl.shop.lottery.model.DlTeamResult500W;

@Service
@Transactional
public class DlTeamResult500WService extends AbstractService<DlTeamResult500W> {
	@Resource
	private DlTeamResult500WMapper dlTeamResult500WMapper;

	public DlTeamResult500W findByTeamId(Integer teamId) {
		return dlTeamResult500WMapper.findByTeamId(teamId);
	}

}

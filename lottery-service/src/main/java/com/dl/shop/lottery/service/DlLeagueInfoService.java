package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.shop.lottery.dao2.DlLeagueInfoMapper;
import com.dl.shop.lottery.dao2.LotteryMatchMapper;
import com.dl.shop.lottery.model.DlLeagueInfo;
import com.dl.shop.lottery.model.LotteryMatch;

@Service
@Transactional(value="transactionManager2")
public class DlLeagueInfoService extends AbstractService<DlLeagueInfo> {
    @Resource
    private DlLeagueInfoMapper dlLeagueInfoMapper;

	public List<LeagueInfoDTO> getFilterConditions() {
		List<DlLeagueInfo> list = dlLeagueInfoMapper.getFilterConditions();
		if(null == list) {
			return new ArrayList<LeagueInfoDTO>(0);
		}
		return list.stream().map(info->{
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(info.getLeagueAddr());
			dto.setLeagueId(info.getLeagueId());
			dto.setLeagueName(info.getLeagueName());
			return dto;
		}).collect(Collectors.toList());
	}
}

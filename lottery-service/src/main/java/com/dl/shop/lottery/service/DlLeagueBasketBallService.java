package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.DlLeagueBasketBall;
import com.dl.shop.lottery.dao.DlLeagueBasketBallMapper;
import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.LeagueInfoDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
public class DlLeagueBasketBallService extends AbstractService<DlLeagueBasketBall> {
    @Resource
    private DlLeagueBasketBallMapper dlLeagueBasketBallMapper;
    
    
	public List<LeagueInfoDTO> getFilterConditions() {
		List<LeagueInfoDTO> filterConditions = dlLeagueBasketBallMapper.getFilterConditions();
		if(filterConditions == null) {
			filterConditions = new ArrayList<LeagueInfoDTO>(0);
		}
		return filterConditions;
	}

}

package com.dl.shop.lottery.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DLFutureMatchDTO;
import com.dl.lottery.dto.DlFutureMatchInfoDTO;
import com.dl.lottery.dto.DlLeagueMatchDTO;
import com.dl.shop.lottery.dao2.DlFutureMatchMapper;
import com.dl.shop.lottery.model.DlFutureMatch;

@Service
@Transactional(value = "transactionManager2")
public class DlFutureMatchService extends AbstractService<DlFutureMatch> {
	@Resource
	private DlFutureMatchMapper dlFutureMatchMapper;

	public List<DLFutureMatchDTO> getFutureMatchInfo(Integer teamId) {
		List<DLFutureMatchDTO> dtos = new ArrayList<DLFutureMatchDTO>(0);
		List<DlFutureMatch> list = dlFutureMatchMapper.getList(teamId);
		if (CollectionUtils.isNotEmpty(list)) {
			dtos = new ArrayList<DLFutureMatchDTO>(list.size());
			for (DlFutureMatch match : list) {
				DLFutureMatchDTO dto = new DLFutureMatchDTO();
				dto.setHomeTeamAbbr(match.getHomeTeamAbbr());
				dto.setHomeTeamId(match.getHomeTeamId());
				dto.setHomeTeamName(match.getHomeTeamName());
				dto.setLeagueAbbr(match.getLeagueAbbr());
				dto.setLeagueId(match.getLeagueId());
				dto.setLeagueName(match.getLeagueName());
				dto.setMatchDate(match.getMatchDate());
				dto.setMatchId(match.getMatchId());
				dto.setMatchTime(match.getMatchTime());
				dto.setVisitorTeamAbbr(match.getVisitorTeamAbbr());
				dto.setVisitorTeamId(match.getVisitorTeamId());
				dto.setVisitorTeamName(match.getVisitorTeamName());
				dtos.add(dto);
			}
		}
		return dtos;
	}

	public DlLeagueMatchDTO findByLeagueId(Integer leagueId) {
		DlLeagueMatchDTO leagueMatchDTO = new DlLeagueMatchDTO();
		List<DlFutureMatchInfoDTO> futureMatchDTOList = new ArrayList<DlFutureMatchInfoDTO>();
		List<DlFutureMatch> futureMatchList = dlFutureMatchMapper.findByLeagueId(leagueId);
		for (int i = 0; i < futureMatchList.size(); i++) {
			DlFutureMatchInfoDTO futureMatchInfoDTO = new DlFutureMatchInfoDTO();
			futureMatchInfoDTO.setHomeTeamAbbr(futureMatchList.get(i).getHomeTeamAbbr());
			futureMatchInfoDTO.setLeagueAbbr(futureMatchList.get(i).getLeagueAbbr());
			futureMatchInfoDTO.setMatchTime(futureMatchList.get(i).getMatchDate() + " " + futureMatchList.get(i).getMatchTime());
			futureMatchInfoDTO.setVisitorTeamAbbr(futureMatchList.get(i).getVisitorTeamAbbr());
			futureMatchDTOList.add(futureMatchInfoDTO);
		}
		leagueMatchDTO.setFutureMatchDTOList(futureMatchDTOList);
		return leagueMatchDTO;
	}

}

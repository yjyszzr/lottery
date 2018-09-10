package com.dl.shop.lottery.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DlFutureMatchInfoDTO;
import com.dl.lottery.dto.DlMatchGroupDTO;
import com.dl.lottery.dto.DlMatchGroupData500WDTO;
import com.dl.lottery.dto.DlMatchGroupData500WDTO.MatchGroupData;
import com.dl.shop.lottery.dao2.DlSeasonGroupData500WMapper;
import com.dl.shop.lottery.model.DlSeasonGroupData500W;

@Service
@Transactional(value = "transactionManager2")
public class DlSeasonGroupData500WService extends AbstractService<DlSeasonGroupData500W> {
	@Resource
	private DlSeasonGroupData500WMapper dlSeasonGroupData500WMapper;

	public DlMatchGroupData500WDTO findByLeagueIdAndSeasonId(Integer seasonId, Integer leagueId) {
		DlMatchGroupData500WDTO matchGroupData500W = new DlMatchGroupData500WDTO();
		List<DlMatchGroupDTO> MatchGroups = dlSeasonGroupData500WMapper.findMatchGroupsByLeagueIdAndSeasonId(seasonId, leagueId);
		List<DlSeasonGroupData500W> matchGroupData500WList = dlSeasonGroupData500WMapper.findByLeagueIdAndSeasonId(seasonId, leagueId);

		List<MatchGroupData> matchGroupDataList = new ArrayList<MatchGroupData>();
		for (int i = 0; i < MatchGroups.size(); i++) {
			MatchGroupData matchGroup = new MatchGroupData();
			List<DlFutureMatchInfoDTO> futureMatchDTOList = new ArrayList<DlFutureMatchInfoDTO>();
			for (int j = 0; j < matchGroupData500WList.size(); j++) {
				DlSeasonGroupData500W s = matchGroupData500WList.get(j);
				if (s.getMatchGroupId().equals(MatchGroups.get(i).getGroupId())) {
					DlFutureMatchInfoDTO futureMatchInfoDTO = new DlFutureMatchInfoDTO();
					futureMatchInfoDTO.setHomeTeamAbbr(s.getHomeTeam());
					futureMatchInfoDTO.setMatchTime(s.getMatchTime());
					futureMatchInfoDTO.setVisitorTeamAbbr(s.getVisitorTeam());
					futureMatchDTOList.add(futureMatchInfoDTO);
				}
			}
			matchGroup.setGroupName(MatchGroups.get(i).getGroupName());
			matchGroup.setFutureMatchDTOList(futureMatchDTOList);
			matchGroupDataList.add(matchGroup);
		}
		matchGroupData500W.setMatchGroupData(matchGroupDataList);
		return matchGroupData500W;
	}
}

package com.dl.shop.lottery.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DlFutureMatchInfoDTO;
import com.dl.lottery.dto.DlMatchGroupDTO;
import com.dl.lottery.dto.DlMatchGroupData500WDTO;
import com.dl.lottery.dto.DlMatchGroupData500WDTO.MatchGroupData;
import com.dl.lottery.dto.DlMatchGroupData500WDTO.MatchTurnGroupData;
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
		// 获取每个轮次
		List<MatchTurnGroupData> matchTurnGroupDataList = new ArrayList<MatchTurnGroupData>();
		for (int i = 0; i < MatchGroups.size(); i++) {
			MatchTurnGroupData matchTurnGroupData = new MatchTurnGroupData();
			List<MatchGroupData> groupDTOList = new ArrayList<MatchGroupData>();
			Map<String, List<DlFutureMatchInfoDTO>> matchMap = new HashMap<String, List<DlFutureMatchInfoDTO>>();
			for (int j = 0; j < matchGroupData500WList.size(); j++) {
				DlSeasonGroupData500W s = matchGroupData500WList.get(j);
				if (s.getMatchGroupId().equals(MatchGroups.get(i).getGroupId())) {
					DlFutureMatchInfoDTO futureMatchInfoDTO = new DlFutureMatchInfoDTO();
					futureMatchInfoDTO.setHomeTeamAbbr(s.getHomeTeam());
					futureMatchInfoDTO.setMatchTime(s.getMatchTime());
					futureMatchInfoDTO.setMatchScore(s.getMatchScore());
					futureMatchInfoDTO.setVisitorTeamAbbr(s.getVisitorTeam());
					futureMatchInfoDTO.setGroupName(s.getGroupName());
					String groupName = matchGroupData500WList.get(j).getGroupName();
					List<DlFutureMatchInfoDTO> matchs = matchMap.get(groupName);
					if (matchs == null) {
						matchs = new ArrayList<DlFutureMatchInfoDTO>(30);
						matchMap.put(groupName, matchs);
					}
					matchs.add(futureMatchInfoDTO);
				}
			}
			Set<String> stringStr = matchMap.keySet();
			for (String str : stringStr) {
				MatchGroupData matchGroupData = new MatchGroupData();
				matchGroupData.setGroupName(str);
				matchGroupData.setFutureMatchDTOList(matchMap.get(str));
				groupDTOList.add(matchGroupData);
			}
			matchTurnGroupData.setGroupDTOList(groupDTOList);
			matchTurnGroupData.setGroupType(stringStr.size() > 1 ? 1 : 0);// 是否分组0:不分组,1分组
			matchTurnGroupData.setTurnGroupName(MatchGroups.get(i).getGroupName());
			matchTurnGroupDataList.add(matchTurnGroupData);
		}
		matchGroupData500W.setMatchTurnGroupList(matchTurnGroupDataList);
		return matchGroupData500W;
	}
}

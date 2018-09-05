package com.dl.shop.lottery.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DlLeagueShooterDTO;
import com.dl.lottery.dto.DlLeagueShooterInfoDTO;
import com.dl.shop.lottery.dao2.DlLeagueShooterMapper;
import com.dl.shop.lottery.model.DlLeagueShooter;

@Service
@Transactional(value = "transactionManager2")
@Slf4j
public class DlLeagueShooterService extends AbstractService<DlLeagueShooter> {
	@Resource
	private DlLeagueShooterMapper dlLeagueShooterMapper;

	public DlLeagueShooterDTO findByLeagueId(Integer leagueId) {
		DlLeagueShooterDTO leagueShooter = new DlLeagueShooterDTO();
		List<DlLeagueShooterInfoDTO> leagueShooterDTOList = new ArrayList<DlLeagueShooterInfoDTO>();
		List<DlLeagueShooter> leagueShooterList = dlLeagueShooterMapper.findByLeagueId(leagueId);
		for (int i = 0; i < leagueShooterList.size(); i++) {
			DlLeagueShooterInfoDTO leagueShooterDTO = new DlLeagueShooterInfoDTO();
			leagueShooterDTO.setInNum(leagueShooterList.get(i).getInNum());
			leagueShooterDTO.setMatchSeasonId(leagueShooterList.get(i).getMatchSeasonId());
			leagueShooterDTO.setPlayerName(leagueShooterList.get(i).getPlayerName());
			leagueShooterDTO.setPlayerTeam(leagueShooterList.get(i).getPlayerTeam());
			leagueShooterDTO.setSort(leagueShooterList.get(i).getSort());
			leagueShooterDTOList.add(leagueShooterDTO);
		}
		leagueShooter.setLeagueShooterInfoList(leagueShooterDTOList);
		return leagueShooter;

	}

	public DlLeagueShooterDTO findBySeasonId(Integer seasonId) {
		DlLeagueShooterDTO leagueShooter = new DlLeagueShooterDTO();
		List<DlLeagueShooterInfoDTO> leagueShooterDTOList = new ArrayList<DlLeagueShooterInfoDTO>();
		List<DlLeagueShooter> leagueShooterList = dlLeagueShooterMapper.findBySeasonId(seasonId);
		for (int i = 0; i < leagueShooterList.size(); i++) {
			DlLeagueShooterInfoDTO leagueShooterDTO = new DlLeagueShooterInfoDTO();
			leagueShooterDTO.setInNum(leagueShooterList.get(i).getInNum());
			leagueShooterDTO.setMatchSeasonId(leagueShooterList.get(i).getMatchSeasonId());
			leagueShooterDTO.setPlayerName(leagueShooterList.get(i).getPlayerName());
			leagueShooterDTO.setPlayerTeam(leagueShooterList.get(i).getPlayerTeam());
			leagueShooterDTO.setSort(leagueShooterList.get(i).getSort());
			leagueShooterDTOList.add(leagueShooterDTO);
		}
		leagueShooter.setLeagueShooterInfoList(leagueShooterDTOList);
		return leagueShooter;
	}

}

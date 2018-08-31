package com.dl.shop.lottery.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DLLeagueTeamScoreDTO;
import com.dl.lottery.dto.DLLeagueTeamScoreInfoDTO;
import com.dl.lottery.dto.DlLeagueIntegralDTO;
import com.dl.shop.lottery.dao2.DlMatchTeamScoreMapper;
import com.dl.shop.lottery.model.DlMatchTeamScore;

@Service
@Transactional(value = "transactionManager2")
public class DlMatchTeamScoreService extends AbstractService<DlMatchTeamScore> {
	@Resource
	private DlMatchTeamScoreMapper dlMatchTeamScoreMapper;

	public DLLeagueTeamScoreInfoDTO getTeamScores(Integer teamId, Integer leagueId) {
		DLLeagueTeamScoreInfoDTO teamScoreInfo = new DLLeagueTeamScoreInfoDTO();
		DlMatchTeamScore teamScore = dlMatchTeamScoreMapper.getByTeamId(teamId, leagueId);
		if (teamScore != null) {
			DLLeagueTeamScoreDTO lteamScore = new DLLeagueTeamScoreDTO();// 总
			lteamScore.setTeamId(teamId);
			lteamScore.setTeamOrder(Integer.valueOf(teamScore.getTeamOrder()));
			lteamScore.setTeamName(teamScore.getTeamName());
			lteamScore.setBallClean(teamScore.getlBallClean());
			lteamScore.setBallIn(teamScore.getlBallIn());
			lteamScore.setBallLose(teamScore.getlBallLose());
			lteamScore.setMatchD(teamScore.getlMatchD());
			lteamScore.setMatchH(teamScore.getlMatchH());
			lteamScore.setMatchL(teamScore.getlMatchA());
			lteamScore.setMatchNum(teamScore.getlMatchNum());
			lteamScore.setScore(teamScore.getlScore());
			DLLeagueTeamScoreDTO hteamScore = new DLLeagueTeamScoreDTO();// 主
			hteamScore.setTeamId(teamId);
			hteamScore.setTeamOrder(Integer.valueOf(teamScore.getTeamOrder()));
			hteamScore.setTeamName(teamScore.getTeamName());
			hteamScore.setBallClean(teamScore.gethBallClean());
			hteamScore.setBallIn(teamScore.gethBallIn());
			hteamScore.setBallLose(teamScore.gethBallLose());
			hteamScore.setMatchD(teamScore.gethMatchD());
			hteamScore.setMatchH(teamScore.gethMatchH());
			hteamScore.setMatchL(teamScore.gethMatchA());
			hteamScore.setMatchNum(teamScore.gethMatchNum());
			hteamScore.setScore(teamScore.gethScore());
			DLLeagueTeamScoreDTO tteamScore = new DLLeagueTeamScoreDTO();// 客
			tteamScore.setTeamId(teamId);
			tteamScore.setTeamOrder(Integer.valueOf(teamScore.getTeamOrder()));
			tteamScore.setTeamName(teamScore.getTeamName());
			tteamScore.setBallClean(teamScore.getvBallClean());
			tteamScore.setBallIn(teamScore.getvBallIn());
			tteamScore.setBallLose(teamScore.getvBallLose());
			tteamScore.setMatchD(teamScore.getvMatchD());
			tteamScore.setMatchH(teamScore.getvMatchH());
			tteamScore.setMatchL(teamScore.getvMatchA());
			tteamScore.setMatchNum(teamScore.getvMatchNum());
			tteamScore.setScore(teamScore.getvScore());
			teamScoreInfo.setTeamId(teamId);
			teamScoreInfo.setTeamName(teamScoreInfo.getTeamName());
			teamScoreInfo.setLteamScore(lteamScore);
			teamScoreInfo.setHteamScore(hteamScore);
			teamScoreInfo.setTteamScore(tteamScore);

		}
		return teamScoreInfo;
	}

	public DlLeagueIntegralDTO getByleagueId(Integer leagueId) {
		List<DlMatchTeamScore> list = dlMatchTeamScoreMapper.getByleagueId(leagueId);
		DlLeagueIntegralDTO teamScoreInfo = new DlLeagueIntegralDTO();

		List<DLLeagueTeamScoreDTO> vteamScoreList = new ArrayList<DLLeagueTeamScoreDTO>();
		List<DLLeagueTeamScoreDTO> hteamScoreList = new ArrayList<DLLeagueTeamScoreDTO>();
		List<DLLeagueTeamScoreDTO> tteamScoreList = new ArrayList<DLLeagueTeamScoreDTO>();

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				DlMatchTeamScore teamScore = list.get(i);
				DLLeagueTeamScoreDTO tteamScore = new DLLeagueTeamScoreDTO();// 总
				tteamScore.setTeamOrder(Integer.valueOf(teamScore.getTeamOrder()));
				tteamScore.setTeamName(teamScore.getTeamName());
				tteamScore.setBallClean(teamScore.getlBallClean());
				tteamScore.setBallIn(teamScore.getlBallIn());
				tteamScore.setBallLose(teamScore.getlBallLose());
				tteamScore.setMatchD(teamScore.getlMatchD());
				tteamScore.setMatchH(teamScore.getlMatchH());
				tteamScore.setMatchL(teamScore.getlMatchA());
				tteamScore.setMatchNum(teamScore.getlMatchNum());
				tteamScore.setScore(teamScore.getlScore());
				DLLeagueTeamScoreDTO hteamScore = new DLLeagueTeamScoreDTO();// 主
				hteamScore.setTeamOrder(Integer.valueOf(teamScore.getTeamOrder()));
				hteamScore.setTeamName(teamScore.getTeamName());
				hteamScore.setBallClean(teamScore.gethBallClean());
				hteamScore.setBallIn(teamScore.gethBallIn());
				hteamScore.setBallLose(teamScore.gethBallLose());
				hteamScore.setMatchD(teamScore.gethMatchD());
				hteamScore.setMatchH(teamScore.gethMatchH());
				hteamScore.setMatchL(teamScore.gethMatchA());
				hteamScore.setMatchNum(teamScore.gethMatchNum());
				hteamScore.setScore(teamScore.gethScore());
				DLLeagueTeamScoreDTO vteamScore = new DLLeagueTeamScoreDTO();// 客
				vteamScore.setTeamOrder(Integer.valueOf(teamScore.getTeamOrder()));
				vteamScore.setTeamName(teamScore.getTeamName());
				vteamScore.setBallClean(teamScore.getvBallClean());
				vteamScore.setBallIn(teamScore.getvBallIn());
				vteamScore.setBallLose(teamScore.getvBallLose());
				vteamScore.setMatchD(teamScore.getvMatchD());
				vteamScore.setMatchH(teamScore.getvMatchH());
				vteamScore.setMatchL(teamScore.getvMatchA());
				vteamScore.setMatchNum(teamScore.getvMatchNum());
				vteamScore.setScore(teamScore.getvScore());
				vteamScoreList.add(vteamScore);
				hteamScoreList.add(hteamScore);
				tteamScoreList.add(tteamScore);
			}
			teamScoreInfo.setHteamScoreList(hteamScoreList);
			teamScoreInfo.setTteamScoreList(tteamScoreList);
			teamScoreInfo.setVteamScoreList(vteamScoreList);
		}
		return teamScoreInfo;
	}
}

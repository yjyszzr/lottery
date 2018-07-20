package com.dl.shop.lottery.service;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DLLeagueTeamScoreDTO;
import com.dl.lottery.dto.DLLeagueTeamScoreInfoDTO;
import com.dl.shop.lottery.dao2.DlMatchTeamScoreMapper;
import com.dl.shop.lottery.model.DlMatchTeamScore;

@Service
@Transactional(value="transactionManager2")
public class DlMatchTeamScoreService extends AbstractService<DlMatchTeamScore> {
    @Resource
    private DlMatchTeamScoreMapper dlMatchTeamScoreMapper;

    public DLLeagueTeamScoreInfoDTO getTeamScores(Integer teamId,Integer leagueId) {
    	DLLeagueTeamScoreInfoDTO teamScoreInfo = new DLLeagueTeamScoreInfoDTO();
    	DlMatchTeamScore teamScore = dlMatchTeamScoreMapper.getByTeamId(teamId,leagueId);
    	if(teamScore != null) {
    		DLLeagueTeamScoreDTO lteamScore = new DLLeagueTeamScoreDTO();
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
    		DLLeagueTeamScoreDTO hteamScore = new DLLeagueTeamScoreDTO();
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
    		DLLeagueTeamScoreDTO tteamScore = new DLLeagueTeamScoreDTO();
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
    		tteamScore.setScore(teamScore.getlScore());
    		teamScoreInfo.setTeamId(teamId);
    		teamScoreInfo.setTeamName(teamScoreInfo.getTeamName());
    		teamScoreInfo.setHteamScore(hteamScore);
    		teamScoreInfo.setTteamScore(tteamScore);
    		teamScoreInfo.setLteamScore(lteamScore);
    	}
    	return teamScoreInfo;
    }
}

package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DLLeagueTeamScoreDTO;
import com.dl.shop.lottery.dao.DlLeagueTeamScoreMapper;
import com.dl.shop.lottery.model.DlLeagueTeamScore;

@Service
@Transactional
public class DlLeagueTeamScoreService extends AbstractService<DlLeagueTeamScore> {
    @Resource
    private DlLeagueTeamScoreMapper dlLeagueTeamScoreMapper;

    public DLLeagueTeamScoreDTO getTeamScores(Integer teamId, int flag) {
    	DlLeagueTeamScore score = dlLeagueTeamScoreMapper.getTeamScoresByFlag(teamId, flag);
    	DLLeagueTeamScoreDTO dto = new DLLeagueTeamScoreDTO();
    	if(null != score) {
			dto.setBallIn(score.getBallIn());
			dto.setBallLose(score.getBallLose());
			dto.setFlag(score.getFlag());
			dto.setMatchD(score.getMatchD());
			dto.setMatchH(score.getMatchH());
			dto.setMatchL(score.getMatchL());
			dto.setMatchNum(score.getMatchNum());
			dto.setPreH(score.getPreH());
			dto.setPreL(score.getPreL());
			dto.setRatioD(score.getRatioD());
			dto.setRatioH(score.getRatioH());
			dto.setRatioL(score.getRatioL());
			dto.setScore(score.getScore());
			dto.setTeamId(score.getTeamId());
			dto.setTeamName(score.getTeamName());
			dto.setBallClean(score.getBallClean());
			dto.setRank(score.getRank());
    	}
    	return dto;
    }
}

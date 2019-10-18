package com.dl.shop.lottery.web;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.MatchLineUpInfosDTO;
import com.dl.lottery.param.GetMatchLineUpInfoParam;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.service.DlMatchLineUpsService;
import com.dl.shop.lottery.service.LotteryMatchService;

import io.swagger.annotations.ApiOperation;

/**
* Created by CodeGenerator on 2018/07/06.
*/
@RestController
@RequestMapping("/match/lineup")
public class DlMatchLineUpsController {
    @Resource
    private DlMatchLineUpsService dlMatchLineUpsService;
    @Resource
    private LotteryMatchService lotteryMatchService;


    @ApiOperation(value = "比赛阵容", notes = "比赛阵容")
    @PostMapping("/info")
    public BaseResult<MatchLineUpInfosDTO> matchLineUpInfo(@RequestBody GetMatchLineUpInfoParam param) {
    	LotteryMatch lotteryMatch = lotteryMatchService.findById(param.getMatchId());
		if(null == lotteryMatch) {
			return ResultGenerator.genFailResult("数据读取失败！", null);
		}
		Integer changciId = lotteryMatch.getChangciId();
		MatchLineUpInfosDTO matchLineUpInfosDTO = dlMatchLineUpsService.getMatchLineUps(changciId);
		Date matchTimeDate = lotteryMatch.getMatchTime();
		Instant instant = matchTimeDate.toInstant();
		int matchTime = Long.valueOf(instant.getEpochSecond()).intValue();
		matchLineUpInfosDTO.setMatchTime(matchTime);
		String changci = lotteryMatch.getChangci();
		matchLineUpInfosDTO.setChangci(changci);
		String leagueAddr = lotteryMatch.getLeagueAddr();
		matchLineUpInfosDTO.setLeagueAddr(leagueAddr);
		String homeTeamAbbr = lotteryMatch.getHomeTeamAbbr();
		matchLineUpInfosDTO.setHomeTeamAbbr(homeTeamAbbr);
		String visitingTeamAbbr = lotteryMatch.getVisitingTeamAbbr();
		matchLineUpInfosDTO.setVisitingTeamAbbr(visitingTeamAbbr);
    	return ResultGenerator.genSuccessResult(null, matchLineUpInfosDTO);
    }

}

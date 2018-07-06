package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.MatchLineUpInfosDTO;
import com.dl.lottery.dto.MatchLiveInfoDTO;
import com.dl.lottery.param.GetMatchLineUpInfoParam;
import com.dl.lottery.param.GetMatchLiveInfoParam;
import com.dl.shop.lottery.model.DlMatchLive;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.service.DlMatchLiveService;
import com.dl.shop.lottery.service.LotteryMatchService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
* Created by CodeGenerator on 2018/07/06.
*/
@RestController
@RequestMapping("/match/live")
public class DlMatchLiveController {
    @Resource
    private DlMatchLiveService dlMatchLiveService;
    @Resource
    private LotteryMatchService lotteryMatchService;


    @ApiOperation(value = "比赛赛况", notes = "比赛赛况")
    @PostMapping("/info")
    public BaseResult<MatchLiveInfoDTO> matchLiveInfo(@RequestBody GetMatchLiveInfoParam param) {
    	LotteryMatch lotteryMatch = lotteryMatchService.findById(param.getMatchId());
		if(null == lotteryMatch) {
			return ResultGenerator.genFailResult("数据读取失败！", null);
		}
		Integer changciId = lotteryMatch.getChangciId();
		MatchLiveInfoDTO matchLiveInfo = dlMatchLiveService.getMatchLiveInfo(changciId);
		Date matchTimeDate = lotteryMatch.getMatchTime();
		Instant instant = matchTimeDate.toInstant();
		int matchTime = Long.valueOf(instant.getEpochSecond()).intValue();
		matchLiveInfo.setMatchTime(matchTime);
		String changci = lotteryMatch.getChangci();
		matchLiveInfo.setChangci(changci);
		String leagueAddr = lotteryMatch.getLeagueAddr();
		matchLiveInfo.setLeagueAddr(leagueAddr);
		String homeTeamAbbr = lotteryMatch.getHomeTeamAbbr();
		matchLiveInfo.setHomeTeamAbbr(homeTeamAbbr);
		String visitingTeamAbbr = lotteryMatch.getVisitingTeamAbbr();
		matchLiveInfo.setVisitingTeamAbbr(visitingTeamAbbr);
    	return ResultGenerator.genSuccessResult(null, matchLiveInfo);
    }
}

package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlLeagueTeamScore;
import com.dl.shop.lottery.service.DlLeagueTeamScoreService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/17.
*/
@RestController
@RequestMapping("/dl/league/team/score")
public class DlLeagueTeamScoreController {
    @Resource
    private DlLeagueTeamScoreService dlLeagueTeamScoreService;

}

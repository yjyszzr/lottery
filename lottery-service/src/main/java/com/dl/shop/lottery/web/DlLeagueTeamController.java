package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.param.RefreshMatchParam;
import com.dl.shop.lottery.model.DlLeagueInfo;
import com.dl.shop.lottery.model.DlLeagueTeam;
import com.dl.shop.lottery.service.DlLeagueInfoService;
import com.dl.shop.lottery.service.DlLeagueTeamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Condition;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/15.
*/
@RestController
@RequestMapping("/dl/league/team")
public class DlLeagueTeamController {
    @Resource
    private DlLeagueTeamService dlLeagueTeamService;

    @Resource
    private DlLeagueInfoService leagueInfoService;
  /*  
    @PostMapping("/refresh")
    public BaseResult add() {
    	leagueInfoService.aa();
//    	List<DlLeagueInfo> getall = leagueInfoService.getall1();
    	while(getall.size() > 0) {
    		int end = getall.size() > 100?100:getall.size();
    		List<DlLeagueInfo> subList = getall.subList(0, end);
    		leagueInfoService.refreshLeague(subList);
    		getall.removeAll(subList);
    	}
        return ResultGenerator.genSuccessResult();
    }*/
   /* @ApiOperation(value = "刷新拉取球队信息", notes = "刷新拉取球队信息")
    @PostMapping("/refresh")
    public BaseResult add() {
    	for(int i=1; i< 43; i++) {
    		Integer contryId = i;
    		Condition condition = new Condition(DlLeagueInfo.class);
    		condition.and().andEqualTo("contryId", contryId);
    		List<DlLeagueInfo> leagueInfos = leagueInfoService.findByCondition(condition);
    		for(DlLeagueInfo leagueInfo: leagueInfos) {
    			dlLeagueTeamService.refreshMatchTeams(leagueInfo);
    		}
    	}
        return ResultGenerator.genSuccessResult();
    }*/

   /* @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlLeagueTeamService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlLeagueTeam dlLeagueTeam) {
        dlLeagueTeamService.update(dlLeagueTeam);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlLeagueTeam dlLeagueTeam = dlLeagueTeamService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlLeagueTeam);
    }*/

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlLeagueTeam> list = dlLeagueTeamService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

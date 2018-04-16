package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlLeagueTeam;
import com.dl.shop.lottery.service.DlLeagueTeamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/add")
    public BaseResult add(DlLeagueTeam dlLeagueTeam) {
        dlLeagueTeamService.save(dlLeagueTeam);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
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
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlLeagueTeam> list = dlLeagueTeamService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

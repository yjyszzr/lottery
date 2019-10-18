package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlMatchTeamScore;
import com.dl.shop.lottery.service.DlMatchTeamScoreService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/07/05.
*/
@RestController
@RequestMapping("/dl/match/team/score")
public class DlMatchTeamScoreController {
    @Resource
    private DlMatchTeamScoreService dlMatchTeamScoreService;

    @PostMapping("/add")
    public BaseResult add(DlMatchTeamScore dlMatchTeamScore) {
        dlMatchTeamScoreService.save(dlMatchTeamScore);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlMatchTeamScoreService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlMatchTeamScore dlMatchTeamScore) {
        dlMatchTeamScoreService.update(dlMatchTeamScore);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlMatchTeamScore dlMatchTeamScore = dlMatchTeamScoreService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlMatchTeamScore);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlMatchTeamScore> list = dlMatchTeamScoreService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

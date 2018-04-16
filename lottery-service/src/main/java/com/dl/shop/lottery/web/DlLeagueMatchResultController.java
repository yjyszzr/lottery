package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.param.RefreshMatchParam;
import com.dl.shop.lottery.model.DlLeagueMatchResult;
import com.dl.shop.lottery.service.DlLeagueMatchResultService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;

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
@RequestMapping("/dl/league/match/result")
public class DlLeagueMatchResultController {
    @Resource
    private DlLeagueMatchResultService dlLeagueMatchResultService;

    @ApiOperation(value = "刷新拉取赛事结果", notes = "刷新拉取赛事结果")
    @PostMapping("/refresh")
    public BaseResult add(@RequestBody RefreshMatchParam param) {
        dlLeagueMatchResultService.refreshMatchResultFromZC(param.getChangciId());
        return ResultGenerator.genSuccessResult();
    }

   /* @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlLeagueMatchResultService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlLeagueMatchResult dlLeagueMatchResult) {
        dlLeagueMatchResultService.update(dlLeagueMatchResult);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlLeagueMatchResult dlLeagueMatchResult = dlLeagueMatchResultService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlLeagueMatchResult);
    }
*/
    /*@PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlLeagueMatchResult> list = dlLeagueMatchResultService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }*/
}

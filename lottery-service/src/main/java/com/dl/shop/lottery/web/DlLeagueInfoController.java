package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlLeagueInfo;
import com.dl.shop.lottery.service.DlLeagueInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/03.
*/
@RestController
@RequestMapping("/dl/league/info")
public class DlLeagueInfoController {
    @Resource
    private DlLeagueInfoService dlLeagueInfoService;

    @PostMapping("/add")
    public BaseResult add(DlLeagueInfo dlLeagueInfo) {
        dlLeagueInfoService.save(dlLeagueInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlLeagueInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlLeagueInfo dlLeagueInfo) {
        dlLeagueInfoService.update(dlLeagueInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlLeagueInfo dlLeagueInfo = dlLeagueInfoService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlLeagueInfo);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlLeagueInfo> list = dlLeagueInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlLeagueGroup;
import com.dl.shop.lottery.service.DlLeagueGroupService;
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
@RequestMapping("/dl/league/group")
public class DlLeagueGroupController {
    @Resource
    private DlLeagueGroupService dlLeagueGroupService;

    @PostMapping("/add")
    public BaseResult add(DlLeagueGroup dlLeagueGroup) {
        dlLeagueGroupService.save(dlLeagueGroup);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlLeagueGroupService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlLeagueGroup dlLeagueGroup) {
        dlLeagueGroupService.update(dlLeagueGroup);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlLeagueGroup dlLeagueGroup = dlLeagueGroupService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlLeagueGroup);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlLeagueGroup> list = dlLeagueGroupService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

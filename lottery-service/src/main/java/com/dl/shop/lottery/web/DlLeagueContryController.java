package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlLeagueContry;
import com.dl.shop.lottery.service.DlLeagueContryService;
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
@RequestMapping("/dl/league/contry")
public class DlLeagueContryController {
    @Resource
    private DlLeagueContryService dlLeagueContryService;

    @PostMapping("/add")
    public BaseResult add(DlLeagueContry dlLeagueContry) {
        dlLeagueContryService.save(dlLeagueContry);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlLeagueContryService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlLeagueContry dlLeagueContry) {
        dlLeagueContryService.update(dlLeagueContry);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlLeagueContry dlLeagueContry = dlLeagueContryService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlLeagueContry);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlLeagueContry> list = dlLeagueContryService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

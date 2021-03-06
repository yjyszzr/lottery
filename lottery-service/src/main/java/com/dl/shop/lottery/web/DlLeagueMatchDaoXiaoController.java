package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlLeagueMatchDaoXiao;
import com.dl.shop.lottery.service.DlLeagueMatchDaoXiaoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/19.
*/
@RestController
@RequestMapping("/dl/league/match/dao/xiao")
public class DlLeagueMatchDaoXiaoController {
    @Resource
    private DlLeagueMatchDaoXiaoService dlLeagueMatchDaoXiaoService;

   /* @PostMapping("/add")
    public BaseResult add(DlLeagueMatchDaoXiao dlLeagueMatchDaoXiao) {
        dlLeagueMatchDaoXiaoService.save(dlLeagueMatchDaoXiao);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlLeagueMatchDaoXiaoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlLeagueMatchDaoXiao dlLeagueMatchDaoXiao) {
        dlLeagueMatchDaoXiaoService.update(dlLeagueMatchDaoXiao);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlLeagueMatchDaoXiao dlLeagueMatchDaoXiao = dlLeagueMatchDaoXiaoService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlLeagueMatchDaoXiao);
    }*/

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlLeagueMatchDaoXiao> list = dlLeagueMatchDaoXiaoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlTeamResult500W;
import com.dl.shop.lottery.service.DlTeamResult500WService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/09/06.
*/
@RestController
@RequestMapping("/dl/team/result500/w")
public class DlTeamResult500WController {
    @Resource
    private DlTeamResult500WService dlTeamResult500WService;

    @PostMapping("/add")
    public BaseResult add(DlTeamResult500W dlTeamResult500W) {
        dlTeamResult500WService.save(dlTeamResult500W);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlTeamResult500WService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlTeamResult500W dlTeamResult500W) {
        dlTeamResult500WService.update(dlTeamResult500W);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlTeamResult500W dlTeamResult500W = dlTeamResult500WService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlTeamResult500W);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlTeamResult500W> list = dlTeamResult500WService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

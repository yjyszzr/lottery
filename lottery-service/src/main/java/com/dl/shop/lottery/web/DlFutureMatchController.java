package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlFutureMatch;
import com.dl.shop.lottery.service.DlFutureMatchService;
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
@RequestMapping("/dl/future/match")
public class DlFutureMatchController {
    @Resource
    private DlFutureMatchService dlFutureMatchService;

    @PostMapping("/add")
    public BaseResult add(DlFutureMatch dlFutureMatch) {
        dlFutureMatchService.save(dlFutureMatch);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlFutureMatchService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlFutureMatch dlFutureMatch) {
        dlFutureMatchService.update(dlFutureMatch);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlFutureMatch dlFutureMatch = dlFutureMatchService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlFutureMatch);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlFutureMatch> list = dlFutureMatchService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

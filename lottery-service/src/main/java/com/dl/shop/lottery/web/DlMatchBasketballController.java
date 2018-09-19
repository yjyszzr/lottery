package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlMatchBasketball;
import com.dl.shop.lottery.service.DlMatchBasketballService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/09/18.
*/
@RestController
@RequestMapping("/dl/match/basketball")
public class DlMatchBasketballController {
    @Resource
    private DlMatchBasketballService dlMatchBasketballService;

    @PostMapping("/add")
    public BaseResult add(DlMatchBasketball dlMatchBasketball) {
        dlMatchBasketballService.save(dlMatchBasketball);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlMatchBasketballService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlMatchBasketball dlMatchBasketball) {
        dlMatchBasketballService.update(dlMatchBasketball);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlMatchBasketball dlMatchBasketball = dlMatchBasketballService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlMatchBasketball);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlMatchBasketball> list = dlMatchBasketballService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

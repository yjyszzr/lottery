package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlMatchPlayBasketball;
import com.dl.shop.lottery.service.DlMatchPlayBasketballService;
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
@RequestMapping("/dl/match/play/basketball")
public class DlMatchPlayBasketballController {
    @Resource
    private DlMatchPlayBasketballService dlMatchPlayBasketballService;

    @PostMapping("/add")
    public BaseResult add(DlMatchPlayBasketball dlMatchPlayBasketball) {
        dlMatchPlayBasketballService.save(dlMatchPlayBasketball);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlMatchPlayBasketballService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlMatchPlayBasketball dlMatchPlayBasketball) {
        dlMatchPlayBasketballService.update(dlMatchPlayBasketball);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlMatchPlayBasketball dlMatchPlayBasketball = dlMatchPlayBasketballService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlMatchPlayBasketball);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlMatchPlayBasketball> list = dlMatchPlayBasketballService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

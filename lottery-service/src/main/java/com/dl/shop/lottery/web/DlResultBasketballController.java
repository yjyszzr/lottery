package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlResultBasketball;
import com.dl.shop.lottery.service.DlResultBasketballService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/09/30.
*/
@RestController
@RequestMapping("/dl/result/basketball")
public class DlResultBasketballController {
    @Resource
    private DlResultBasketballService dlResultBasketballService;

    @PostMapping("/add")
    public BaseResult add(DlResultBasketball dlResultBasketball) {
        dlResultBasketballService.save(dlResultBasketball);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlResultBasketballService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlResultBasketball dlResultBasketball) {
        dlResultBasketballService.update(dlResultBasketball);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlResultBasketball dlResultBasketball = dlResultBasketballService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlResultBasketball);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlResultBasketball> list = dlResultBasketballService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

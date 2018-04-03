package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.PeriodRewardDetail;
import com.dl.shop.lottery.service.PeriodRewardDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/02.
*/
@RestController
@RequestMapping("/period/reward/detail")
public class PeriodRewardDetailController {
    @Resource
    private PeriodRewardDetailService periodRewardDetailService;

    @PostMapping("/add")
    public BaseResult add(PeriodRewardDetail periodRewardDetail) {
        periodRewardDetailService.save(periodRewardDetail);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        periodRewardDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(PeriodRewardDetail periodRewardDetail) {
        periodRewardDetailService.update(periodRewardDetail);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        PeriodRewardDetail periodRewardDetail = periodRewardDetailService.findById(id);
        return ResultGenerator.genSuccessResult(null,periodRewardDetail);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<PeriodRewardDetail> list = periodRewardDetailService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

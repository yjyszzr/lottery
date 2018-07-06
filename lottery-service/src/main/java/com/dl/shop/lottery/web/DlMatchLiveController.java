package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlMatchLive;
import com.dl.shop.lottery.service.DlMatchLiveService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/07/06.
*/
@RestController
@RequestMapping("/dl/match/live")
public class DlMatchLiveController {
    @Resource
    private DlMatchLiveService dlMatchLiveService;

    @PostMapping("/add")
    public BaseResult add(DlMatchLive dlMatchLive) {
        dlMatchLiveService.save(dlMatchLive);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlMatchLiveService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlMatchLive dlMatchLive) {
        dlMatchLiveService.update(dlMatchLive);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlMatchLive dlMatchLive = dlMatchLiveService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlMatchLive);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlMatchLive> list = dlMatchLiveService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

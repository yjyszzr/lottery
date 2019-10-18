package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.param.RefreshMatchParam;
import com.dl.shop.lottery.model.DlMatchSupport;
import com.dl.shop.lottery.service.DlMatchSupportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/20.
*/
@RestController
@RequestMapping("/dl/match/support")
public class DlMatchSupportController {
    @Resource
    private DlMatchSupportService dlMatchSupportService;

    @ApiOperation(value = "刷新信息", notes = "刷新拉取信息")
    @PostMapping("/refresh")
    public BaseResult add() {
    	dlMatchSupportService.refreshMatchSupports();
        return ResultGenerator.genSuccessResult();
    }
    
   /* @PostMapping("/add")
    public BaseResult add(DlMatchSupport dlMatchSupport) {
        dlMatchSupportService.save(dlMatchSupport);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlMatchSupportService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlMatchSupport dlMatchSupport) {
        dlMatchSupportService.update(dlMatchSupport);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlMatchSupport dlMatchSupport = dlMatchSupportService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlMatchSupport);
    }*/

   /* @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlMatchSupport> list = dlMatchSupportService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }*/
}

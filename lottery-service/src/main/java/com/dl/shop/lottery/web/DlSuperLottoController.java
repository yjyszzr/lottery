package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.DlSuperLotto;
import com.dl.shop.lottery.service.DlSuperLottoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/08/30.
*/
@RestController
@RequestMapping("/dl/super/lotto")
public class DlSuperLottoController {
    @Resource
    private DlSuperLottoService dlSuperLottoService;

    @PostMapping("/add")
    public BaseResult add(DlSuperLotto dlSuperLotto) {
        dlSuperLottoService.save(dlSuperLotto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlSuperLottoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlSuperLotto dlSuperLotto) {
        dlSuperLottoService.update(dlSuperLotto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlSuperLotto dlSuperLotto = dlSuperLottoService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlSuperLotto);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlSuperLotto> list = dlSuperLottoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

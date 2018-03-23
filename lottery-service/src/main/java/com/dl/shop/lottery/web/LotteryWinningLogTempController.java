package com.dl.shop.lottery.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.lottery.model.LotteryWinningLogTemp;
import com.dl.shop.lottery.service.LotteryWinningLogTempService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/03/23.
*/
@RestController
@RequestMapping("/lottery/winning/log/temp")
public class LotteryWinningLogTempController {
    @Resource
    private LotteryWinningLogTempService lotteryWinningLogTempService;

    @PostMapping("/add")
    public BaseResult add(LotteryWinningLogTemp lotteryWinningLogTemp) {
        lotteryWinningLogTempService.save(lotteryWinningLogTemp);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        lotteryWinningLogTempService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(LotteryWinningLogTemp lotteryWinningLogTemp) {
        lotteryWinningLogTempService.update(lotteryWinningLogTemp);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        LotteryWinningLogTemp lotteryWinningLogTemp = lotteryWinningLogTempService.findById(id);
        return ResultGenerator.genSuccessResult(null,lotteryWinningLogTemp);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<LotteryWinningLogTemp> list = lotteryWinningLogTempService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}

package com.dl.shop.lottery.web;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.LeagueMatchEuropeDTO;
import com.dl.lottery.param.ListMatchInfoParam;
import com.dl.lottery.param.RefreshMatchParam;
import com.dl.shop.lottery.service.DlLeagueMatchEuropeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;

/**
* Created by CodeGenerator on 2018/04/17.
*/
@RestController
@RequestMapping("/dl/league/match/europe")
public class DlLeagueMatchEuropeController {
    @Resource
    private DlLeagueMatchEuropeService dlLeagueMatchEuropeService;

    @ApiOperation(value = "刷新拉取赛事欧赔信息", notes = "刷新拉取赛事欧赔信息")
    @PostMapping("/refresh")
    public BaseResult add(@RequestBody RefreshMatchParam param) {
    	dlLeagueMatchEuropeService.refreshMatchEuropeInfoFromZC(param.getChangciId());
        return ResultGenerator.genSuccessResult();
    }

    /*@PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlLeagueMatchEuropeService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlLeagueMatchEurope dlLeagueMatchEurope) {
        dlLeagueMatchEuropeService.update(dlLeagueMatchEurope);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlLeagueMatchEurope dlLeagueMatchEurope = dlLeagueMatchEuropeService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlLeagueMatchEurope);
    }
*/
    @ApiOperation(value = "赛事欧赔信息", notes = "欧赔信息")
    @PostMapping("/list")
    public BaseResult<PageInfo<LeagueMatchEuropeDTO>> list(@RequestBody ListMatchInfoParam param) {
    	Integer page = param.getPage();
    	page = null == page?1:page;
    	Integer size = param.getSize();
    	size = null == size?10:size;
        PageHelper.startPage(page, size);
        List<LeagueMatchEuropeDTO> leagueMatchEuropes = dlLeagueMatchEuropeService.leagueMatchEuropes(param.getChangciId());
        PageInfo<LeagueMatchEuropeDTO> pageInfo = new PageInfo<LeagueMatchEuropeDTO>(leagueMatchEuropes);
        return ResultGenerator.genSuccessResult("success",pageInfo);
    }
}

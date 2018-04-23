package com.dl.shop.lottery.web;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.LeagueMatchAsiaDTO;
import com.dl.lottery.param.ListMatchInfoParam;
import com.dl.lottery.param.RefreshMatchParam;
import com.dl.shop.lottery.service.DlLeagueMatchAsiaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;

/**
* Created by CodeGenerator on 2018/04/15.
*/
@RestController
@RequestMapping("/dl/league/match/asia")
public class DlLeagueMatchAsiaController {
    @Resource
    private DlLeagueMatchAsiaService dlLeagueMatchAsiaService;

    @ApiOperation(value = "刷新拉取赛事亚盘信息", notes = "刷新拉取赛事亚盘信息")
    @PostMapping("/refresh")
    public BaseResult add(@RequestBody RefreshMatchParam param) {
        dlLeagueMatchAsiaService.refreshMatchAsiaInfoFromZC(param.getChangciId());
        return ResultGenerator.genSuccessResult();
    }

   /* @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlLeagueMatchAsiaService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlLeagueMatchAsia dlLeagueMatchAsia) {
        dlLeagueMatchAsiaService.update(dlLeagueMatchAsia);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlLeagueMatchAsia dlLeagueMatchAsia = dlLeagueMatchAsiaService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlLeagueMatchAsia);
    }*/

    @ApiOperation(value = "赛事亚盘信息", notes = "亚盘信息")
    @PostMapping("/list")
    public BaseResult<PageInfo<LeagueMatchAsiaDTO>> list(@RequestBody ListMatchInfoParam param) {
    	Integer page = param.getPage();
    	page = null == page?1:page;
    	Integer size = param.getSize();
    	size = null == size?10:size;
        PageHelper.startPage(page, size);
        List<LeagueMatchAsiaDTO> leagueMatchAsias = dlLeagueMatchAsiaService.leagueMatchAsias(param.getChangciId());
        PageInfo<LeagueMatchAsiaDTO> pageInfo = new PageInfo<LeagueMatchAsiaDTO>(leagueMatchAsias);
        return ResultGenerator.genSuccessResult("success",pageInfo);
    }
}

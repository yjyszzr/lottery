package com.dl.shop.lottery.web;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.LeagueMatchAsiaDTO;
import com.dl.lottery.param.ListMatchAsiaInfoParam;
import com.dl.lottery.param.RefreshMatchParam;
import com.dl.shop.lottery.model.DlLeagueMatchAsia;
import com.dl.shop.lottery.service.DlLeagueMatchAsiaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;

/**
* Created by CodeGenerator on 2018/04/15.
*/
@RestController
@RequestMapping("/dl/league/match/asia")
public class DlLeagueMatchAsiaController {
    @Resource
    private DlLeagueMatchAsiaService dlLeagueMatchAsiaService;

    @PostMapping("/refresh")
    public BaseResult add(@RequestBody RefreshMatchParam param) {
        dlLeagueMatchAsiaService.refreshMatchAsiaInfoFromZC(param.getMatchId());
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

    @PostMapping("/list")
    public BaseResult<PageInfo<LeagueMatchAsiaDTO>> list(@RequestBody ListMatchAsiaInfoParam param) {
    	Integer page = param.getPage();
    	page = null == page?1:page;
    	Integer size = param.getSize();
    	size = null == size?10:size;
        PageHelper.startPage(page, size);
        Condition condition = new Condition(DlLeagueMatchAsia.class);
        condition.and().andEqualTo("matchId", param.getMatchId());
        List<DlLeagueMatchAsia> list = dlLeagueMatchAsiaService.findByCondition(condition);
        List<LeagueMatchAsiaDTO> dtos = new ArrayList<LeagueMatchAsiaDTO>(0);
        if(null != list) {
        	dtos = new ArrayList<LeagueMatchAsiaDTO>(list.size());
        	for(DlLeagueMatchAsia asia: list) {
        		LeagueMatchAsiaDTO dto = new LeagueMatchAsiaDTO();
        		dto.setAsiaId(asia.getAsiaId());
        		dto.setComName(asia.getComName());
        		dto.setIndexA(asia.getIndexA());
        		dto.setIndexH(asia.getIndexH());
        		dto.setInitOdds1(asia.getInitOdds1());
        		dto.setInitOdds2(asia.getInitOdds2());
        		dto.setInitRule(asia.getInitRule());
        		dto.setChangciId(asia.getChangciId());
        		dto.setOdds1Change(asia.getOdds1Change());
        		dto.setOdds2Change(asia.getOdds2Change());
        		dto.setRatioA(asia.getRatioA());
        		dto.setRatioH(asia.getRatioH());
        		dto.setRealOdds1(asia.getRealOdds1());
        		dto.setRealOdds2(asia.getRealOdds2());
        		dto.setRealRule(asia.getRealRule());
        		dto.setTimeMinus(asia.getTimeMinus());
        		dtos.add(dto);
        	}
        }
        PageInfo<LeagueMatchAsiaDTO> pageInfo = new PageInfo<LeagueMatchAsiaDTO>(dtos);
        return ResultGenerator.genSuccessResult("success",pageInfo);
    }
}

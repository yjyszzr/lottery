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
import com.dl.lottery.dto.MatchResultDTO;
import com.dl.lottery.param.QueryMatchResultByPlayCodeParam;
import com.dl.lottery.param.QueryMatchResultsByPlayCodesParam;
import com.dl.lottery.param.RefreshMatchParam;
import com.dl.shop.lottery.model.DlLeagueMatchResult;
import com.dl.shop.lottery.service.DlLeagueMatchResultService;

import io.swagger.annotations.ApiOperation;

/**
* Created by CodeGenerator on 2018/04/15.
*/
@RestController
@RequestMapping("/dl/league/match/result")
public class DlLeagueMatchResultController {
    @Resource
    private DlLeagueMatchResultService dlLeagueMatchResultService;

    @ApiOperation(value = "刷新拉取赛事结果", notes = "刷新拉取赛事结果")
    @PostMapping("/refresh")
    public BaseResult add(@RequestBody RefreshMatchParam param) {
        dlLeagueMatchResultService.refreshMatchResultFromZC(param.getChangciId());
        return ResultGenerator.genSuccessResult();
    }
    
    @ApiOperation(value = "取赛事结果", notes = "取赛事结果")
    @PostMapping("/queryMatchResultByPlayCode")
    public BaseResult<List<MatchResultDTO>> queryMatchResultByPlayCode(@RequestBody QueryMatchResultByPlayCodeParam param) {
    	List<DlLeagueMatchResult> queryMatchResultByPlayCode = dlLeagueMatchResultService.queryMatchResultByPlayCode(param.getPlayCode());
    	List<MatchResultDTO> rst = new ArrayList<MatchResultDTO>(0);
    	if(queryMatchResultByPlayCode != null) {
    		rst = new ArrayList<MatchResultDTO>(queryMatchResultByPlayCode.size());
    		for(DlLeagueMatchResult dto: queryMatchResultByPlayCode) {
    			MatchResultDTO resultDTO = new MatchResultDTO();
    			resultDTO.setCellCode(dto.getCellCode());
    			resultDTO.setCellName(dto.getCellName());
    			resultDTO.setChangciId(dto.getChangciId());
    			resultDTO.setGoalline(dto.getGoalline());
    			resultDTO.setOdds(dto.getOdds());
    			resultDTO.setPlayCode(dto.getPlayCode());
    			resultDTO.setPlayType(dto.getPlayType());
    			resultDTO.setSingle(dto.getSingle());
    			rst.add(resultDTO);
    		}
    	}
    	return ResultGenerator.genSuccessResult("success", rst);
    }
    
    @ApiOperation(value = "取赛事结果", notes = "取赛事结果")
    @PostMapping("/queryMatchResultsByPlayCodes")
    public BaseResult<List<MatchResultDTO>> queryMatchResultsByPlayCodes(@RequestBody QueryMatchResultsByPlayCodesParam param) {
    	List<DlLeagueMatchResult> queryMatchResultByPlayCode = dlLeagueMatchResultService.queryMatchResultsByPlayCodes(param.getPlayCodes());
    	List<MatchResultDTO> rst = new ArrayList<MatchResultDTO>(0);
    	if(queryMatchResultByPlayCode != null) {
    		rst = new ArrayList<MatchResultDTO>(queryMatchResultByPlayCode.size());
    		for(DlLeagueMatchResult dto: queryMatchResultByPlayCode) {
    			MatchResultDTO resultDTO = new MatchResultDTO();
    			resultDTO.setCellCode(dto.getCellCode());
    			resultDTO.setCellName(dto.getCellName());
    			resultDTO.setChangciId(dto.getChangciId());
    			resultDTO.setGoalline(dto.getGoalline());
    			resultDTO.setOdds(dto.getOdds());
    			resultDTO.setPlayCode(dto.getPlayCode());
    			resultDTO.setPlayType(dto.getPlayType());
    			resultDTO.setSingle(dto.getSingle());
    			rst.add(resultDTO);
    		}
    	}
    	return ResultGenerator.genSuccessResult("success", rst);
    }

   /* @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlLeagueMatchResultService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlLeagueMatchResult dlLeagueMatchResult) {
        dlLeagueMatchResultService.update(dlLeagueMatchResult);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlLeagueMatchResult dlLeagueMatchResult = dlLeagueMatchResultService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlLeagueMatchResult);
    }
*/
    /*@PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlLeagueMatchResult> list = dlLeagueMatchResultService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }*/
}

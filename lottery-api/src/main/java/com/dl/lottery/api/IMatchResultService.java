package com.dl.lottery.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.MatchResultDTO;
import com.dl.lottery.param.QueryMatchResultByPlayCodeParam;

@FeignClient(value="lottery-service")
public interface IMatchResultService {

	@RequestMapping(path="/dl/league/match/result/queryMatchResultByPlayCode", method=RequestMethod.POST)
    public BaseResult<List<MatchResultDTO>> queryMatchResultByPlayCode(@RequestBody QueryMatchResultByPlayCodeParam param);
    
}
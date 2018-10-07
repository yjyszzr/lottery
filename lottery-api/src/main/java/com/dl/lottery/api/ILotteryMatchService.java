package com.dl.lottery.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.DLBetLottoInfoDTO;
import com.dl.lottery.dto.DLLQBetInfoDTO;
import com.dl.lottery.dto.DLZQBetInfoDTO;
import com.dl.lottery.param.DlJcZqMatchBetParam;
import com.dl.lottery.param.GetBetInfoByOrderSn;
import com.dl.lottery.param.GetCancelMatchesParam;

@FeignClient(value="lottery-service")
public interface ILotteryMatchService {

	@RequestMapping(path="/lottery/match/getBetInfo", method=RequestMethod.POST)
	public BaseResult<DLZQBetInfoDTO> getBetInfo(DlJcZqMatchBetParam param);
	
	@RequestMapping(path="/lottery/match/getBetInfoByOrderSn", method=RequestMethod.POST)
	public BaseResult<DLZQBetInfoDTO> getBetInfoByOrderSn(@RequestBody GetBetInfoByOrderSn param);
	
	@RequestMapping(path="/lottery/match/getBetInfoByOrderSn", method=RequestMethod.POST)
	public BaseResult<DLLQBetInfoDTO> getBasketBallBetInfoByOrderSn(@RequestBody GetBetInfoByOrderSn param);
	
	@RequestMapping(path="/lottery/match/getBetInfoByLotto", method=RequestMethod.POST)
	public BaseResult<List<DLBetLottoInfoDTO>> getBetInfoByLotto(@RequestBody GetBetInfoByOrderSn param);
	
	@PostMapping("/lottery/match/getCancelMatches")
	public BaseResult<List<String>> getCancelMatches(GetCancelMatchesParam param);
}

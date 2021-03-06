package com.dl.lottery.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.DLBetLottoInfoDTO;
import com.dl.lottery.dto.DLLQBetInfoDTO;
import com.dl.lottery.dto.DLZQBetInfoDTO;
import com.dl.lottery.dto.DlJcZqMatchListDTO;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.lottery.dto.OrderIdDTO;
import com.dl.lottery.param.DlJcZqMatchBetParam;
import com.dl.lottery.param.DlJcZqMatchListParam;
import com.dl.lottery.param.DlPlayCodeParam;
import com.dl.lottery.param.GetBetInfoByOrderSn;
import com.dl.lottery.param.GetCancelMatchesParam;
import com.dl.lottery.param.IsHideParam;
import com.dl.lottery.param.MatchTimePream;

@FeignClient(value="lottery-service")
public interface ILotteryMatchService {

	@RequestMapping(path="/lottery/match/getBetInfo", method=RequestMethod.POST)
	public BaseResult<DLZQBetInfoDTO> getBetInfo(DlJcZqMatchBetParam param);
	
	@RequestMapping(path="/lottery/match/getBetInfoByOrderSn", method=RequestMethod.POST)
	public BaseResult<DLZQBetInfoDTO> getBetInfoByOrderSn(@RequestBody GetBetInfoByOrderSn param);
	
	@RequestMapping(path="/lottery/match/getBasketBallBetInfoByOrderSn", method=RequestMethod.POST)
	public BaseResult<DLLQBetInfoDTO> getBasketBallBetInfoByOrderSn(@RequestBody GetBetInfoByOrderSn param);
	
	@RequestMapping(path="/lottery/match/getBetInfoByLotto", method=RequestMethod.POST)
	public BaseResult<List<DLBetLottoInfoDTO>> getBetInfoByLotto(@RequestBody GetBetInfoByOrderSn param);
	
	@RequestMapping(path="/lottery/match/getCancelMatches", method=RequestMethod.POST)
	public BaseResult<List<String>> getCancelMatches(GetCancelMatchesParam param);

	@RequestMapping(path="/lottery/match/getMatchList", method=RequestMethod.POST)
	public  BaseResult<DlJcZqMatchListDTO> getMatchList(DlJcZqMatchListParam param);
	
	@RequestMapping(path="/lottery/match/isShutDownBet", method=RequestMethod.POST)
	public  BaseResult<Boolean> isShutDownBet(@RequestBody EmptyParam emptyParam);

	@RequestMapping(path="/lottery/match/getBetEndTime", method=RequestMethod.POST)
	public  BaseResult<Integer> getBetEndTime(@RequestBody  MatchTimePream matchTimePream);

	@RequestMapping(path="/lottery/match/isHideMatch", method=RequestMethod.POST)
	public   BaseResult<Boolean> isHideMatch( @RequestBody IsHideParam isHideParam);

	@RequestMapping(path="/lottery/match/getBetInfo1", method=RequestMethod.POST)
	public   BaseResult<DLZQBetInfoDTO> getBetInfo1(@RequestBody DlJcZqMatchBetParam param);

	@RequestMapping(path="/lottery/match/getMinBetMoney", method=RequestMethod.POST)
	public   BaseResult<Double> getMinBetMoney(@RequestBody EmptyParam emptyParam);

	@RequestMapping(path="/lottery/match/canBetMoney", method=RequestMethod.POST)
	public   BaseResult<Integer> canBetMoney(@RequestBody EmptyParam emptyParam);

	@RequestMapping(path="/lottery/match/getMatchByConditions", method=RequestMethod.POST)
	public BaseResult<List<LeagueInfoDTO>> getMatchByConditions(@RequestBody EmptyParam emptyParam);

	@RequestMapping(path="/lottery/match/getCancelMatches", method=RequestMethod.POST)
	public BaseResult<List<String>> getCancelMatches(@RequestBody DlPlayCodeParam playCodeParam);
	
	@RequestMapping(path="/lottery/match/createOrderBySimulate", method=RequestMethod.POST)
	public BaseResult<OrderIdDTO>  createOrderForStoreProject(@RequestBody DlJcZqMatchBetParam jcZqMatchBetParam);
}

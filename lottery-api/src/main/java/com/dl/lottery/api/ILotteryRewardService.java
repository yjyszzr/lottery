package com.dl.lottery.api;

import javax.validation.Valid;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.LotteryRewardByIssueDTO;
import com.dl.lottery.param.DlLotteryRewardByIssueParam;

@FeignClient(value="lottery-service")
public interface ILotteryRewardService {

	@RequestMapping(path="/lottery/reward/queryRewardByIssue", method=RequestMethod.POST)
    public BaseResult<LotteryRewardByIssueDTO> queryRewardByIssue(@Valid @RequestBody DlLotteryRewardByIssueParam param);

	@PostMapping("/lottery/reward/updateOrderAfterOpenReward")
    public BaseResult<String> updateOrderAfterOpenReward(@RequestBody EmptyParam emptyParam);

}

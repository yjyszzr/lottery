package com.dl.lottery.api;

import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.ActivityDTO;
import com.dl.lottery.param.ActTypeParam;

@FeignClient(value="lottery-service")
public interface ILotteryActivityService {

	@RequestMapping(path="/lottery/activity/queryActivityByActType", method=RequestMethod.POST)
    public BaseResult<List<ActivityDTO>> queryActivityByActType(@RequestBody ActTypeParam actTypeParam);
}

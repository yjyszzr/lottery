package com.dl.lottery.api;

import javax.validation.Valid;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.param.ArticleIdsParam;
import com.github.pagehelper.PageInfo;

@FeignClient(value="lottery-service")
public interface ILotteryArticleService {

	@RequestMapping(path="/dl/article/queryArticlesByIds", method=RequestMethod.POST)
    public BaseResult<PageInfo<DLArticleDTO>> queryArticlesByIds(@Valid @RequestBody ArticleIdsParam param);
}

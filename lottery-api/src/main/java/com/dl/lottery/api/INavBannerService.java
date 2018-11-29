package com.dl.lottery.api;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.DlBannerPicDTO;
import com.dl.lottery.dto.MatchResultDTO;
import com.dl.lottery.param.QueryMatchResultByPlayCodeParam;
import com.dl.lottery.param.QueryMatchResultsByPlayCodesParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value="lottery-service")
public interface INavBannerService {

    @RequestMapping(path="/lottery/nav/banner/shopBanners", method=RequestMethod.POST)
    public BaseResult<List<DlBannerPicDTO>> shopBanners(@RequestBody EmptyParam param);
    
}
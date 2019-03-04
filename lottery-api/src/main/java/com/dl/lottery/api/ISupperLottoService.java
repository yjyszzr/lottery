package com.dl.lottery.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.DlSuperLottoRewardDTO;
import com.dl.lottery.param.SupperLottoParam;

@FeignClient(value="lottery-service")
public interface ISupperLottoService {

    @RequestMapping(path="/lottery/supperLotto/byTermNum", method=RequestMethod.POST)
    public BaseResult< List<DlSuperLottoRewardDTO>> findByTermNum(@RequestBody SupperLottoParam param);
    
}

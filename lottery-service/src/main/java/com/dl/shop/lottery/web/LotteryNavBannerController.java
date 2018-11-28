package com.dl.shop.lottery.web;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.DlBannerPicDTO;
import com.dl.member.api.ISwitchConfigService;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.param.StrParam;
import com.dl.shop.lottery.model.LotteryNavBanner;
import com.dl.shop.lottery.service.LotteryNavBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.swing.BakedArrayList;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* Created by CodeGenerator on 2018/03/23.
*/
@RestController
@RequestMapping("/lottery/nav/banner")
public class LotteryNavBannerController {
    @Resource
    private LotteryNavBannerService lotteryNavBannerService;

    @Resource
    private ISwitchConfigService  iSwitchConfigService;

    @ApiOperation(value = "广告图", notes = "广告图")
    @PostMapping("/adNavs")
    public BaseResult<?> queryNavs(@RequestBody EmptyParam param){
        Integer dealSwitch = 2;//默认交易版
        BaseResult<SwitchConfigDTO> switchRst = iSwitchConfigService.querySwitch(new StrParam(""));
        if(switchRst.getCode() != 0){
            dealSwitch = 2;
        }else{
            SwitchConfigDTO switchConfigDTO = switchRst.getData();
            Integer dealTurnOn = switchConfigDTO.getTurnOn();
            dealSwitch = dealTurnOn == 1?2:1;
        }

        List<LotteryNavBanner> navList = lotteryNavBannerService.queryNavBannerByType(3);
        List<LotteryNavBanner> navFilterList = new ArrayList<>();
        if(dealSwitch == 2){
            navFilterList = navList.stream().filter(s->"2".equals(s.getIsTransaction())).collect(Collectors.toList());
        }else if(dealSwitch == 1){
            navFilterList = navList.stream().filter(s->"1".equals(s.getIsTransaction())).collect(Collectors.toList());
        }

        List<DlBannerPicDTO> navPicDTOList = new ArrayList<>();
        if(navFilterList.size() > 0){
            navPicDTOList = navFilterList.stream().map(e->new DlBannerPicDTO(e.getBannerName(),e.getBannerImage(),
                    e.getBannerLink(),e.getStartTime(),e.getEndTime())).collect(Collectors.toList());
        }

        return ResultGenerator.genSuccessResult("success",navPicDTOList);
    }

    @ApiOperation(value = "开屏图", notes = "开屏图")
    @PostMapping("/openNavs")
    public BaseResult<?> openNavs(@RequestBody EmptyParam param){
        Integer dealSwitch = 2;//默认交易版
        BaseResult<SwitchConfigDTO> switchRst = iSwitchConfigService.querySwitch(new StrParam(""));
        if(switchRst.getCode() != 0){
            dealSwitch = 2;
        }else{
            SwitchConfigDTO switchConfigDTO = switchRst.getData();
            Integer dealTurnOn = switchConfigDTO.getTurnOn();
            dealSwitch = dealTurnOn == 1?2:1;
        }

        List<LotteryNavBanner> navList = lotteryNavBannerService.queryNavBannerByType(2);
        List<LotteryNavBanner> navFilterList = new ArrayList<>();
        if(dealSwitch == 2){
            navFilterList = navList.stream().filter(s->"2".equals(s.getIsTransaction())).collect(Collectors.toList());
        }

        List<DlBannerPicDTO> navPicDTOList = new ArrayList<>();
        if(navFilterList.size() > 0){
            navPicDTOList = navFilterList.stream().map(e->new DlBannerPicDTO(e.getBannerName(),e.getBannerImage(),
                    e.getBannerLink(),e.getStartTime(),e.getEndTime())).collect(Collectors.toList());
        }

        return ResultGenerator.genSuccessResult("success",navPicDTOList);
    }

}

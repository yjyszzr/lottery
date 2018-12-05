package com.dl.shop.lottery.web;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.DlBannerPicDTO;
import com.dl.member.api.IDeviceControlService;
import com.dl.member.api.ISwitchConfigService;
import com.dl.member.dto.DlDeviceActionControlDTO;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.param.DlDeviceActionControlParam;
import com.dl.member.param.MacParam;
import com.dl.member.param.StrParam;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.model.LotteryNavBanner;
import com.dl.shop.lottery.service.LotteryNavBannerService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* Created by CodeGenerator on 2018/03/23.
*/

@RestController
@RequestMapping("/lottery/nav/banner")
@Slf4j
public class LotteryNavBannerController {
    @Resource
    private LotteryNavBannerService lotteryNavBannerService;

    @Resource
    private ISwitchConfigService  iSwitchConfigService;

    @Resource
    private LotteryConfig lotteryConfig;

    @Resource
    private IDeviceControlService iDeviceControlService;

//    @ApiOperation(value = "广告图", notes = "广告图")
////    @PostMapping("/adNavs")
////    public BaseResult<?> queryNavs(@RequestBody EmptyParam param){
////        Integer dealSwitch = 2;//默认交易版
////        BaseResult<SwitchConfigDTO> switchRst = iSwitchConfigService.querySwitch(new StrParam(""));
////        if(switchRst.getCode() != 0){
////            dealSwitch = 2;
////        }else{
////            SwitchConfigDTO switchConfigDTO = switchRst.getData();
////            Integer dealTurnOn = switchConfigDTO.getTurnOn();
////            dealSwitch = dealTurnOn == 1?2:1;
////        }
////
////        List<LotteryNavBanner> navList = lotteryNavBannerService.queryNavBannerByType(3);
////        List<LotteryNavBanner> navFilterList = new ArrayList<>();
////        if(dealSwitch == 2){
////            navFilterList = navList.stream().filter(s->"2".equals(s.getIsTransaction())).collect(Collectors.toList());
////        }else if(dealSwitch == 1){
////            navFilterList = navList.stream().filter(s->"1".equals(s.getIsTransaction())).collect(Collectors.toList());
////        }
////
////        DlBannerPicDTO dto = new DlBannerPicDTO();
////        dto = new DlBannerPicDTO(e.getBannerName(),e.getBannerImage(),e.getBannerLink(),e.getStartTime(),e.getEndTime());
////
////        return ResultGenerator.genSuccessResult("success",dto);
////    }

    @ApiOperation(value = "商城banner轮播图", notes = "商城banner轮播图")
    @PostMapping("/shopBanners")
    public BaseResult<List<DlBannerPicDTO>> shopBanners(@RequestBody EmptyParam param){
        List<LotteryNavBanner> navList = lotteryNavBannerService.queryNavBannerByType(3);
        List<DlBannerPicDTO> bannerPicDTOList = new ArrayList<DlBannerPicDTO>();
        if(navList.size() > 0){
            bannerPicDTOList =  navList.stream().map(e->new DlBannerPicDTO(e.getBannerName(),lotteryConfig.getBannerShowUrl()+e.getBannerImage(),e.getBannerLink(),e.getStartTime(),e.getEndTime())).collect(Collectors.toList());
        }

        return ResultGenerator.genSuccessResult("success",bannerPicDTOList);
    }

    @ApiOperation(value = "开屏图", notes = "开屏图")
    @PostMapping("/openNavs")
    public BaseResult<DlBannerPicDTO> openNavs(@RequestBody EmptyParam param){
        Integer dealSwitch = 2;//默认交易版
//        BaseResult<SwitchConfigDTO> switchRst = iSwitchConfigService.querySwitch(new StrParam(""));
//        if(switchRst.getCode() != 0){
//            dealSwitch = 2;
//        }else{
//            SwitchConfigDTO switchConfigDTO = switchRst.getData();
//            Integer dealTurnOn = switchConfigDTO.getTurnOn();
//            dealSwitch = dealTurnOn == 1?2:1;
//        }
//
//        List<LotteryNavBanner> navList = lotteryNavBannerService.queryNavBannerByType(2);
//        List<LotteryNavBanner> navFilterList = new ArrayList<>();
        if(dealSwitch == 2){
            navFilterList = navList.stream().filter(s->"2".equals(s.getIsTransaction())).collect(Collectors.toList());
        }

        DlBannerPicDTO dto = new DlBannerPicDTO();
        List<DlBannerPicDTO> navPicDTOList = new ArrayList<>();
        if(navFilterList.size() > 0){
            LotteryNavBanner navBanner = navList.get(0);
            UserDeviceInfo userDevice = SessionUtil.getUserDevice();
            String plat = userDevice.getPlat(); //1-android,2-iphone
            String deviceUnique = "";
            if ("android".equals(userDevice.getPlat())){
                deviceUnique = userDevice.getAndroidid();
                log.info("android,"+deviceUnique);
            }else if("iphone".equals(userDevice.getPlat())){
                deviceUnique = userDevice.getIDFA();
                log.info("iphone,"+deviceUnique);
            }


            log.info("deviceUnique:"+deviceUnique);
            MacParam macParam = new MacParam();
            macParam.setMac(deviceUnique);
            BaseResult<DlDeviceActionControlDTO> deviceActionControlDTOBaseResult = iDeviceControlService.queryDeviceByIMEI(macParam);
            if(deviceActionControlDTOBaseResult.getCode() == 0){
                DlDeviceActionControlDTO deviceActionControlDTO = deviceActionControlDTOBaseResult.getData();
                Integer alertTime = deviceActionControlDTO.getAddTime();
                Integer endTodayTime = DateUtil.getTimeAfterDays(new Date(),0,0,0,0);
                if(alertTime !=null && endTodayTime -alertTime > 0){
                    dto.setBannerName(navBanner.getBannerName());
                    dto.setBannerImage(lotteryConfig.getBannerShowUrl()+ navBanner.getBannerImage());
                    dto.setBannerLink(navBanner.getBannerLink());
                    dto.setStartTime(navBanner.getStartTime());
                    dto.setEndTime(navBanner.getEndTime());
                }
            }else if(deviceActionControlDTOBaseResult.getCode() == MemberEnums.DBDATA_IS_NULL.getcode()){
                DlDeviceActionControlParam deviceParam = new DlDeviceActionControlParam();
                deviceParam.setAddTime(DateUtil.getCurrentTimeLong());
                deviceParam.setAlertTimes(1);
                deviceParam.setBusiType(1);
                deviceParam.setMac(deviceUnique);
                iDeviceControlService.add(deviceParam);
                dto.setBannerName(navBanner.getBannerName());
                dto.setBannerImage(lotteryConfig.getBannerShowUrl() + navBanner.getBannerImage());
                dto.setBannerLink(navBanner.getBannerLink());
                dto.setStartTime(navBanner.getStartTime());
                dto.setEndTime(navBanner.getEndTime());
            }else{
                dto.setBannerName(navBanner.getBannerName());
                dto.setBannerImage(lotteryConfig.getBannerShowUrl()+ navBanner.getBannerImage());
                dto.setBannerLink(navBanner.getBannerLink());
                dto.setStartTime(navBanner.getStartTime());
                dto.setEndTime(navBanner.getEndTime());
            }
        }

        return ResultGenerator.genSuccessResult("success",dto);
    }

}

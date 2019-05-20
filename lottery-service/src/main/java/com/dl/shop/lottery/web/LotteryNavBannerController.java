package com.dl.shop.lottery.web;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.DlBannerPicDTO;
import com.dl.member.api.IDeviceControlService;
import com.dl.member.api.ISwitchConfigService;
import com.dl.member.api.IUserBonusService;
import com.dl.member.dto.DlDeviceActionControlDTO;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.param.DlDeviceActionControlParam;
import com.dl.member.param.MacParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.UserBonusIdParam;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.model.LotteryNavBanner;
import com.dl.shop.lottery.service.LotteryNavBannerService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

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

    @Resource
    private IUserBonusService iUserBonusService;
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
        List<LotteryNavBanner> navList = lotteryNavBannerService.queryNavBannerByType(3,null);
        List<DlBannerPicDTO> bannerPicDTOList = new ArrayList<DlBannerPicDTO>();
        if(navList.size() > 0){
            bannerPicDTOList =  navList.stream().map(e->new DlBannerPicDTO(e.getBannerName(),lotteryConfig.getBannerShowUrl()+e.getBannerImage(),e.getBannerLink(),e.getStartTime(),e.getEndTime())).collect(Collectors.toList());
        }

        return ResultGenerator.genSuccessResult("success",bannerPicDTOList);
    }

    @ApiOperation(value = "开屏图", notes = "开屏图")
    @PostMapping("/openNavs")
    public BaseResult<List<Object>> openNavs(@RequestBody EmptyParam param){
        Integer dealSwitch = 2;//默认交易版
        BaseResult<SwitchConfigDTO> switchRst = iSwitchConfigService.querySwitch(new StrParam(""));
        if(switchRst.getCode() != 0){
            dealSwitch = 2;
        }else{
            SwitchConfigDTO switchConfigDTO = switchRst.getData();
            Integer dealTurnOn = switchConfigDTO.getTurnOn();
            dealSwitch = dealTurnOn == 1?2:1;
        }

        UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
        String appCodeNameStr = userDeviceInfo.getAppCodeName();
        String appCodeName = StringUtils.isEmpty(appCodeNameStr)?"10":appCodeNameStr;
        List<LotteryNavBanner> navList = lotteryNavBannerService.queryNavBannerByType(2,appCodeName);
        List<LotteryNavBanner> navFilterList = new ArrayList<>();
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
                log.info(JSON.toJSONString(userDevice));
                deviceUnique = userDevice.getAndroidid();
                log.info("android,"+deviceUnique);
            }else if("iphone".equals(userDevice.getPlat())){
                deviceUnique = userDevice.getIDFA();
                log.info("iphone,"+deviceUnique);
            }else if("h5".equals(userDevice.getPlat())){
                deviceUnique = "h5";
                log.info("h5,"+deviceUnique);
            }

            log.info("deviceUnique:"+deviceUnique);
            if(!StringUtils.isEmpty(deviceUnique)){
                if(deviceUnique.equals("h5")){//h5特色需求 如果有开屏图，总是返回
                    dto.setBannerName(navBanner.getBannerName());
                    dto.setBannerImage(lotteryConfig.getBannerShowUrl()+ navBanner.getBannerImage());
                    dto.setBannerLink(navBanner.getBannerLink());
                    dto.setStartTime(navBanner.getStartTime());
                    dto.setEndTime(navBanner.getEndTime());
                }

                MacParam macParam = new MacParam();
                macParam.setMac(deviceUnique);
                BaseResult<DlDeviceActionControlDTO> deviceActionControlDTOBaseResult = iDeviceControlService.queryDeviceByIMEI(macParam);
                if(deviceActionControlDTOBaseResult.getCode() == 0){
                    DlDeviceActionControlDTO deviceActionControlDTO = deviceActionControlDTOBaseResult.getData();
                    Integer alertTime = deviceActionControlDTO.getUpdateTime();
                    Integer endTodayTime = DateUtil.getTimeAfterDays(new Date(),0,0,0,0);
                    if(endTodayTime - alertTime > 0){
                        dto.setBannerName(navBanner.getBannerName());
                        dto.setBannerImage(lotteryConfig.getBannerShowUrl()+ navBanner.getBannerImage());
                        dto.setBannerLink(navBanner.getBannerLink());
                        dto.setStartTime(navBanner.getStartTime());
                        dto.setEndTime(navBanner.getEndTime());
                        MacParam updateMac = new MacParam();
                        updateMac.setMac(deviceUnique);
                        iDeviceControlService.updateDeviceControlUpdteTime(updateMac);
                    }
                }else if(deviceActionControlDTOBaseResult.getCode() == MemberEnums.DBDATA_IS_NULL.getcode()){
                    DlDeviceActionControlParam deviceParam = new DlDeviceActionControlParam();
                    deviceParam.setAddTime(DateUtil.getCurrentTimeLong());
                    deviceParam.setUpdateTime(DateUtil.getCurrentTimeLong());
                    deviceParam.setAlertTimes(1);
                    deviceParam.setBusiType(1);
                    deviceParam.setMac(deviceUnique);
                    iDeviceControlService.add(deviceParam);
                    dto.setBannerName(navBanner.getBannerName());
                    dto.setBannerImage(lotteryConfig.getBannerShowUrl() + navBanner.getBannerImage());
                    dto.setBannerLink(navBanner.getBannerLink());
                    dto.setStartTime(navBanner.getStartTime());
                    dto.setEndTime(navBanner.getEndTime());
                }
            }

        }
        
        List<Object> list = new ArrayList();
        HashMap<String, Object> result = new HashMap();
        dto.setBannerImage("https://image.so.com/view?q=%E5%9B%BE%E7%89%87&listsrc=sobox&listsign=3f58ebc8d3202e4e475cbe2b0f86a143&src=360pic_strong&correct=%E5%9B%BE%E7%89%87&ancestor=list&cmsid=7e9b89e43cd83eea919ad96a1d6d7000&cmran=0&cmras=6&cn=0&gn=0&kn=37&fsn=97#id=27d096087c1f11f337ceb4d71ead6606&currsn=0");
        dto.setBannerLink("http://www.baidu.com");
        dto.setBannerName("测试 活动");
        dto.setStartTime(1541779199);
        dto.setEndTime(1641779199);
        result.put("name", "活动");
        result.put("bannerImage", dto.getBannerImage());
        result.put("bannerLink", dto.getBannerLink());
        result.put("bannerName", dto.getBannerName());
        result.put("endTime", dto.getEndTime());
        result.put("startTime", dto.getStartTime());
        list.add(result);
        if(SessionUtil.getUserId()==null || "".equals(SessionUtil.getUserId())) {//用户未登录
        }else {
        	//获取用户可用红包数量和金额
            UserBonusIdParam userBonusIdParam = new UserBonusIdParam();
            userBonusIdParam.setUserBonusId(SessionUtil.getUserId());
            BaseResult<UserBonusDTO> userBonus = iUserBonusService.queryUserBonusNumAndPrice(userBonusIdParam);
            result = new HashMap();
            result.put("name", "红包");
            result.put("bonusPrice", userBonus.getData().getBonusPrice());
            result.put("bonusNumber", userBonus.getData().getBonusId());
            list.add(result);
        }
        return ResultGenerator.genSuccessResult("success",list);
    }

}

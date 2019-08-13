package com.dl.shop.lottery.web;
import java.math.BigDecimal;
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
    public BaseResult<DlBannerPicDTO> openNavs(@RequestBody EmptyParam param){
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
            boolean isflag = false;
            if ("android".equals(userDevice.getPlat())){
                log.info(JSON.toJSONString(userDevice));
                deviceUnique = userDevice.getAndroidid();
                isflag = true;
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
                log.info("isflag："+isflag);
                if(isflag) {
                	log.info("isflag："+isflag);
                	dto.setBannerName(navBanner.getBannerName());
                    dto.setBannerImage(lotteryConfig.getBannerShowUrl()+ navBanner.getBannerImage());
                    dto.setBannerLink(navBanner.getBannerLink());
                    dto.setStartTime(navBanner.getStartTime());
                    dto.setEndTime(navBanner.getEndTime());
                }else {
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

        }

        return ResultGenerator.genSuccessResult("success",dto);
    }

    @ApiOperation(value = "开屏图", notes = "开屏图")
    @PostMapping("/openNavsNew")
    public BaseResult<List<Object>> openNavsNew(@RequestBody EmptyParam param){
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
        
//        navFilterList=navFilterList.stream().filter(s->s.getId()==316).collect(Collectors.toList());//过滤ID为316的新版升级开屏图
        
        DlBannerPicDTO dto = null;
        String deviceUnique = "";
        UserDeviceInfo userDevice = SessionUtil.getUserDevice();
        String plat = userDevice.getPlat(); //1-android,2-iphone
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
        List<DlBannerPicDTO> navPicDTOList = new ArrayList<>();
        if(navFilterList.size() > 0){
            LotteryNavBanner navBanner = navList.get(0);
            log.info("deviceUnique:"+deviceUnique);
            if(!StringUtils.isEmpty(deviceUnique)){
                if(deviceUnique.equals("h5")){//h5特色需求 如果有开屏图，总是返回
                	dto = new DlBannerPicDTO();
                    dto.setBannerName(navBanner.getBannerName());
                    dto.setBannerImage(lotteryConfig.getBannerShowUrl()+ navBanner.getBannerImage());
                    dto.setBannerLink(navBanner.getBannerLink());
                    dto.setStartTime(navBanner.getStartTime());
                    dto.setEndTime(navBanner.getEndTime());
                }

                MacParam macParam = new MacParam();
                macParam.setMac(deviceUnique);
                macParam.setBusiType(1);
                BaseResult<DlDeviceActionControlDTO> deviceActionControlDTOBaseResult = iDeviceControlService.queryDeviceByIMEI(macParam);
                if(deviceActionControlDTOBaseResult.getCode() == 0){
                    DlDeviceActionControlDTO deviceActionControlDTO = deviceActionControlDTOBaseResult.getData();
                    Integer alertTime = deviceActionControlDTO.getUpdateTime();
                    Integer endTodayTime = DateUtil.getTimeAfterDays(new Date(),0,0,0,0);
                    if(endTodayTime - alertTime > 0){
                    	dto = new DlBannerPicDTO();
                        dto.setBannerName(navBanner.getBannerName());
                        dto.setBannerImage(lotteryConfig.getBannerShowUrl()+ navBanner.getBannerImage());
                        dto.setBannerLink(navBanner.getBannerLink());
                        dto.setStartTime(navBanner.getStartTime());
                        dto.setEndTime(navBanner.getEndTime());
                        MacParam updateMac = new MacParam();
                        updateMac.setMac(deviceUnique);
                        updateMac.setBusiType(1);
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
                    dto = new DlBannerPicDTO();
                    dto.setBannerName(navBanner.getBannerName());
                    dto.setBannerImage(lotteryConfig.getBannerShowUrl() + navBanner.getBannerImage());
                    dto.setBannerLink(navBanner.getBannerLink());
                    dto.setStartTime(navBanner.getStartTime());
                    dto.setEndTime(navBanner.getEndTime());
                }
            }

        }
        
        List<Object> list = new ArrayList();
//        dto = new DlBannerPicDTO();
//        dto.setBannerImage("http://img1.efu.com.cn/upfile/fashion/photo/15310/360408.jpg");
//        dto.setBannerLink("http://39.106.18.39:8765/api/lottery/freebuy/singleNote?cxmxc=scm&type=3&id=1");
//        dto.setBannerName("测试活动");
//        dto.setStartTime(1541779199);
//        dto.setEndTime(1641779199);
        if(dto!=null) {
	        HashMap<String, Object> result = new HashMap();
	        result.put("name", "1");
	        result.put("bannerImage", dto.getBannerImage());
	        result.put("bannerLink", dto.getBannerLink());
	        result.put("bannerName", dto.getBannerName());
	        result.put("endTime", dto.getEndTime());
	        result.put("startTime", dto.getStartTime());
	        list.add(result);
        }
        
        if(SessionUtil.getUserId()==null || "".equals(SessionUtil.getUserId())) {//用户未登录
        }else {
        	//优惠券弹出
            boolean isQshow = false;
        	//获取用户可用红包数量和金额
            UserBonusIdParam userBonusIdParam = new UserBonusIdParam();
            userBonusIdParam.setUserBonusId(SessionUtil.getUserId());
            BaseResult<UserBonusDTO> userBonus = iUserBonusService.queryUserBonusNumAndPrice(userBonusIdParam);
            if(userBonus.getData()!=null&&userBonus.getData().getBonusId()>0) {//有用户红包
	        	if(!StringUtils.isEmpty(deviceUnique)){
	                if(deviceUnique.equals("h5")){//h5特色需求 如果有开屏图，总是返回
	                	isQshow = true;
	                }
	
	                MacParam macParam = new MacParam();
	                macParam.setMac(deviceUnique);
	                macParam.setBusiType(2);
	                BaseResult<DlDeviceActionControlDTO> deviceActionControlDTOBaseResult = iDeviceControlService.queryDeviceByIMEI(macParam);
	                if(deviceActionControlDTOBaseResult.getCode() == 0){
	                    DlDeviceActionControlDTO deviceActionControlDTO = deviceActionControlDTOBaseResult.getData();
	                    Integer alertTime = deviceActionControlDTO.getUpdateTime();
	                    Integer endTodayTime = DateUtil.getTimeAfterDays(new Date(),0,0,0,0);
	                    if(endTodayTime - alertTime > 0){
	                    	isQshow = true;
	                        MacParam updateMac = new MacParam();
	                        updateMac.setMac(deviceUnique);
	                        updateMac.setBusiType(2);
	                        iDeviceControlService.updateDeviceControlUpdteTime(updateMac);
	                    }
	                }else if(deviceActionControlDTOBaseResult.getCode() == MemberEnums.DBDATA_IS_NULL.getcode()){
	                    DlDeviceActionControlParam deviceParam = new DlDeviceActionControlParam();
	                    deviceParam.setAddTime(DateUtil.getCurrentTimeLong());
	                    deviceParam.setUpdateTime(DateUtil.getCurrentTimeLong());
	                    deviceParam.setAlertTimes(1);
	                    deviceParam.setBusiType(2);
	                    deviceParam.setMac(deviceUnique);
	                    iDeviceControlService.add(deviceParam);
	                    isQshow = true;
	                }
	            }
        	
	        	if(isQshow) {
	            	HashMap<String, Object> result = new HashMap();
		            result.put("name", "2");
		            result.put("bonusPrice", userBonus.getData()!=null?(userBonus.getData().getBonusPrice()!=null?userBonus.getData().getBonusPrice():BigDecimal.ZERO):BigDecimal.ZERO);
		            result.put("bonusNumber", userBonus.getData()!=null?userBonus.getData().getBonusId():0);
		            list.add(result);
	        	}
            }
        }
        return ResultGenerator.genSuccessResult("success",list);
    }
    @ApiOperation(value = "首页悬浮图", notes = "首页悬浮图")
    @PostMapping("/openNavsSusp")
    public BaseResult<DlBannerPicDTO> openNavsSusp(@RequestBody EmptyParam param){
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
        String appCodeNameStr = userDeviceInfo==null?"11":userDeviceInfo.getAppCodeName();
        String appCodeName = StringUtils.isEmpty(appCodeNameStr)?"10":appCodeNameStr;
        List<LotteryNavBanner> navList = lotteryNavBannerService.queryNavBannerByType(4,appCodeName);
        List<LotteryNavBanner> navFilterList = new ArrayList<>();
        if(dealSwitch == 2){
            navFilterList = navList.stream().filter(s->"2".equals(s.getIsTransaction())).collect(Collectors.toList());
        }
        log.info("susp():dealSwitch="+dealSwitch+";navFilterList="+navFilterList.size());
        DlBannerPicDTO dto = null;
        if(navList.size() > 0){
        	dto=new DlBannerPicDTO();
            LotteryNavBanner navBanner = navList.get(0);
            UserDeviceInfo userDevice = SessionUtil.getUserDevice();
            String deviceUnique = "";
            boolean isflag = true;
            if ("android".equals(userDevice.getPlat())){
                log.info(JSON.toJSONString(userDevice));
                deviceUnique = userDevice.getAndroidid();
                isflag = true;
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
                log.info("isflag："+isflag);
                if(isflag) {
                	log.info("isflag："+isflag);
                	dto.setBannerName(navBanner.getBannerName());
                    dto.setBannerImage(lotteryConfig.getBannerShowUrl()+ navBanner.getBannerImage());
                    dto.setBannerLink(navBanner.getBannerLink());
                    dto.setStartTime(navBanner.getStartTime());
                    dto.setEndTime(navBanner.getEndTime());
                }else {
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

        }

        return ResultGenerator.genSuccessResult("success",dto);
    }
}

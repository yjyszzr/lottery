package com.dl.shop.lottery.service;

import com.alibaba.druid.util.StringUtils;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.DlDiscoveryHallClassifyDTO;
import com.dl.lottery.dto.DlHallDTO;
import com.dl.lottery.dto.DlHallDTO.DlActivityDTO;
import com.dl.lottery.dto.DlHallDTO.DlLotteryClassifyDTO;
import com.dl.lottery.dto.DlHallDTO.DlNavBannerDTO;
import com.dl.lottery.dto.DlHallDTO.DlWinningLogDTO;
import com.dl.lottery.dto.DlPlayClassifyDTO;
import com.dl.lottery.dto.DlPlayClassifyDTO.DlPlayTitleDTO;
import com.dl.lottery.dto.DlPlayClassifyDetailDTO;
import com.dl.lottery.param.DlPlayClassifyParam;
import com.dl.lottery.param.HallParam;
import com.dl.lotto.api.ISuperLottoService;
import com.dl.lotto.dto.LottoDTO;
import com.dl.member.api.ISwitchConfigService;
import com.dl.member.api.ISysConfigService;
import com.dl.member.api.IUserService;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.param.BusiIdsListParam;
import com.dl.member.param.StrParam;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.*;
import com.dl.shop.lottery.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(value = "transactionManager1")
@Slf4j
public class LotteryHallService {

	@Resource
	private LotteryNavBannerMapper lotteryNavBannerMapper;

	@Resource
	private LotteryActivityMapper lotteryActivityMapper;

	@Resource
	private LotteryWinningLogTempMapper lotteryWinningLogTempMapper;

	@Resource
	private LotteryClassifyMapper lotteryClassifyMapper;

	@Resource
	private LotteryPlayClassifyMapper lotteryPlayClassifyMapper;
	
	@Resource
	private LotteryConfig lotteryConfig;
	
	@Resource
	private IUserService userService;
	
	@Resource
	private	ISuperLottoService iSuperLottoService;
	
	@Resource
	private ISwitchConfigService  iSwitchConfigService;

	@Resource
	private DlDiscoveryHallClassifyService dlDiscoveryHallClassifyService;

    @Resource
    private LotteryMatchService lotteryMatchService;

	@Resource
	private ISysConfigService iSysConfigService;

	/**
	 * 获取彩票大厅数据
	 * 第一版本使用
	 * @return
	 */
	public DlHallDTO getHallDataAllLottery(HallParam hallParam) {
		DlHallDTO dlHallDTO = new DlHallDTO();
		// 获取首页轮播图列表
		dlHallDTO.setNavBanners(getDlNavBannerDTO(hallParam));
		// 获取活动数据
		dlHallDTO.setActivity(getDlActivityDTO(hallParam));
		// 获取中奖信息列表
		dlHallDTO.setWinningMsgs(getDlWinningLogDTOs());
		// 获取彩票分类列表
		// dlHallDTO.setLotteryClassifys(getDlLotteryClassifyDTOs());
		// //第一版只显示竞彩足球的子列表
		List<DlPlayClassifyDetailDTO> dlPlayClassifyDetailDTOs = new ArrayList<DlPlayClassifyDetailDTO>();
		//新版本展示的是不同彩种的logo相关信息
		List<LotteryClassify> classifyList = lotteryClassifyMapper.selectAllLotteryClassData();
		for(LotteryClassify s:classifyList){
			DlPlayClassifyDetailDTO  dto = new DlPlayClassifyDetailDTO();
			dto.setPlayClassifyId(String.valueOf(s.getLotteryClassifyId()));
			dto.setPlayClassifyImg(lotteryConfig.getBannerShowUrl()+s.getLotteryImg());
			dto.setSubTitle(s.getSubTitle());
			dto.setPlayClassifyName(s.getLotteryName());
			dto.setRedirectUrl(s.getRedirectUrl());
			dlPlayClassifyDetailDTOs.add(dto);
		}
		dlHallDTO.setDlPlayClassifyDetailDTOs(dlPlayClassifyDetailDTOs);
		return dlHallDTO;
	}
	/**
	 * 新版本使用，多采种
	 * @param hallParam
	 * @return
	 */
	public DlHallDTO getHallDataAllLottery1(HallParam hallParam) {
		DlHallDTO dlHallDTO = new DlHallDTO();
		// 获取首页轮播图列表
		dlHallDTO.setNavBanners(getDlNavBannerDTO(hallParam));
		// 获取活动数据
		dlHallDTO.setActivity(null);
		//获取发现页的各个图标
		dlHallDTO.setDiscoveryHallClassifyDTOList(queryDisHallClassByType());
		// 获取中奖信息列表
		dlHallDTO.setWinningMsgs(getDlWinningLogDTOs());
		// 获取彩票分类列表
		// dlHallDTO.setLotteryClassifys(getDlLotteryClassifyDTOs());
		// //第一版只显示竞彩足球的子列表
		List<DlLotteryClassifyDTO> lotteryClassifys = new ArrayList<DlLotteryClassifyDTO>();

		//新版本展示的是不同彩种的logo相关信息
		List<LotteryClassify> classifyList = lotteryClassifyMapper.selectAllLotteryClasses();
		for(LotteryClassify s:classifyList){
			DlLotteryClassifyDTO  dto = new DlLotteryClassifyDTO();
			dto.setLotteryId(s.getLotteryClassifyId().toString());
			dto.setLotteryImg(lotteryConfig.getBannerShowUrl()+s.getLotteryImg());
			dto.setLotteryName(s.getLotteryName());
			if(2 == s.getLotteryClassifyId()) {
				dto.setSubTitle(this.queryLatestLottoPrizes());
			}else {
				dto.setSubTitle(s.getSubTitle());
			}
			dto.setStatus(s.getStatus().toString());
			dto.setStatusReason(s.getStatusReason());
			dto.setRedirectUrl(s.getRedirectUrl());
			lotteryClassifys.add(dto);
		}
		dlHallDTO.setLotteryClassifys(lotteryClassifys);
		return dlHallDTO;
	}
	
	public String  queryLatestLottoPrizes() {
		String prizes = "";
		EmptyParam emptyParam = new EmptyParam();
		emptyParam.setEmptyStr("");
		BaseResult<LottoDTO> lottoDtoRst = iSuperLottoService.queryLottoLatestPrizes(emptyParam);
		if(0 != lottoDtoRst.getCode()) {
			return prizes;
		}
		LottoDTO lottoDTO = lottoDtoRst.getData();
		return lottoDTO.getPrizes();
	}
	
	/**
	 * 获取彩票大厅数据
	 * 
	 * @return
	 */
	public DlHallDTO getHallData(HallParam hallParam) {
		Integer userId = SessionUtil.getUserId();
		DlHallDTO dlHallDTO = new DlHallDTO();
		// 获取首页轮播图列表
		dlHallDTO.setNavBanners(getDlNavBannerDTO(hallParam));
		// 获取活动数据
		dlHallDTO.setActivity(null);
		// 发现页图标
		dlHallDTO.setDiscoveryHallClassifyDTOList(queryDisHallClassByType());
		// 获取中奖信息列表
		dlHallDTO.setWinningMsgs(getDlWinningLogDTOs());
		// 获取彩票分类列表
		// dlHallDTO.setLotteryClassifys(getDlLotteryClassifyDTOs());
		// 第一版只显示竞彩足球的子列表
		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		List<DlPlayClassifyDetailDTO> dlPlayClassifyDetailDTOs = new ArrayList<DlPlayClassifyDetailDTO>();
		boolean isShowWorldCup = true;
		String channel = null;
		String appv = "";
		if(userDevice != null) {
			channel = userDevice.getChannel();
			appv = userDevice.getAppv();
			if(appv.compareTo("1.0.4") <= 0) {
				isShowWorldCup = false;
			}
		}
		
		String phoneChannel = "&qd="+channel;
		log.info("channel:"+channel);
		List<DlPlayClassifyDetailDTO> playClassifyList  = lotteryPlayClassifyMapper.selectAllData(1);
		String playClassifyUrl = playClassifyList.get(0).getRedirectUrl();
		List<LotteryClassify> classifyList = lotteryClassifyMapper.selectAllLotteryClasses();
		for(LotteryClassify lotteryClassify:classifyList) {
			DlPlayClassifyDetailDTO dlPlayDetailDto = new DlPlayClassifyDetailDTO();
			dlPlayDetailDto.setLotteryId(String.valueOf(lotteryClassify.getLotteryClassifyId()));
			dlPlayDetailDto.setPlayClassifyImg(lotteryConfig.getBannerShowUrl()+lotteryClassify.getLotteryImg());
			dlPlayDetailDto.setPlayClassifyLabelName(lotteryClassify.getSubTitle());
			
			if(1 == lotteryClassify.getLotteryClassifyId()) {
				String isLogin = "&isLogin=0";
				if(userId != null) {
					isLogin = "&isLogin=1";
				}
				dlPlayDetailDto.setRedirectUrl(playClassifyUrl+isLogin+phoneChannel);
				dlPlayDetailDto.setPlayClassifyName(lotteryClassify.getLotteryName());
			}else {
				dlPlayDetailDto.setRedirectUrl("");
				dlPlayDetailDto.setPlayClassifyName(lotteryClassify.getStatusReason());
			}
			dlPlayDetailDto.setSubTitle(lotteryClassify.getSubTitle());
			dlPlayClassifyDetailDTOs.add(dlPlayDetailDto);
		}

		if (CollectionUtils.isNotEmpty(dlPlayClassifyDetailDTOs)) {
			DlPlayClassifyDetailDTO wcDTO = null;
			for (DlPlayClassifyDetailDTO dto : dlPlayClassifyDetailDTOs) {
				dto.setLotteryId("1");
				if("8".equals(dto.getPlayClassifyId())) {
					channel = channel ==null?"":channel;
					String redirectUrl = dto.getRedirectUrl();
					redirectUrl = redirectUrl + "&channel="+channel;
					dto.setRedirectUrl(redirectUrl);
					wcDTO = dto;
				}
			}
			if(wcDTO != null && !isShowWorldCup) {
				dlPlayClassifyDetailDTOs.remove(wcDTO);
			}
		}
	
		dlHallDTO.setDlPlayClassifyDetailDTOs(dlPlayClassifyDetailDTOs);
		return dlHallDTO;
	}

	/**
	 * 获取彩票玩法列表
	 * 
	 * @return
	 */
	public DlPlayClassifyDTO getPlayClassifyList(DlPlayClassifyParam param) {
		DlPlayClassifyDTO dlPlayClassifyDTO = new DlPlayClassifyDTO();
		DlPlayTitleDTO dlPlayTitleDTO = new DlPlayTitleDTO();
		LotteryClassify lotteryClassify = lotteryClassifyMapper.selectByPrimaryKey(Integer.valueOf(param.getLotteryClassifyId()));
		if (null != lotteryClassify) {
			dlPlayTitleDTO.setPlayName(lotteryClassify.getLotteryName());
		}
		dlPlayClassifyDTO.setDlPlayTitleDTO(dlPlayTitleDTO);
		List<DlPlayClassifyDetailDTO> dlPlayClassifyDetailDTOs = lotteryPlayClassifyMapper.selectAllData(Integer.valueOf(param.getLotteryClassifyId()));
		dlPlayClassifyDTO.setDlPlayClassifyDetailDTOs(dlPlayClassifyDetailDTOs);
		return dlPlayClassifyDTO;
	}

	/**
	 * 	获取发现页的各个图标
	 *
	 */
	public List<DlDiscoveryHallClassifyDTO> queryDisHallClassByType(){
		UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
		String plat = userDeviceInfo.getPlat();
		Integer androidTurnOn = 0;
		Integer iosTurnOn = 0;
		Integer h5TurnOn = 1;
		List<Integer> typeList= Arrays.asList(2,4,7,9,10,11);//大厅的几个图标的类型
		List<DlDiscoveryHallClassifyDTO> dtoList = new ArrayList<>();
		List<DlDiscoveryHallClassify> discoveryList = dlDiscoveryHallClassifyService.queryDiscoveryListByType(typeList,2);

		BusiIdsListParam busiIdsListParam = new BusiIdsListParam();
		List<Integer> busiIdList = Arrays.asList(50,51,52);//android,ios,h5的大厅店铺按钮开关
		busiIdsListParam.setBusinessIdList(busiIdList);
		BaseResult<List<SysConfigDTO>> sysConfigDTOSRst = iSysConfigService.querySysConfigList(busiIdsListParam);
		List<SysConfigDTO> sysConfigDTOList = new ArrayList<>();
		if(sysConfigDTOSRst.isSuccess()){
			sysConfigDTOList = sysConfigDTOSRst.getData();
		}

		discoveryList.removeIf(s->s.getIsShow().equals("0") && !s.getType().equals("10"));
		if(sysConfigDTOList.size() > 0){
			androidTurnOn = sysConfigDTOList.get(0).getValue().intValue();
			iosTurnOn = sysConfigDTOList.get(1).getValue().intValue();
			h5TurnOn = sysConfigDTOList.get(2).getValue().intValue();
		}

		//根据 android,ios,h5的大厅店铺按钮开关 来决定是否显示 店铺按钮
		if((plat.equals("android") && androidTurnOn != null  && androidTurnOn == 0)||
		(plat.equals("iphone") && iosTurnOn != null && iosTurnOn == 0)||
		(plat.equals("h5") && h5TurnOn != null && h5TurnOn == 0)){
			discoveryList.removeIf(s->s.getType().equals("10"));
		}

		if(discoveryList.size() > 0){
			dtoList = discoveryList.stream().map(s->new DlDiscoveryHallClassifyDTO(String.valueOf(s.getClassifyId()),String.valueOf(s.getType()),s.getClassName(),lotteryConfig.getBannerShowUrl() + s.getClassImg(),s.getStatus(),s.getStatusReason(),s.getRedirectUrl()
			)).collect(Collectors.toList());
		}

		return dtoList;
	}

	/**
	 * 	获取发现页的各个图标
	 *
	 */
	public List<DlDiscoveryHallClassifyDTO> moreDiscoveryClass(){
		//查询交易版还是资讯版
		String isTransaction = ProjectConstant.DEAL_VERSION;//默认交易版
		StrParam strParam = new StrParam();
		BaseResult<SwitchConfigDTO> switchConfigDto = iSwitchConfigService.querySwitch(strParam);
		if(switchConfigDto.getCode() == 0) {
			Integer turnOn = switchConfigDto.getData().getTurnOn();
			isTransaction = (turnOn == 1)?ProjectConstant.DEAL_VERSION:ProjectConstant.INFO_VERSION;
		}

		List<Integer> typeList= new ArrayList<>();
		typeList.add(1);
		typeList.add(3);
		List<DlDiscoveryHallClassifyDTO> dtoList = new ArrayList<>();
		if (typeList.size() > 0){
			List<DlDiscoveryHallClassify> discoveryList = dlDiscoveryHallClassifyService.queryDiscoveryListByType(typeList,Integer.valueOf(isTransaction));
			if(discoveryList.size() > 0){
				dtoList = discoveryList.stream().map(s->new DlDiscoveryHallClassifyDTO(String.valueOf(s.getClassifyId()),String.valueOf(s.getType()),s.getClassName(),lotteryConfig.getBannerShowUrl() + s.getClassImg(),s.getStatus(),s.getStatusReason(),s.getRedirectUrl()
				)).collect(Collectors.toList());
			}
		}


		return dtoList;
	}

	/**
	 * 获取活动数据
	 * 
	 * @return
	 */
	private List<DlNavBannerDTO> getDlNavBannerDTO(HallParam hallParam) {
		//查询交易版还是资讯版
		String isTransaction = ProjectConstant.DEAL_VERSION;//默认交易版
		StrParam strParam = new StrParam();
		BaseResult<SwitchConfigDTO> switchConfigDto = iSwitchConfigService.querySwitch(strParam);
		if(switchConfigDto.getCode() == 0) {
			Integer turnOn = switchConfigDto.getData().getTurnOn();
			isTransaction = (turnOn == 1)?ProjectConstant.DEAL_VERSION:ProjectConstant.INFO_VERSION;
		}
		
		List<DlNavBannerDTO> dlNavBannerDTOs = new LinkedList<DlNavBannerDTO>();
		Condition condition = new Condition(LotteryClassify.class);
		condition.setOrderByClause("banner_sort asc");
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("start_time <=", DateUtil.getCurrentTimeLong());
		criteria.andCondition("end_time >", DateUtil.getCurrentTimeLong());
		criteria.andCondition("is_show=", 1);
		criteria.andCondition("show_position=", 0);
		if(isTransaction.equals(ProjectConstant.INFO_VERSION)) {
			criteria.andCondition("is_transaction =",Integer.valueOf(ProjectConstant.INFO_VERSION));
		}
		List<LotteryNavBanner> lotteryNavBanners = lotteryNavBannerMapper.selectByCondition(condition);
		if (CollectionUtils.isNotEmpty(lotteryNavBanners)) {
			Integer userId = SessionUtil.getUserId();
			String isLogin = "&isLogin=0";
			if(userId != null) {
				isLogin = "&isLogin=1";
			}
			for (LotteryNavBanner lotteryNavBanner : lotteryNavBanners) {
				DlNavBannerDTO dlNavBannerDTO = new DlNavBannerDTO();
				dlNavBannerDTO.setBannerName(lotteryNavBanner.getBannerName());
				dlNavBannerDTO.setBannerImage(lotteryConfig.getBannerShowUrl() + lotteryNavBanner.getBannerImage());
				if ("1".equals(lotteryNavBanner.getBannerParam())) {
					dlNavBannerDTO.setBannerLink(lotteryConfig.getBanneLinkArticleUrl() + lotteryNavBanner.getBannerLink());// 资讯链接,后面跟资讯链接
				} else if ("2".equals(lotteryNavBanner.getBannerParam())) {
					dlNavBannerDTO.setBannerLink(lotteryConfig.getBanneLinkMatchUrl() + lotteryNavBanner.getBannerLink()); // 赛事链接,后面跟赛事ID
				} else {
					String bannerLink = lotteryNavBanner.getBannerLink();
					bannerLink = bannerLink + isLogin;
					dlNavBannerDTO.setBannerLink(bannerLink);// 活动链接,后面跟活动URL
				}
				dlNavBannerDTO.setStartTime(lotteryNavBanner.getStartTime());
				dlNavBannerDTO.setEndTime(lotteryNavBanner.getEndTime());
				dlNavBannerDTOs.add(dlNavBannerDTO);
			}
		}
		return dlNavBannerDTOs;
	}

	/**
	 * 获取活动数据,此活动暂时不展示
	 * 
	 * @return
	 */
	private DlActivityDTO getDlActivityDTO(HallParam hallParam) {
		DlActivityDTO dlActivityDTO = new DlActivityDTO();
		String isTransaction = hallParam.getIsTransaction();
		if("2".equals(isTransaction) || null == isTransaction) {
			Condition condition = new Condition(LotteryActivity.class);
			condition.createCriteria().andCondition("act_type=", 0).andCondition("is_finish=", 0).andCondition("status=", 1).andGreaterThan("endTime", DateUtil.getCurrentTimeLong()).andLessThanOrEqualTo("startTime", DateUtil.getCurrentTimeLong());
			List<LotteryActivity> lotteryActivitys = lotteryActivityMapper.selectByCondition(condition);
			if (CollectionUtils.isNotEmpty(lotteryActivitys)) {
				LotteryActivity lotteryActivity = lotteryActivitys.get(0);
				if (null != lotteryActivity) {
					dlActivityDTO.setActTitle(lotteryActivity.getActTitle());
					dlActivityDTO.setActImg(lotteryActivity.getActImg());
					dlActivityDTO.setActUrl(lotteryActivity.getActUrl());
				}
			}else {
				return null;
			}
		}
		return dlActivityDTO;
	}

	/**
	 * 获取中奖信息列表
	 * 
	 * @return
	 */
	private List<DlWinningLogDTO> getDlWinningLogDTOs() {
		List<DlWinningLogDTO> dlWinningLogDTOs = new ArrayList<DlWinningLogDTO>();
        UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
		String plat = userDeviceInfo.getPlat();
		String channel = userDeviceInfo.getChannel();
		String version = userDeviceInfo.getAppv();
		if(!StringUtils.isEmpty(plat) && !StringUtils.isEmpty(version)){
			if(("android".equals(plat) && channel.compareTo("c28000") >= 0) || ("iphone".equals(plat) && channel.compareTo("c30000") >= 0) || ("h5".equals(plat) && version.compareTo("2.1.1") > 0)){
				log.info("球多多展示比赛信息");
				List<LotteryMatch> latestMatchs = lotteryMatchService .queryLatest3Match();
				if (CollectionUtils.isNotEmpty(latestMatchs)) {
					for (LotteryMatch match : latestMatchs) {
						DlWinningLogDTO dlWinningLogDTO = new DlWinningLogDTO();
						String msg = match.getHomeTeamAbbr() +" VS " + match.getVisitingTeamAbbr() + " "+ DateUtil.toStringDateByFormat(match.getMatchTime(),"yyyy-MM-dd HH:mm");
						dlWinningLogDTO.setWinningMsg(msg);
						dlWinningLogDTOs.add(dlWinningLogDTO);
					}
				}
			}else{
				Condition condition = new Condition(LotteryWinningLogTemp.class);
				condition.createCriteria().andCondition("is_show=", 1);
				List<LotteryWinningLogTemp> lotteryWinningLogTemps = lotteryWinningLogTempMapper.selectByCondition(condition);
				if (CollectionUtils.isNotEmpty(lotteryWinningLogTemps)) {
					for (LotteryWinningLogTemp winningLog : lotteryWinningLogTemps) {
						DlWinningLogDTO dlWinningLogDTO = new DlWinningLogDTO();
						String phone = winningLog.getPhone();
						if(org.apache.commons.lang3.StringUtils.isBlank(phone)) {
							continue;
						}
						phone = phone.substring(0, 3) + "****" + phone.substring(7);
						dlWinningLogDTO.setWinningMsg(MessageFormat.format(ProjectConstant.FORMAT_WINNING_MSG, phone));
						dlWinningLogDTO.setWinningMoney(winningLog.getWinningMoney().toString());
						dlWinningLogDTOs.add(dlWinningLogDTO);
					}
				}
			}
		}else{
			Condition condition = new Condition(LotteryWinningLogTemp.class);
			condition.createCriteria().andCondition("is_show=", 1);
			List<LotteryWinningLogTemp> lotteryWinningLogTemps = lotteryWinningLogTempMapper.selectByCondition(condition);
			if (CollectionUtils.isNotEmpty(lotteryWinningLogTemps)) {
				for (LotteryWinningLogTemp winningLog : lotteryWinningLogTemps) {
					DlWinningLogDTO dlWinningLogDTO = new DlWinningLogDTO();
					String phone = winningLog.getPhone();
					if(org.apache.commons.lang3.StringUtils.isBlank(phone)) {
						continue;
					}
					phone = phone.substring(0, 3) + "****" + phone.substring(7);
					dlWinningLogDTO.setWinningMsg(MessageFormat.format(ProjectConstant.FORMAT_WINNING_MSG, phone));
					dlWinningLogDTO.setWinningMoney(winningLog.getWinningMoney().toString());
					dlWinningLogDTOs.add(dlWinningLogDTO);
				}
			}
		}

		return dlWinningLogDTOs;
	}

	/**
	 * 获取彩票分类列表
	 * 
	 * @return
	 */
	/*private List<DlLotteryClassifyDTO> getDlLotteryClassifyDTOs() {
		List<DlLotteryClassifyDTO> dlLotteryClassifyDTOs = new LinkedList<DlLotteryClassifyDTO>();
		Condition condition = new Condition(LotteryClassify.class);
		condition.createCriteria().andCondition("is_show=", 1);
		condition.setOrderByClause("sort asc,create_time desc");
		List<LotteryClassify> lotteryClassifys = lotteryClassifyMapper.selectByCondition(condition);
		if (CollectionUtils.isNotEmpty(lotteryClassifys)) {
			for (LotteryClassify lotteryClassify : lotteryClassifys) {
				DlLotteryClassifyDTO dlLotteryClassifyDTO = new DlLotteryClassifyDTO();
				dlLotteryClassifyDTO.setLotteryId(lotteryClassify.getLotteryClassifyId().toString());
				dlLotteryClassifyDTO.setLotteryName(lotteryClassify.getLotteryName());
				dlLotteryClassifyDTO.setLotteryImg(lotteryClassify.getLotteryImg());
				dlLotteryClassifyDTO.setStatus(lotteryClassify.getStatus());
				dlLotteryClassifyDTOs.add(dlLotteryClassifyDTO);
			}
		}
		return dlLotteryClassifyDTOs;
	}*/
}

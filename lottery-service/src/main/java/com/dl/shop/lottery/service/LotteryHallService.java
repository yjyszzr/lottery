package com.dl.shop.lottery.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
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
import com.dl.member.api.IUserService;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryActivityMapper;
import com.dl.shop.lottery.dao.LotteryClassifyMapper;
import com.dl.shop.lottery.dao.LotteryNavBannerMapper;
import com.dl.shop.lottery.dao.LotteryPlayClassifyMapper;
import com.dl.shop.lottery.dao.LotteryWinningLogTempMapper;
import com.dl.shop.lottery.model.LotteryClassify;
import com.dl.shop.lottery.model.LotteryNavBanner;
import com.dl.shop.lottery.model.LotteryWinningLogTemp;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Transactional(value = "transactionManager1")
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
		dlHallDTO.setActivity(getDlActivityDTO(userId));
		// 获取中奖信息列表
		dlHallDTO.setWinningMsgs(getDlWinningLogDTOs());
		// 获取彩票分类列表
		// dlHallDTO.setLotteryClassifys(getDlLotteryClassifyDTOs());
		// //第一版只显示竞彩足球的子列表
		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		boolean isShowWorldCup = true;
		String channel = null;
		if(userDevice != null) {
			channel = userDevice.getChannel();
			String appv = userDevice.getAppv();
			if(appv.compareTo("1.0.4") <= 0) {
				isShowWorldCup = false;
			}
		}
		List<DlPlayClassifyDetailDTO> dlPlayClassifyDetailDTOs = lotteryPlayClassifyMapper.selectAllData(1);
		if (CollectionUtils.isNotEmpty(dlPlayClassifyDetailDTOs)) {
			DlPlayClassifyDetailDTO wcDTO = null;
			for (DlPlayClassifyDetailDTO dto : dlPlayClassifyDetailDTOs) {
				dto.setLotteryId("1");
				if("8".equals(dto.getPlayClassifyId())) {
					String redirectUrl = dto.getRedirectUrl();
					channel = channel == null?"":channel;
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
	 * 获取活动数据
	 * 
	 * @return
	 */
	private List<DlNavBannerDTO> getDlNavBannerDTO(HallParam hallParam) {
		List<DlNavBannerDTO> dlNavBannerDTOs = new LinkedList<DlNavBannerDTO>();
		Condition condition = new Condition(LotteryClassify.class);
		condition.setOrderByClause("banner_sort asc");
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("start_time <=", DateUtil.getCurrentTimeLong());
		criteria.andCondition("end_time >", DateUtil.getCurrentTimeLong());
		criteria.andCondition("is_show=", 1);
		criteria.andCondition("show_position=", 0);
		if(!StringUtils.isEmpty(hallParam.getIsTransaction()) && hallParam.getIsTransaction().equals(ProjectConstant.INFO_VERSION)) {//资讯版仅仅展示资讯版，交易版全部展示
			criteria.andCondition("is_transaction =",Integer.valueOf(ProjectConstant.INFO_VERSION));
		}
		
		List<LotteryNavBanner> lotteryNavBanners = lotteryNavBannerMapper.selectByCondition(condition);
		if (CollectionUtils.isNotEmpty(lotteryNavBanners)) {
			for (LotteryNavBanner lotteryNavBanner : lotteryNavBanners) {
				DlNavBannerDTO dlNavBannerDTO = new DlNavBannerDTO();
				dlNavBannerDTO.setBannerName(lotteryNavBanner.getBannerName());
				dlNavBannerDTO.setBannerImage(lotteryConfig.getBannerShowUrl() + lotteryNavBanner.getBannerImage());
				if ("1".equals(lotteryNavBanner.getBannerParam())) {
					dlNavBannerDTO.setBannerLink(lotteryConfig.getBanneLinkArticleUrl() + lotteryNavBanner.getBannerLink());// 资讯链接,后面跟资讯链接
				} else if ("2".equals(lotteryNavBanner.getBannerParam())) {
					dlNavBannerDTO.setBannerLink(lotteryConfig.getBanneLinkMatchUrl() + lotteryNavBanner.getBannerLink()); // 赛事链接,后面跟赛事ID
				} else {
					dlNavBannerDTO.setBannerLink(lotteryNavBanner.getBannerLink());// 活动链接,后面跟活动URL
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
	private DlActivityDTO getDlActivityDTO(Integer userId) {
		return null;
		//该用户与店员绑定过才展示活动数据
//		DlActivityDTO dlActivityDTO = new DlActivityDTO();
//		if(null == userId) {
//			return null;
//		}else {
//			UserIdParam userIdParam = new UserIdParam();
//			userIdParam.setUserId(userId);
//			BaseResult<ChannelCustomerBindDTO> channelDistributorDTORst = userService.queryChannelDistributorByUserId(userIdParam);
//			if(channelDistributorDTORst.getCode() != 0) {
//				return null;
//			}
//			
//			Condition condition = new Condition(LotteryActivity.class);
//			condition.createCriteria().andCondition("is_finish=", 0).andCondition("status=", 1).andGreaterThan("endTime", DateUtil.getCurrentTimeLong()).andLessThanOrEqualTo("startTime", DateUtil.getCurrentTimeLong());
//			List<LotteryActivity> lotteryActivitys = lotteryActivityMapper.selectByCondition(condition);
//			if (CollectionUtils.isNotEmpty(lotteryActivitys)) {
//				LotteryActivity lotteryActivity = lotteryActivitys.get(0);
//				if (null != lotteryActivity) {
//					dlActivityDTO.setActTitle(lotteryActivity.getActTitle());
//					dlActivityDTO.setActImg(lotteryActivity.getActImg());
//					dlActivityDTO.setActUrl(lotteryActivity.getActUrl());
//				}
//			}else {
//				return null;
//			}
//		}
//		
//		return dlActivityDTO;
	}

	/**
	 * 获取中奖信息列表
	 * 
	 * @return
	 */
	private List<DlWinningLogDTO> getDlWinningLogDTOs() {
		Condition condition = new Condition(LotteryWinningLogTemp.class);
		condition.createCriteria().andCondition("is_show=", 1);
		List<LotteryWinningLogTemp> lotteryWinningLogTemps = lotteryWinningLogTempMapper.selectByCondition(condition);
		List<DlWinningLogDTO> dlWinningLogDTOs = new ArrayList<DlWinningLogDTO>();
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
		return dlWinningLogDTOs;
	}

	/**
	 * 获取彩票分类列表
	 * 
	 * @return
	 */
	private List<DlLotteryClassifyDTO> getDlLotteryClassifyDTOs() {
		List<DlLotteryClassifyDTO> dlLotteryClassifyDTOs = new LinkedList<DlLotteryClassifyDTO>();
		Condition condition = new Condition(LotteryClassify.class);
		condition.createCriteria().andCondition("is_show=", 1);
		condition.setOrderByClause("sort asc,create_time desc");
		List<LotteryClassify> lotteryClassifys = lotteryClassifyMapper.selectByCondition(condition);
		if (CollectionUtils.isNotEmpty(lotteryClassifys)) {
			for (LotteryClassify lotteryClassify : lotteryClassifys) {
				DlLotteryClassifyDTO dlLotteryClassifyDTO = new DlLotteryClassifyDTO();
				dlLotteryClassifyDTO.setLotteryId(lotteryClassify.getLotteryClassifyId());
				dlLotteryClassifyDTO.setLotteryName(lotteryClassify.getLotteryName());
				dlLotteryClassifyDTO.setLotteryImg(lotteryClassify.getLotteryImg());
				dlLotteryClassifyDTO.setStatus(lotteryClassify.getStatus());
				dlLotteryClassifyDTOs.add(dlLotteryClassifyDTO);
			}
		}
		return dlLotteryClassifyDTOs;
	}
}

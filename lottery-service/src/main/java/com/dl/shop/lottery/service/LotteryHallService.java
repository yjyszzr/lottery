package com.dl.shop.lottery.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.dl.base.util.DateUtil;
import com.dl.lottery.dto.DlHallDTO;
import com.dl.lottery.dto.DlHallDTO.DlActivityDTO;
import com.dl.lottery.dto.DlHallDTO.DlLotteryClassifyDTO;
import com.dl.lottery.dto.DlHallDTO.DlNavBannerDTO;
import com.dl.lottery.dto.DlHallDTO.DlWinningLogDTO;
import com.dl.lottery.dto.DlPlayClassifyDTO;
import com.dl.lottery.dto.DlPlayClassifyDTO.DlPlayTitleDTO;
import com.dl.lottery.dto.DlPlayClassifyDetailDTO;
import com.dl.lottery.param.DlPlayClassifyParam;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryActivityMapper;
import com.dl.shop.lottery.dao.LotteryClassifyMapper;
import com.dl.shop.lottery.dao.LotteryNavBannerMapper;
import com.dl.shop.lottery.dao.LotteryPlayClassifyMapper;
import com.dl.shop.lottery.dao.LotteryWinningLogTempMapper;
import com.dl.shop.lottery.model.LotteryActivity;
import com.dl.shop.lottery.model.LotteryClassify;
import com.dl.shop.lottery.model.LotteryNavBanner;
import com.dl.shop.lottery.model.LotteryWinningLogTemp;

import tk.mybatis.mapper.entity.Condition;

@Service
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
	
	/**
	 * 获取彩票大厅数据
	 * @return
	 */
	public DlHallDTO getHallData() {
		DlHallDTO dlHallDTO = new DlHallDTO();
		//获取首页轮播图列表
		dlHallDTO.setNavBanners(getDlNavBannerDTO());
		//获取活动数据
		dlHallDTO.setActivity(getDlActivityDTO());
		//获取中奖信息列表
		dlHallDTO.setWinningMsgs(getDlWinningLogDTOs());
		//获取彩票分类列表
		//dlHallDTO.setLotteryClassifys(getDlLotteryClassifyDTOs()); //第一版只显示竞彩足球的子列表
		List<DlPlayClassifyDetailDTO> dlPlayClassifyDetailDTOs = lotteryPlayClassifyMapper.selectAllData(1);
		dlHallDTO.setDlPlayClassifyDetailDTOs(dlPlayClassifyDetailDTOs);
        return dlHallDTO;		
	}
	
	/**
	 * 获取彩票玩法列表
	 * @return
	 */
	public DlPlayClassifyDTO getPlayClassifyList(DlPlayClassifyParam param) {
		DlPlayClassifyDTO dlPlayClassifyDTO = new DlPlayClassifyDTO();
		DlPlayTitleDTO dlPlayTitleDTO = new DlPlayTitleDTO();
		LotteryClassify lotteryClassify = lotteryClassifyMapper.selectByPrimaryKey(Integer.valueOf(param.getLotteryClassifyId()));
		if(null != lotteryClassify) {
			dlPlayTitleDTO.setPlayName(lotteryClassify.getLotteryName());
		}
		dlPlayClassifyDTO.setDlPlayTitleDTO(dlPlayTitleDTO);
		List<DlPlayClassifyDetailDTO> dlPlayClassifyDetailDTOs = lotteryPlayClassifyMapper.selectAllData(Integer.valueOf(param.getLotteryClassifyId()));
		dlPlayClassifyDTO.setDlPlayClassifyDetailDTOs(dlPlayClassifyDetailDTOs);
		return dlPlayClassifyDTO;
	}
	
	/**
	 * 获取活动数据
	 * @return
	 */
	private List<DlNavBannerDTO> getDlNavBannerDTO() {
		List<DlNavBannerDTO> dlNavBannerDTOs = new LinkedList<DlNavBannerDTO>();
		Condition condition = new Condition(LotteryClassify.class);
        condition.createCriteria().andCondition("is_show=", 1);
        condition.setOrderByClause("banner_sort asc");
		List<LotteryNavBanner> lotteryNavBanners = lotteryNavBannerMapper.selectByCondition(condition);
		if(CollectionUtils.isNotEmpty(lotteryNavBanners)) {
			for(LotteryNavBanner lotteryNavBanner : lotteryNavBanners) {
				DlNavBannerDTO dlNavBannerDTO = new DlNavBannerDTO();
				dlNavBannerDTO.setBannerName(lotteryNavBanner.getBannerName());
				dlNavBannerDTO.setBannerImage(lotteryNavBanner.getBannerImage());
				dlNavBannerDTO.setBannerLink(lotteryNavBanner.getBannerLink() + "?cxmxc=scm&" + lotteryNavBanner.getBannerParam());
				dlNavBannerDTOs.add(dlNavBannerDTO);
			}
		}
		return dlNavBannerDTOs;
	}
	
	/**
	 * 获取活动数据
	 * @return
	 */
	private DlActivityDTO getDlActivityDTO() {
		DlActivityDTO dlActivityDTO = new DlActivityDTO();
		Condition condition = new Condition(LotteryActivity.class);
        condition.createCriteria().andCondition("is_finish=", 0).andCondition("status=", 1)
                 .andGreaterThan("endTime", DateUtil.getCurrentTimeLong())
                 .andLessThanOrEqualTo("startTime", DateUtil.getCurrentTimeLong());
		List<LotteryActivity> lotteryActivitys = lotteryActivityMapper.selectByCondition(condition);
		if(CollectionUtils.isNotEmpty(lotteryActivitys)) {
			LotteryActivity lotteryActivity = lotteryActivitys.get(0);
			if(null != lotteryActivity) {
				dlActivityDTO.setActTitle(lotteryActivity.getActTitle());
				dlActivityDTO.setActImg(lotteryActivity.getActImg());
				dlActivityDTO.setActUrl(lotteryActivity.getActUrl());
			}
		}
		return dlActivityDTO;
	}
	
	/**
	 * 获取中奖信息列表
	 * @return
	 */
	private List<DlWinningLogDTO> getDlWinningLogDTOs() {
		Condition condition = new Condition(LotteryWinningLogTemp.class);
        condition.createCriteria().andCondition("is_show=", 1);
        List<LotteryWinningLogTemp> lotteryWinningLogTemps = lotteryWinningLogTempMapper.selectByCondition(condition);
        List<DlWinningLogDTO> dlWinningLogDTOs = new ArrayList<DlWinningLogDTO>();
        if(CollectionUtils.isNotEmpty(lotteryWinningLogTemps)) {
        	for(LotteryWinningLogTemp winningLog : lotteryWinningLogTemps) {
        		DlWinningLogDTO dlWinningLogDTO = new DlWinningLogDTO();
        		String phone = winningLog.getPhone();
        		phone = phone.substring(0, 3) + "****" + phone.substring(7);
        		dlWinningLogDTO.setWinningMsg(MessageFormat.format(ProjectConstant.FORMAT_WINNING_MSG, phone,
        				winningLog.getWinningMoney().toString()));
        		dlWinningLogDTO.setWinningMoney(winningLog.getWinningMoney().toString());
        		dlWinningLogDTOs.add(dlWinningLogDTO);
        	}
        }
        return dlWinningLogDTOs;
	}
	
	/**
	 * 获取彩票分类列表
	 * @return
	 */
	private List<DlLotteryClassifyDTO> getDlLotteryClassifyDTOs() {
		List<DlLotteryClassifyDTO> dlLotteryClassifyDTOs = new LinkedList<DlLotteryClassifyDTO>();
		Condition condition = new Condition(LotteryClassify.class);
        condition.createCriteria().andCondition("is_show=", 1);
        condition.setOrderByClause("sort asc,create_time desc");
        List<LotteryClassify> lotteryClassifys = lotteryClassifyMapper.selectByCondition(condition);
        if(CollectionUtils.isNotEmpty(lotteryClassifys)) {
        	for(LotteryClassify lotteryClassify : lotteryClassifys) {
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

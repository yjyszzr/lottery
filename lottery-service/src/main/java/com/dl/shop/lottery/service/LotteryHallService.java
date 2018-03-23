package com.dl.shop.lottery.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.dl.dto.DlHallDTO;
import com.dl.dto.DlHallDTO.DlLotteryClassifyDTO;
import com.dl.dto.DlHallDTO.DlWinningLogDTO;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryClassifyMapper;
import com.dl.shop.lottery.dao.LotteryWinningLogTempMapper;
import com.dl.shop.lottery.model.LotteryClassify;
import com.dl.shop.lottery.model.LotteryWinningLogTemp;

import tk.mybatis.mapper.entity.Condition;

@Service
public class LotteryHallService {
	
	@Resource
	private LotteryWinningLogTempMapper lotteryWinningLogTempMapper;
	
	@Resource
	private LotteryClassifyMapper lotteryClassifyMapper;

	/**
	 * 获取彩票大厅数据
	 * @return
	 */
	public DlHallDTO getHallData() {
		DlHallDTO dlHallDTO = new DlHallDTO();
		//获取中奖信息列表
		dlHallDTO.setWinningMsgs(getDlWinningLogDTOs());
		//获取彩票分类列表
		dlHallDTO.setLotteryClassifys(getDlLotteryClassifyDTOs());
        return dlHallDTO;		
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
        		dlLotteryClassifyDTO.setLotteryName(lotteryClassify.getLotteryName());
        		dlLotteryClassifyDTO.setLotteryImg(lotteryClassify.getLotteryImg());
        		dlLotteryClassifyDTO.setStatus(lotteryClassify.getStatus());
        		dlLotteryClassifyDTOs.add(dlLotteryClassifyDTO);
        	}
        }
		return dlLotteryClassifyDTOs;
	}
}

package com.dl.shop.lottery.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.dl.dto.DlHallDTO;
import com.dl.dto.DlHallDTO.DlWinningLogDTO;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryWinningLogTempMapper;
import com.dl.shop.lottery.model.LotteryWinningLogTemp;

import tk.mybatis.mapper.entity.Condition;

@Service
public class LotteryHallService {
	
	@Resource
	private LotteryWinningLogTempMapper lotteryWinningLogTempMapper;

	/**
	 * 获取彩票大厅数据
	 * @param param
	 * @return
	 */
	public DlHallDTO getHallData() {
		DlHallDTO dlHallDTO = new DlHallDTO();
		//获取中奖信息列表
		dlHallDTO.setWinningMsgs(getDlWinningLogDTOs());
        return dlHallDTO;		
	}
	
	/**
	 * 获取中奖信息列表
	 * @return
	 */
	private List<DlWinningLogDTO> getDlWinningLogDTOs(){
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
}

package com.dl.shop.lottery.service;

import org.springframework.stereotype.Service;

import com.dl.dto.DlHallDTO;
import com.dl.param.DlHallParam;

@Service
public class LotteryHallService {

	/**
	 * 获取彩票大厅数据
	 * @param param
	 * @return
	 */
	public DlHallDTO getHallData(DlHallParam param) {
		DlHallDTO dlHallDTO = new DlHallDTO();
        return dlHallDTO;		
	}
}

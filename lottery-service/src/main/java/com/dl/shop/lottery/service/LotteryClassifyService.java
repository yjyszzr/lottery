package com.dl.shop.lottery.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao.LotteryClassifyMapper;
import com.dl.shop.lottery.model.LotteryClassify;

@Service
@Transactional(value = "transactionManager1")
public class LotteryClassifyService extends AbstractService<LotteryClassify> {
	@Resource
	private LotteryClassifyMapper lotteryClassifyMapper;

	public List<LotteryClassify> selectAllLotteryClasses() {
		return lotteryClassifyMapper.selectAllLotteryClasses();
	}

}

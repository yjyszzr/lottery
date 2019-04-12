package com.dl.shop.lottery.service;

import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao.LotteryClassifyMapper;
import com.dl.shop.lottery.model.LotteryClassify;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(value = "transactionManager1")
public class LotteryClassifyService extends AbstractService<LotteryClassify> {
	@Resource
	private LotteryClassifyMapper lotteryClassifyMapper;

	public List<LotteryClassify> selectAllLotteryClasses(Integer appCodeName) {
		return lotteryClassifyMapper.selectAllLotteryClasses(appCodeName);
	}

}

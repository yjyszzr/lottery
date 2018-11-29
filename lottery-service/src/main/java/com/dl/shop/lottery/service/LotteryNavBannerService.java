package com.dl.shop.lottery.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao.LotteryNavBannerMapper;
import com.dl.shop.lottery.model.LotteryNavBanner;

@Service
@Transactional(value = "transactionManager1")
public class LotteryNavBannerService extends AbstractService<LotteryNavBanner> {
	@Resource
	private LotteryNavBannerMapper lotteryNavBannerMapper;

	public List<LotteryNavBanner> selectAll() {
		return lotteryNavBannerMapper.selectAll();
	}

	/**
	 * 根据显示位置查找图片
	 * @param showPosition
	 * @return
	 */
	public List<LotteryNavBanner> queryNavBannerByType(Integer showPosition){
		return  lotteryNavBannerMapper.queryNavBannerByType(showPosition);
	}

}

package com.dl.shop.lottery.dao;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.LotteryNavBanner;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface LotteryNavBannerMapper extends Mapper<LotteryNavBanner> {

    List<LotteryNavBanner> queryNavBannerByType(@Param("showPosition") Integer showPosition);

}
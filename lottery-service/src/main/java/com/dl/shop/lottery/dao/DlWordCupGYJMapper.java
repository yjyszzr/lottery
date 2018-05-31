package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlWordCupGYJ;

public interface DlWordCupGYJMapper extends Mapper<DlWordCupGYJ> {

	List<DlWordCupGYJ> getMatchList(@Param("issue") String issue, @Param("countryIds")String countryIds);
}
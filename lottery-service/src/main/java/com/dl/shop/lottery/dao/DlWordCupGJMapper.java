package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlWordCupGJ;

public interface DlWordCupGJMapper extends Mapper<DlWordCupGJ> {
	
	public List<DlWordCupGJ> getMatchList(@Param("issue") String issue);
}
package com.dl.shop.lottery.dao2;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlSeason500w;

public interface DlSeason500wMapper extends Mapper<DlSeason500w> {

	List<DlSeason500w> findAllSeason();
}
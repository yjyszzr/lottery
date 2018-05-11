package com.dl.shop.lottery.dao2;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlMatchSupport;

public interface DlMatchSupportMapper extends Mapper<DlMatchSupport> {

	DlMatchSupport getByChangciIdAndPlayType(@Param("changciId")Integer changciId, @Param("playType")int playType);
}
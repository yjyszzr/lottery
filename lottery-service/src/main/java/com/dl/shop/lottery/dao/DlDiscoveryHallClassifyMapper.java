package com.dl.shop.lottery.dao;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlDiscoveryHallClassify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DlDiscoveryHallClassifyMapper extends Mapper<DlDiscoveryHallClassify> {

    List<DlDiscoveryHallClassify> queryDiscoveryListByType(@Param("typeList") List<Integer> typeList, @Param("appCodeName") Integer appCodeName, @Param("isTransaction") Integer isTransaction);

}
package com.dl.shop.lottery.dao2;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlResultBasketball;

public interface DlResultBasketballMapper extends Mapper<DlResultBasketball> {
	List<DlResultBasketball> queryMatchResultsByChangciIds(@Param("changciIds")List<Integer> changciIds);
}
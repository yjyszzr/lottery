package com.dl.shop.lottery.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlSuperLotto;
import com.dl.shop.lottery.model.DlSuperLottoReward;

public interface DlSuperLottoMapper extends Mapper<DlSuperLotto> {

	DlSuperLotto getLastNumLottos(int i);

	List<DlSuperLottoReward> findByTermNum(@Param("termNum") Integer termNum);
}
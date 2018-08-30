package com.dl.shop.lottery.dao2;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlSuperLotto;

public interface DlSuperLottoMapper extends Mapper<DlSuperLotto> {

	DlSuperLotto getLastNumLottos(int i);
}
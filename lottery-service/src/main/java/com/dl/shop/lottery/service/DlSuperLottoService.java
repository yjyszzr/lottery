package com.dl.shop.lottery.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao2.DlSuperLottoMapper;
import com.dl.shop.lottery.model.DlSuperLotto;

@Service
@Transactional(value = "transactionManager2")
public class DlSuperLottoService extends AbstractService<DlSuperLotto> {
	@Resource
	private DlSuperLottoMapper dlSuperLottoMapper;

	public DlSuperLotto getLastNumLottos(int i) {
		return dlSuperLottoMapper.getLastNumLottos(i);
	}

}

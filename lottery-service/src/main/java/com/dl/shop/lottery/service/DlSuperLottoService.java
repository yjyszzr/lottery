package com.dl.shop.lottery.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.shop.lottery.dao2.DlSuperLottoMapper;
import com.dl.shop.lottery.model.DlSuperLotto;
import com.dl.shop.lottery.model.DlSuperLottoReward;

@Service
@Transactional(value = "transactionManager2")
public class DlSuperLottoService extends AbstractService<DlSuperLotto> {
	@Resource
	private DlSuperLottoMapper dlSuperLottoMapper;

	public DlSuperLotto getLastNumLottos(int i) {
		return dlSuperLottoMapper.getLastNumLottos(i);
	}

	public List<DlSuperLottoReward> findByTermNum(Integer termNum) {
		return dlSuperLottoMapper.findByTermNum(termNum);
	}

}

package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.DlMatchPlayBasketball;
import com.dl.shop.lottery.dao2.DlMatchPlayBasketballMapper;
import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.LeagueInfoDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional(value="transactionManager2")
public class DlMatchPlayBasketballService extends AbstractService<DlMatchPlayBasketball> {
    @Resource
    private DlMatchPlayBasketballMapper dlMatchPlayBasketballMapper;
    
    

}

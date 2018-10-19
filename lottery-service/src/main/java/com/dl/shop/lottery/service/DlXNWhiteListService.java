package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.DlXNWhiteList;
import com.dl.shop.lottery.dao.DlXNWhiteListMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlXNWhiteListService extends AbstractService<DlXNWhiteList> {
    @Resource
    private DlXNWhiteListMapper dlXNWhiteListMapper;

}

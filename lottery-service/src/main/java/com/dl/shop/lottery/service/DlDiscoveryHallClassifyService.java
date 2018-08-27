package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.DlDiscoveryHallClassify;
import com.dl.shop.lottery.dao.DlDiscoveryHallClassifyMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlDiscoveryHallClassifyService extends AbstractService<DlDiscoveryHallClassify> {
    @Resource
    private DlDiscoveryHallClassifyMapper dlDiscoveryHallClassifyMapper;

}

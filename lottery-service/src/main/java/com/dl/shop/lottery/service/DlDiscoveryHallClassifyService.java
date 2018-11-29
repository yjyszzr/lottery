package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.DlDiscoveryHallClassify;
import com.dl.shop.lottery.dao.DlDiscoveryHallClassifyMapper;
import com.dl.base.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(value = "transactionManager1")
@Slf4j
public class DlDiscoveryHallClassifyService extends AbstractService<DlDiscoveryHallClassify> {
    @Resource
    private DlDiscoveryHallClassifyMapper dlDiscoveryHallClassifyMapper;

    public List<DlDiscoveryHallClassify> queryDiscoveryListByType(List<Integer> typeList, Integer isTransaction){
       return  dlDiscoveryHallClassifyMapper.queryDiscoveryListByType(typeList,isTransaction);
    }

}

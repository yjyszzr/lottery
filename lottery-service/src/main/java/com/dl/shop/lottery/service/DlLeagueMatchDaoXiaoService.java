package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.LeagueMatchDaoXiaoDTO;
import com.dl.shop.lottery.dao2.DlLeagueMatchDaoXiaoMapper;
import com.dl.shop.lottery.model.DlLeagueMatchDaoXiao;

import tk.mybatis.mapper.entity.Condition;

@Service
@Transactional(value="transactionManager2")
public class DlLeagueMatchDaoXiaoService extends AbstractService<DlLeagueMatchDaoXiao> {
    @Resource
    private DlLeagueMatchDaoXiaoMapper dlLeagueMatchDaoXiaoMapper;

    public List<LeagueMatchDaoXiaoDTO> leagueMatchDaoXiaos(Integer changciId) {
        List<DlLeagueMatchDaoXiao> list = dlLeagueMatchDaoXiaoMapper.getAllByChangciId(changciId);
        List<LeagueMatchDaoXiaoDTO> dtos = new ArrayList<LeagueMatchDaoXiaoDTO>(0);
        if(null != list) {
        	dtos = new ArrayList<LeagueMatchDaoXiaoDTO>(list.size());
        	for(DlLeagueMatchDaoXiao daoxiao: list) {
        		LeagueMatchDaoXiaoDTO dto = new LeagueMatchDaoXiaoDTO();
        		dto.setChangciId(changciId);
        		dto.setComName(daoxiao.getComName());
        		dto.setInitDraw(daoxiao.getInitDraw());
        		dto.setInitLose(daoxiao.getInitLose());
        		dto.setInitWin(daoxiao.getInitWin());
        		dto.setRealDraw(daoxiao.getRealDraw());
        		dto.setRealLose(daoxiao.getRealLose());
        		dto.setRealWin(daoxiao.getRealWin());
        		dto.setDaoxiaoId(daoxiao.getDaoxiaoId());
        		dto.setInitTime(daoxiao.getInitTime());
        		dto.setRealTime(daoxiao.getRealTime());
        		dtos.add(dto);
        	}
        }
		return dtos;
    }
}

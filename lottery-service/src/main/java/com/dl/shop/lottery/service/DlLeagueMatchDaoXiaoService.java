package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.base.util.StringHelper;
import com.dl.lottery.dto.LeagueMatchDaoXiaoDTO;
import com.dl.shop.lottery.dao2.DlLeagueMatchDaoXiaoMapper;
import com.dl.shop.lottery.model.DlLeagueMatchDaoXiao;

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
        		dto.setRealDraw(StringHelper.replaceChangeStr(daoxiao.getRealDraw()));
        		dto.setRealLose(StringHelper.replaceChangeStr(daoxiao.getRealLose()));
        		dto.setRealWin(StringHelper.replaceChangeStr(daoxiao.getRealWin()));
        		dto.setDaoxiaoId(daoxiao.getDaoxiaoId());
        		dto.setInitTime(daoxiao.getInitTime());
        		dto.setRealTime(daoxiao.getRealTime());
        		dto.setDrawChange(this.showChange(daoxiao.getRealDraw()));
        		dto.setLoseChange(this.showChange(daoxiao.getRealLose()));
        		dto.setWinChange(this.showChange(daoxiao.getRealWin()));
        		dtos.add(dto);
        	}
        }
		return dtos;
    }
    
    /**
     * 胜变化趋势:0equal,1up,2down
     * @param oldStr
     * @return
     */
    public Integer showChange(String oldStr) {
    	if(oldStr.contains("↑")) {
    		return 1;
    	}else if(oldStr.contains("↓")) {
    		return 2;
    	}else {
    		return 0;
    	}
    }
}

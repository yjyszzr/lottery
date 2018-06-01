package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DlWordCupGJDTO;
import com.dl.shop.lottery.dao2.DlWordCupGJMapper;
import com.dl.shop.lottery.model.DlWordCupGJ;

@Service
public class DlWordCupGJService extends AbstractService<DlWordCupGJ> {
    @Resource
    private DlWordCupGJMapper dlWordCupGJMapper;

	public List<DlWordCupGJDTO> getMatchList(String issue) {
		List<DlWordCupGJDTO> dtos = new ArrayList<DlWordCupGJDTO>(0);
		List<DlWordCupGJ> matchList = dlWordCupGJMapper.getMatchList(issue);
		if(CollectionUtils.isNotEmpty(matchList)) {
			dtos = new ArrayList<DlWordCupGJDTO>(matchList.size());
			for(DlWordCupGJ gj: matchList) {
				DlWordCupGJDTO dto = new DlWordCupGJDTO();
				dto.setBetOdds(gj.getBetOdds());
				dto.setContryName(gj.getContryName());
				dto.setContryPic(gj.getContryPic());
				dto.setCountryId(gj.getCountryId());
				dto.setGame(gj.getGame());
				dto.setIssue(gj.getIssue());
				dto.setSortId(gj.getSortId());
				dto.setGjId(gj.getId());
    			dto.setBetStatus(gj.getBetStatus());
    			dto.setIsSell(gj.getIsSell());
				dtos.add(dto);
			}
		}
		return dtos;
	}

}

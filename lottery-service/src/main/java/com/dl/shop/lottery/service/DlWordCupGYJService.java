package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DlWordCupGYJDTO;
import com.dl.shop.lottery.dao2.DlWordCupGYJMapper;
import com.dl.shop.lottery.model.DlWordCupGYJ;

@Service
public class DlWordCupGYJService extends AbstractService<DlWordCupGYJ> {
    @Resource
    private DlWordCupGYJMapper dlWordCupGYJMapper;

    public List<DlWordCupGYJDTO> getMatchList(String issue, String countryIds) {
    	List<DlWordCupGYJDTO> dtos = new ArrayList<DlWordCupGYJDTO>(0);
    	List<DlWordCupGYJ> wordcupgyjList = dlWordCupGYJMapper.getMatchList(issue, countryIds);
    	if(CollectionUtils.isNotEmpty(wordcupgyjList)) {
    		dtos = new ArrayList<DlWordCupGYJDTO>(wordcupgyjList.size());
    		for(DlWordCupGYJ gyj: wordcupgyjList) {
    			DlWordCupGYJDTO dto = new DlWordCupGYJDTO();
    			dto.setBetOdds(gyj.getBetOdds());
    			dto.setGame(gyj.getGame());
    			dto.setHomeContryName(gyj.getHomeContryName());
    			dto.setHomeContryPic(gyj.getHomeContryPic());
    			dto.setHomeCountryId(gyj.getHomeCountryId());
    			dto.setIssue(gyj.getIssue());
    			dto.setSortId(gyj.getSortId());
    			dto.setVisitorContryName(gyj.getVisitorContryName());
    			dto.setVisitorContryPic(gyj.getVisitorContryPic());
    			dto.setVisitorCountryId(gyj.getVisitorCountryId());
    			dto.setGyjId(gyj.getId());
    			dtos.add(dto);
    		}
    	}
    	return dtos;
    }
    
}

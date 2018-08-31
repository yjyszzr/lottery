package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DlLeagueContryDTO;
import com.dl.lottery.dto.DlLeagueDetailDTO;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.shop.lottery.dao2.DlLeagueContryMapper;
import com.dl.shop.lottery.dao2.DlLeagueInfoMapper;
import com.dl.shop.lottery.model.DlLeagueContry;
import com.dl.shop.lottery.model.DlLeagueInfo;

@Service
@Transactional(value="transactionManager2")
public class DlLeagueInfoService extends AbstractService<DlLeagueInfo> {
    @Resource
    private DlLeagueInfoMapper dlLeagueInfoMapper;
    
    @Resource
    private DlLeagueContryMapper dlLeagueContryMapper;
    
   /* @Resource
    private DlLeagueGroupMapper dlLeagueGroupMapper;*/

    /**
     * 联赛详情
     * @param leagueId
     */
	public DlLeagueDetailDTO leagueDetail(Integer leagueId) {
		DlLeagueDetailDTO detail = null;
		DlLeagueInfo leagueInfo = dlLeagueInfoMapper.getByLeagueId(leagueId);
		if(leagueInfo == null) {
			return null;
		}
		return detail;
	}
	
	/**
	 * 获取分类下的国家列表
	 * @param groupId
	 * @return
	 */
	public List<DlLeagueContryDTO> contryLeagueListByGroupId(Integer groupId) {
		List<DlLeagueContryDTO> contryDtos = null;
		if(groupId == 0) {
			DlLeagueContryDTO contryDTO = this.getHotLeagues();
			contryDtos = new ArrayList<DlLeagueContryDTO>(1);
			contryDtos.add(contryDTO);
		} else {
			contryDtos = this.leagueContrys(groupId);
		}
		return contryDtos;
	}

	//热门
	private DlLeagueContryDTO getHotLeagues() {
		List<DlLeagueInfo> hotLeagues = dlLeagueInfoMapper.getHotLeagues();
		List<LeagueInfoDTO> leagueInfos = new ArrayList<LeagueInfoDTO>(hotLeagues.size());
		for(DlLeagueInfo league: hotLeagues) {
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(league.getLeagueAddr());
			dto.setLeagueId(league.getLeagueId());
			dto.setLeagueName(league.getLeagueName());
			dto.setLeaguePic(league.getLeaguePic());
			leagueInfos.add(dto);
		}
		DlLeagueContryDTO contryDTO = new DlLeagueContryDTO();
		contryDTO.setContryName("");
		contryDTO.setContryPic("");
		contryDTO.setGroupId(0);
		contryDTO.setLeagueInfoList(leagueInfos);
		return contryDTO;
	}
	//
	private List<DlLeagueContryDTO>  leagueContrys(Integer groupId) {
		List<DlLeagueContryDTO> contryDtos = null;
		List<DlLeagueContry> leagueContrys = dlLeagueContryMapper.getContrysByGroupId(groupId);
		if(CollectionUtils.isEmpty(leagueContrys)) {
			return null;
		}
		List<DlLeagueInfo>  leagueInfos = dlLeagueInfoMapper.getAll();
		if(CollectionUtils.isEmpty(leagueInfos)) {
			return null;
		}
		Map<Integer, List<LeagueInfoDTO>> leagueMap = new HashMap<Integer, List<LeagueInfoDTO>>();
		for(DlLeagueInfo league: leagueInfos) {
			Integer contryId = league.getContryId();
			List<LeagueInfoDTO> leagueInfoDTOs = leagueMap.get(contryId);
			if(leagueInfoDTOs == null) {
				leagueInfoDTOs = new ArrayList<LeagueInfoDTO>(30);
				leagueMap.put(contryId, leagueInfoDTOs);
			}
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(league.getLeagueAddr());
			dto.setLeagueId(league.getLeagueId());
			dto.setLeagueName(league.getLeagueName());
			dto.setLeaguePic(league.getLeaguePic());
			leagueInfoDTOs.add(dto);
		}
		contryDtos = new ArrayList<DlLeagueContryDTO>(leagueContrys.size());
		for(DlLeagueContry contry: leagueContrys) {
			DlLeagueContryDTO dto = new DlLeagueContryDTO();
			dto.setContryName(contry.getContryName());
			dto.setContryPic(contry.getContryPic());
			dto.setGroupId(groupId);
			Integer contryId = contry.getId();
			List<LeagueInfoDTO> leagueInfoList = leagueMap.get(contryId);
			dto.setLeagueInfoList(leagueInfoList);
			contryDtos.add(dto);
		}
		return contryDtos;
	}
	
}

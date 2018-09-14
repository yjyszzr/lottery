package com.dl.shop.lottery.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.result.BaseResult;
import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.DlLeagueContryDTO;
import com.dl.lottery.dto.DlLeagueDetailDTO;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.member.api.ISwitchConfigService;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.param.StrParam;
import com.dl.shop.lottery.dao2.DlLeagueContryMapper;
import com.dl.shop.lottery.dao2.DlLeagueInfoMapper;
import com.dl.shop.lottery.model.DlLeagueContry;
import com.dl.shop.lottery.model.DlLeagueInfo;

@Service
@Transactional(value = "transactionManager2")
public class DlLeagueInfoService extends AbstractService<DlLeagueInfo> {
	@Resource
	private DlLeagueInfoMapper dlLeagueInfoMapper;

	@Resource
	private DlLeagueContryMapper dlLeagueContryMapper;
	
	@Resource
	private ISwitchConfigService iSwitchConfigService;

	/*
	 * @Resource private DlLeagueGroupMapper dlLeagueGroupMapper;
	 */

	public Integer queryTurnOnDealVersion() {
		Integer turnOn = 0;// 1-交易开，0-交易关，默认关
		StrParam strParam = new StrParam();
		strParam.setStr("");
		BaseResult<SwitchConfigDTO> switchConfigDTORst = iSwitchConfigService.querySwitch(strParam);
		if(switchConfigDTORst.getCode() == 0) {
			SwitchConfigDTO switchDto = switchConfigDTORst.getData();
			turnOn = switchDto.getTurnOn();
		}	
		return turnOn;
	}
	
	
	/**
	 * 联赛详情
	 * 
	 * @param leagueId
	 */
	public DlLeagueDetailDTO leagueDetail(Integer leagueId) {
		DlLeagueDetailDTO detail = new DlLeagueDetailDTO();
		DlLeagueInfo leagueInfo = dlLeagueInfoMapper.getByLeagueId(leagueId);
		if (leagueInfo == null) {
			return null;
		} else {
			Integer turnOn = this.queryTurnOnDealVersion();
			detail.setLeaguePic(turnOn == 1?leagueInfo.getLeaguePic():"https://static.caixiaomi.net/foot/league_5/log.png");
			detail.setLeagueAddr(leagueInfo.getLeagueAddr());
			detail.setLeagueId(leagueInfo.getLeagueId());
			detail.setLeagueName(leagueInfo.getLeagueName());
//			detail.setLeaguePic(leagueInfo.getLeaguePic());
			detail.setLeagueRule(leagueInfo.getLeagueRule());
		}
		return detail;
	}

	/**
	 * 获取分类下的国家列表
	 * 
	 * @param groupId
	 * @return
	 */
	public List<DlLeagueContryDTO> contryLeagueListByGroupId(Integer groupId) {
		List<DlLeagueContryDTO> contryDtos = null;
		if (groupId == 0) {
			DlLeagueContryDTO contryDTO = this.getHotLeaguesForLD();
			contryDtos = new ArrayList<DlLeagueContryDTO>(1);
			contryDtos.add(contryDTO);
		} else {
			contryDtos = this.leagueContrys(groupId);
		}
		return contryDtos;
	}

	// 国际赛事
	private DlLeagueContryDTO getInternationalLeagues() {
		List<DlLeagueInfo> hotLeagues = dlLeagueInfoMapper.getInternationalLeagues();
		List<LeagueInfoDTO> leagueInfos = new ArrayList<LeagueInfoDTO>(hotLeagues.size());
		Integer turnOn = this.queryTurnOnDealVersion();
		for (DlLeagueInfo league : hotLeagues) {
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(league.getLeagueAddr());
			dto.setLeagueId(league.getLeagueId());
			dto.setLeagueName(league.getLeagueName());
			dto.setLeaguePic(turnOn == 1?league.getLeaguePic():"https://static.caixiaomi.net/foot/league_5/log.png");
			leagueInfos.add(dto);
		}
		DlLeagueContryDTO contryDTO = new DlLeagueContryDTO();
		contryDTO.setContryName("");
		contryDTO.setContryPic("");
		contryDTO.setGroupId(0);
		contryDTO.setLeagueInfoList(leagueInfos);
		return contryDTO;
	}

	// 热门
	private DlLeagueContryDTO getHotLeagues() {
		List<DlLeagueInfo> hotLeagues = dlLeagueInfoMapper.getHotLeagues();
		List<LeagueInfoDTO> leagueInfos = new ArrayList<LeagueInfoDTO>(hotLeagues.size());
		Integer turnOn = this.queryTurnOnDealVersion();
		for (DlLeagueInfo league : hotLeagues) {
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(league.getLeagueAddr());
			dto.setLeagueId(league.getLeagueId());
			dto.setLeagueName(league.getLeagueName());
			dto.setLeaguePic(turnOn == 1?league.getLeaguePic():"https://static.caixiaomi.net/foot/league_5/log.png");
			leagueInfos.add(dto);
		}
		DlLeagueContryDTO contryDTO = new DlLeagueContryDTO();
		contryDTO.setContryName("");
		contryDTO.setContryPic("");
		contryDTO.setGroupId(0);
		contryDTO.setLeagueInfoList(leagueInfos);
		return contryDTO;
	}

	private DlLeagueContryDTO getHotLeaguesForLD() {
		List<DlLeagueInfo> hotLeagues = dlLeagueInfoMapper.getHotLeaguesForLD();
		List<LeagueInfoDTO> leagueInfos = new ArrayList<LeagueInfoDTO>(hotLeagues.size());
		Integer turnOn = this.queryTurnOnDealVersion();
		for (DlLeagueInfo league : hotLeagues) {
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(league.getLeagueAddr());
			dto.setLeagueId(league.getLeagueId());
			dto.setLeagueName(league.getLeagueName());
			dto.setLeaguePic(turnOn == 1?league.getLeaguePic():"https://static.caixiaomi.net/foot/league_5/log.png");
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
	private List<DlLeagueContryDTO> leagueContrys(Integer groupId) {
		List<DlLeagueContryDTO> contryDtos = null;
		List<DlLeagueContry> leagueContrys = dlLeagueContryMapper.getContrysByGroupId(groupId);
		if (CollectionUtils.isEmpty(leagueContrys)) {
			return null;
		}
		List<DlLeagueInfo> leagueInfos = dlLeagueInfoMapper.getAll();
		if (CollectionUtils.isEmpty(leagueInfos)) {
			return null;
		}
		Map<Integer, List<LeagueInfoDTO>> leagueMap = new HashMap<Integer, List<LeagueInfoDTO>>();
		Integer turnOn = this.queryTurnOnDealVersion();
		for (DlLeagueInfo league : leagueInfos) {
			Integer contryId = league.getContryId();
			List<LeagueInfoDTO> leagueInfoDTOs = leagueMap.get(contryId);
			if (leagueInfoDTOs == null) {
				leagueInfoDTOs = new ArrayList<LeagueInfoDTO>(30);
				leagueMap.put(contryId, leagueInfoDTOs);
			}
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(league.getLeagueAddr());
			dto.setLeagueId(league.getLeagueId());
			dto.setLeagueName(league.getLeagueName());
			dto.setLeaguePic(turnOn == 1?league.getLeaguePic():"https://static.caixiaomi.net/foot/league_5/log.png");
			leagueInfoDTOs.add(dto);
		}
		contryDtos = new ArrayList<DlLeagueContryDTO>(leagueContrys.size());
		for (DlLeagueContry contry : leagueContrys) {
			DlLeagueContryDTO dto = new DlLeagueContryDTO();
			dto.setContryName(contry.getContryName());
			dto.setContryPic(contry.getContryPic());
			if(4 == groupId) {
				dto.setContryPic(turnOn == 1?contry.getContryPic():"https://static.caixiaomi.net/foot/league_5/log.png");
			}
			dto.setGroupId(groupId);
			Integer contryId = contry.getId();
			List<LeagueInfoDTO> leagueInfoList = leagueMap.get(contryId);
			dto.setLeagueInfoList(leagueInfoList);
			contryDtos.add(dto);
		}
		return contryDtos;
	}

	public List<DlLeagueContryDTO> leagueHomePageByGroupId(Integer groupId) {
		List<DlLeagueContryDTO> contryDtos = null;
		if (groupId == 0) {
			DlLeagueContryDTO contryDTO = this.getHotLeagues();
			contryDtos = new ArrayList<DlLeagueContryDTO>(1);
			contryDtos.add(contryDTO);
		} else if (groupId == 4) {
			DlLeagueContryDTO contryDTO = this.getInternationalLeagues();
			contryDtos = new ArrayList<DlLeagueContryDTO>(1);
			contryDtos.add(contryDTO);
		} else {
			contryDtos = this.leagueContrys(groupId);
		}
		return contryDtos;
	}

}

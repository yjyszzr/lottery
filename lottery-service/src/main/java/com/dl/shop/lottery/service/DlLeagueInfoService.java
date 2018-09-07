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
import com.dl.base.util.PinyinUtil;
import com.dl.lottery.dto.DlLeagueContryDTO;
import com.dl.lottery.dto.DlLeagueDetailDTO;
import com.dl.lottery.dto.GroupLeagueDTO;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.shop.lottery.dao2.DlLeagueContryMapper;
import com.dl.shop.lottery.dao2.DlLeagueInfoMapper;
import com.dl.shop.lottery.model.DlLeagueContry;
import com.dl.shop.lottery.model.DlLeagueContry500W;
import com.dl.shop.lottery.model.DlLeagueInfo;
import com.dl.shop.lottery.model.DlLeagueInfo500W;

@Service
@Transactional(value = "transactionManager2")
public class DlLeagueInfoService extends AbstractService<DlLeagueInfo> {
	@Resource
	private DlLeagueInfoMapper dlLeagueInfoMapper;

	@Resource
	private DlLeagueContryMapper dlLeagueContryMapper;

	/*
	 * @Resource private DlLeagueGroupMapper dlLeagueGroupMapper;
	 */

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
			detail.setLeagueAddr(leagueInfo.getLeagueAddr());
			detail.setLeagueId(leagueInfo.getLeagueId());
			detail.setLeagueName(leagueInfo.getLeagueName());
			detail.setLeaguePic(leagueInfo.getLeaguePic());
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
	private List<LeagueInfoDTO> getInternationalLeagues() {
		List<DlLeagueInfo500W> hotLeagues = dlLeagueInfoMapper.getInternationalLeagues();
		List<LeagueInfoDTO> leagueInfos = new ArrayList<LeagueInfoDTO>(hotLeagues.size());
		for (DlLeagueInfo500W league : hotLeagues) {
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(league.getLeagueAbbr());
			dto.setLeagueId(league.getLeagueId());
			dto.setLeagueName(league.getLeagueName());
			dto.setLeaguePic(league.getLeaguePic());
			if (null != league.getLeagueAbbr()) {
				dto.setLeagueInitials(PinyinUtil.ToPinyin(league.getLeagueAbbr()));
			}
			leagueInfos.add(dto);
		}
		/*
		 * DlLeagueContryDTO contryDTO = new DlLeagueContryDTO();
		 * contryDTO.setContryName(""); contryDTO.setContryPic("");
		 */
		// contryDTO.setGroupId(0);
		// contryDTO.setLeagueInfoList(leagueInfos);
		return leagueInfos;
	}

	// 热门
	private List<LeagueInfoDTO> getHotLeagues() {
		List<DlLeagueInfo500W> hotLeagues = dlLeagueInfoMapper.getHotLeagues();
		List<LeagueInfoDTO> leagueInfos = new ArrayList<LeagueInfoDTO>(hotLeagues.size());
		for (DlLeagueInfo500W league : hotLeagues) {
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(league.getLeagueAbbr());
			dto.setLeagueId(league.getLeagueId());
			dto.setLeagueName(league.getLeagueName());
			dto.setLeaguePic(league.getLeaguePic());
			if (null != league.getLeagueAbbr()) {
				dto.setLeagueInitials(PinyinUtil.ToPinyin(league.getLeagueAbbr()));
			}
			leagueInfos.add(dto);
		}
		/*
		 * DlLeagueContryDTO contryDTO = new DlLeagueContryDTO();
		 * contryDTO.setContryName(""); contryDTO.setContryPic("");
		 */
		// contryDTO.setGroupId(0);
		// contryDTO.setLeagueInfoList(leagueInfos);
		return leagueInfos;
	}

	private DlLeagueContryDTO getHotLeaguesForLD() {
		List<DlLeagueInfo> hotLeagues = dlLeagueInfoMapper.getHotLeaguesForLD();
		List<LeagueInfoDTO> leagueInfos = new ArrayList<LeagueInfoDTO>(hotLeagues.size());
		for (DlLeagueInfo league : hotLeagues) {
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
		// contryDTO.setGroupId(0);
		contryDTO.setLeagueInfoList(leagueInfos);
		return contryDTO;
	}

	//
	private List<DlLeagueContryDTO> leagueContrysFrom500W(Integer groupId) {
		List<DlLeagueContryDTO> contryDtos = null;
		List<DlLeagueContry500W> leagueContrys = dlLeagueContryMapper.getContrysFrom500WByGroupId(groupId);
		if (CollectionUtils.isEmpty(leagueContrys)) {
			return null;
		}
		List<DlLeagueInfo500W> leagueInfos = dlLeagueInfoMapper.getLeaguesFrom500WByGroupId(groupId);
		if (CollectionUtils.isEmpty(leagueInfos)) {
			return null;
		}
		Map<Integer, List<LeagueInfoDTO>> leagueMap = new HashMap<Integer, List<LeagueInfoDTO>>();
		for (DlLeagueInfo500W league : leagueInfos) {
			Integer contryId = league.getContryId();
			List<LeagueInfoDTO> leagueInfoDTOs = leagueMap.get(contryId);
			if (leagueInfoDTOs == null) {
				leagueInfoDTOs = new ArrayList<LeagueInfoDTO>(30);
				leagueMap.put(contryId, leagueInfoDTOs);
			}
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(league.getLeagueAbbr());
			if (null != league.getLeagueAbbr()) {
				dto.setLeagueInitials(PinyinUtil.ToPinyin(league.getLeagueAbbr()));
			}
			dto.setLeagueId(league.getLeagueId());
			dto.setLeagueName(league.getLeagueName());
			dto.setLeaguePic(league.getLeaguePic());
			leagueInfoDTOs.add(dto);
		}
		contryDtos = new ArrayList<DlLeagueContryDTO>(leagueContrys.size());
		for (DlLeagueContry500W contry : leagueContrys) {
			DlLeagueContryDTO dto = new DlLeagueContryDTO();
			dto.setContryName(contry.getContryName());
			dto.setContryPic(contry.getContryPic());
			// dto.setGroupId(groupId);
			Integer contryId = contry.getContryId();
			List<LeagueInfoDTO> leagueInfoList = leagueMap.get(contryId);
			dto.setLeagueInfoList(leagueInfoList);
			contryDtos.add(dto);
		}
		return contryDtos;
	}

	// 乐德
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
			dto.setLeaguePic(league.getLeaguePic());
			leagueInfoDTOs.add(dto);
		}
		contryDtos = new ArrayList<DlLeagueContryDTO>(leagueContrys.size());
		for (DlLeagueContry contry : leagueContrys) {
			DlLeagueContryDTO dto = new DlLeagueContryDTO();
			dto.setContryName(contry.getContryName());
			dto.setContryPic(contry.getContryPic());
			// dto.setGroupId(groupId);
			Integer contryId = contry.getId();
			List<LeagueInfoDTO> leagueInfoList = leagueMap.get(contryId);
			dto.setLeagueInfoList(leagueInfoList);
			contryDtos.add(dto);
		}
		return contryDtos;
	}

	public GroupLeagueDTO leagueHomePageByGroupId(Integer groupId) {
		GroupLeagueDTO dto = new GroupLeagueDTO();
		dto.setGroupId(groupId);
		if (groupId == 0) {
			List<LeagueInfoDTO> hotLeagues = this.getHotLeagues();
			dto.setGroupLeagues(hotLeagues);
		} else if (groupId == 4) {
			List<LeagueInfoDTO> leagues = this.getInternationalLeagues();
			dto.setGroupLeagues(leagues);
		} else {
			List<DlLeagueContryDTO> contrys = this.leagueContrysFrom500W(groupId);
			List<LeagueInfoDTO> groupLeagues = this.getCupMatchs(groupId);
			dto.setGroupLeagues(groupLeagues);
			dto.setContrys(contrys);
		}
		return dto;
	}

	private List<LeagueInfoDTO> getCupMatchs(Integer groupId) {
		List<DlLeagueInfo500W> leagueInfo500W = dlLeagueInfoMapper.getCupMatchs(groupId);
		List<LeagueInfoDTO> list = new ArrayList<LeagueInfoDTO>();
		for (int i = 0; i < leagueInfo500W.size(); i++) {
			LeagueInfoDTO leagueInfoDTO = new LeagueInfoDTO();
			leagueInfoDTO.setLeagueAddr(leagueInfo500W.get(i).getLeagueAbbr());
			leagueInfoDTO.setLeagueId(leagueInfo500W.get(i).getLeagueId());
			if (null != leagueInfo500W.get(i).getLeagueAbbr()) {
				leagueInfoDTO.setLeagueInitials(PinyinUtil.ToPinyin(leagueInfo500W.get(i).getLeagueAbbr()));
			}
			leagueInfoDTO.setLeagueName(leagueInfo500W.get(i).getLeagueName());
			leagueInfoDTO.setLeaguePic(leagueInfo500W.get(i).getLeaguePic());
			list.add(leagueInfoDTO);
		}
		return list;
	}

	/**
	 * 联赛详情
	 * 
	 * @param leagueId
	 * @return
	 */
	public DlLeagueInfo500W leagueDetailFrom500w(Integer leagueId) {
		DlLeagueInfo500W leagueInfo = dlLeagueInfoMapper.getLeagueInfo500wByLeagueId(leagueId);
		return leagueInfo;
	}
}

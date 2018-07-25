package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.MatchLineUpInfosDTO;
import com.dl.lottery.dto.MatchLineUpPersonDTO;
import com.dl.lottery.enums.MatchLineUpEnums;
import com.dl.shop.lottery.dao2.DlMatchLineUpsMapper;
import com.dl.shop.lottery.model.DlMatchLineUps;

@Service
@Transactional(value="transactionManager2")
public class DlMatchLineUpsService extends AbstractService<DlMatchLineUps> {
    @Resource
    private DlMatchLineUpsMapper dlMatchLineUpsMapper;

    //獲取比分信息
    public MatchLineUpInfosDTO getMatchLineUps(Integer changciId) {
    	MatchLineUpInfosDTO matchLineUpInfosDTO = new  MatchLineUpInfosDTO();
    	DlMatchLineUps matchLineUps = dlMatchLineUpsMapper.getByChangciId(changciId);
    	if(matchLineUps != null) {
    		String matchLineups2 = matchLineUps.getMatchLineups();
    		this.parseMatchLineups(matchLineups2, matchLineUpInfosDTO);
    	}
    	return matchLineUpInfosDTO;
    }
    //解析比分信息
	private void parseMatchLineups(String matchLineups, MatchLineUpInfosDTO matchLineUpInfosDTO) {
		JSONObject dataObj = null;
		JSONObject injuriesObj = null; 
		JSONObject suspensionObj = null; 
		JSONObject hLineupsObj = null; 
		JSONObject aLineupsObj = null; 
		if(StringUtils.isNotBlank(matchLineups)) {
			try {
				dataObj = JSON.parseObject(matchLineups);
			} catch (Exception e) {
			}
		}
		//解析data对象
		if(dataObj != null) {
			try {
				injuriesObj = dataObj.getJSONObject("injuries_info");
			} catch (Exception e) {
			}
			try {
				suspensionObj = dataObj.getJSONObject("suspension_info");
			} catch (Exception e) {
			}
			try {
				hLineupsObj = dataObj.getJSONObject("h_lineups_info");
			} catch (Exception e) {
			}
			try {
				aLineupsObj = dataObj.getJSONObject("a_lineups_info");
			} catch (Exception e) {
			}
			try {
				String refereeName = dataObj.getString("referee_name");
				matchLineUpInfosDTO.setRefereeName(refereeName==null?"":refereeName);
			} catch (Exception e) {
			}
		}
		//解析主队
		List<MatchLineUpPersonDTO> hlineupPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		List<MatchLineUpPersonDTO> hbenchPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		String formationTeamH = null;
		String coachTeamH = null;
		if(hLineupsObj != null) {
			try {
				formationTeamH = hLineupsObj.getString("formation_team_h");
			} catch (Exception e) {
			}
			try {
				coachTeamH = hLineupsObj.getString("coach_team_h");
			} catch (Exception e) {
			}
			JSONArray bArray = null;
			try {
				bArray = hLineupsObj.getJSONArray("lineups_bench");
			} catch (Exception e) {
			}
			JSONArray lArray = null;
			try {
				lArray = hLineupsObj.getJSONArray("lineups");
			} catch (Exception e) {
			}
			if(bArray != null) {
				hbenchPersons = new ArrayList<MatchLineUpPersonDTO>(bArray.size());
				for(int i=0; i<bArray.size();i++) {
					JSONObject hObj = bArray.getJSONObject(i);
					String personId = hObj.getString("person_id");
					String personName = hObj.getString("person");
					String position = hObj.getString("position");
					position = MatchLineUpEnums.getMessageByCode(position);
					String positionX = hObj.getString("position_x");
					String positionY = hObj.getString("position_y");
					String shirtNumber = hObj.getString("shirtnumber");
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					person.setPositionX(positionX);
					person.setPositionY(positionY);
					person.setShirtNumber(shirtNumber);
					hbenchPersons.add(person);
				}
			}
			if(lArray != null) {
				hlineupPersons = new ArrayList<MatchLineUpPersonDTO>(lArray.size());
				for(int i=0; i<lArray.size();i++) {
					JSONObject hObj = lArray.getJSONObject(i);
					String personId = hObj.getString("person_id");
					String personName = hObj.getString("person");
					String position = hObj.getString("position");
					position = MatchLineUpEnums.getMessageByCode(position);
					String positionX = hObj.getString("position_x");
					String positionY = hObj.getString("position_y");
					//球员没有比赛位置，则不统计这场比赛的场上阵容
					if(StringUtils.isEmpty(positionX)&&StringUtils.isEmpty(positionY)) {
						break;
					}
					String shirtNumber = hObj.getString("shirtnumber");
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					person.setPositionX(positionX);
					person.setPositionY(positionY);
					person.setShirtNumber(shirtNumber);
					hlineupPersons.add(person);
				}
			}
		}
		//解析客队
		List<MatchLineUpPersonDTO> alineupPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		List<MatchLineUpPersonDTO> abenchPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		String formationTeamA = null;
		String coachTeamA = null;
		if(aLineupsObj != null) {
			try {
				formationTeamA = aLineupsObj.getString("formation_team_a");
			} catch (Exception e) {
			}
			try {
				coachTeamA = aLineupsObj.getString("coach_team_a");
			} catch (Exception e) {
			}
			JSONArray bArray = null;
			try {
				bArray = aLineupsObj.getJSONArray("lineups_bench");
			} catch (Exception e) {
			}
			JSONArray lArray = null;
			try {
				lArray = aLineupsObj.getJSONArray("lineups");
			} catch (Exception e) {
			}
			if(bArray != null) {
				abenchPersons = new ArrayList<MatchLineUpPersonDTO>(bArray.size());
				for(int i=0; i<bArray.size();i++) {
					JSONObject aObj = bArray.getJSONObject(i);
					String personId = aObj.getString("person_id");
					String personName = aObj.getString("person");
					String position = aObj.getString("position");
					position = MatchLineUpEnums.getMessageByCode(position);
					String positionX = aObj.getString("position_x");
					String positionY = aObj.getString("position_y");
					String shirtNumber = aObj.getString("shirtnumber");
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					person.setPositionX(positionX);
					person.setPositionY(positionY);
					person.setShirtNumber(shirtNumber);
					abenchPersons.add(person);
				}
			}
			if(lArray != null) {
				alineupPersons = new ArrayList<MatchLineUpPersonDTO>(lArray.size());
				for(int i=0; i<lArray.size();i++) {
					JSONObject aObj = lArray.getJSONObject(i);
					String personId = aObj.getString("person_id");
					String personName = aObj.getString("person");
					String position = aObj.getString("position");
					position = MatchLineUpEnums.getMessageByCode(position);
					String positionX = aObj.getString("position_x");
					String positionY = aObj.getString("position_y");
					//球员没有比赛位置，则不统计这场比赛的场上阵容
					if(StringUtils.isEmpty(positionX)&&StringUtils.isEmpty(positionY)) {
						break;
					}
					String shirtNumber = aObj.getString("shirtnumber");
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					person.setPositionX(positionX);
					person.setPositionY(positionY);
					person.setShirtNumber(shirtNumber);
					alineupPersons.add(person);
				}
			}
		}
		//解析伤
		List<MatchLineUpPersonDTO> hInjureiesPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		List<MatchLineUpPersonDTO> aInjureiesPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		if(injuriesObj != null) {
			JSONArray hArray = null;
			try {
				hArray = injuriesObj.getJSONArray("h_injuries");
			} catch (Exception e) {
			}
			JSONArray aArray = null;
			try {
				aArray = injuriesObj.getJSONArray("a_injuries");
			} catch (Exception e) {
			}
			if(hArray != null) {
				hInjureiesPersons = new ArrayList<MatchLineUpPersonDTO>(hArray.size());
				for(int i=0; i<hArray.size();i++) {
					JSONObject hObj = hArray.getJSONObject(i);
					String personId = hObj.getString("person_id");
					String personName = hObj.getString("person_name");
					String position = hObj.getString("position");
					position = MatchLineUpEnums.getMessageByCode(position);
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					hInjureiesPersons.add(person);
				}
			}
			if(aArray != null) {
				aInjureiesPersons = new ArrayList<MatchLineUpPersonDTO>(aArray.size());
				for(int i=0; i<aArray.size();i++) {
					JSONObject aObj = aArray.getJSONObject(i);
					String personId = aObj.getString("person_id");
					String personName = aObj.getString("person_name");
					String position = aObj.getString("position");
					position = MatchLineUpEnums.getMessageByCode(position);
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					aInjureiesPersons.add(person);
				}
			}
		}
		//解析停
		List<MatchLineUpPersonDTO> hSuspensionPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		List<MatchLineUpPersonDTO> aSuspensionPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		if(suspensionObj != null) {
			JSONArray hArray = null;
			try {
				hArray = suspensionObj.getJSONArray("h_suspensions");
			} catch (Exception e) {
			}
			JSONArray aArray = null;
			try {
				aArray = suspensionObj.getJSONArray("a_suspensions");
			} catch (Exception e) {
			}
			if(hArray != null) {
				hSuspensionPersons = new ArrayList<MatchLineUpPersonDTO>(hArray.size());
				for(int i=0; i<hArray.size();i++) {
					JSONObject hObj = hArray.getJSONObject(i);
					String personId = hObj.getString("person_id");
					String personName = hObj.getString("person_name");
					String position = hObj.getString("position");
					position = MatchLineUpEnums.getMessageByCode(position);
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					hSuspensionPersons.add(person);
				}
			}
			if(aArray != null) {
				aSuspensionPersons = new ArrayList<MatchLineUpPersonDTO>(aArray.size());
				for(int i=0; i<aArray.size();i++) {
					JSONObject aObj = aArray.getJSONObject(i);
					String personId = aObj.getString("person_id");
					String personName = aObj.getString("person_name");
					String position = aObj.getString("position");
					position = MatchLineUpEnums.getMessageByCode(position);
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					aSuspensionPersons.add(person);
				}
			}
		}
		matchLineUpInfosDTO.setAbenchPersons(this.doubleListEmptyMatch(abenchPersons, hbenchPersons));
		matchLineUpInfosDTO.setAInjureiesPersons(this.doubleListEmptyMatch(aInjureiesPersons,hInjureiesPersons));
		matchLineUpInfosDTO.setAlineupPersons(this.doubleListEmptyMatch(alineupPersons,hlineupPersons));
		matchLineUpInfosDTO.setASuspensionPersons(this.doubleListEmptyMatch(aSuspensionPersons,hSuspensionPersons));
		matchLineUpInfosDTO.setHbenchPersons(this.doubleListEmptyMatch(hbenchPersons,abenchPersons));
		matchLineUpInfosDTO.setHInjureiesPersons(this.doubleListEmptyMatch(hInjureiesPersons,aInjureiesPersons));
		matchLineUpInfosDTO.setHlineupPersons(this.doubleListEmptyMatch(hlineupPersons,alineupPersons));
		matchLineUpInfosDTO.setHSuspensionPersons(this.doubleListEmptyMatch(hSuspensionPersons,aSuspensionPersons));
		matchLineUpInfosDTO.setCoachTeamA(coachTeamA==null?"":coachTeamA);
		matchLineUpInfosDTO.setCoachTeamH(coachTeamH==null?"":coachTeamH);
		matchLineUpInfosDTO.setFormationTeamA(formationTeamA==null?"":formationTeamA);
		matchLineUpInfosDTO.setFormationTeamH(formationTeamH==null?"":formationTeamH);
	}
	
	/**
	 * 主客队的信息只能成对出现，否则为空
	 * @param list1
	 * @param list2
	 */
	public List doubleListEmptyMatch(List list1,List list2) {
		if(list1.size() == 0 || list2.size() == 0) {
			return new ArrayList<>();
		}
		return list1;
	}
	
}

	

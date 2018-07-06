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
			dataObj = JSON.parseObject(matchLineups);
		}
		//解析data对象
		if(dataObj != null) {
			injuriesObj = dataObj.getJSONObject("injuries_info");
			suspensionObj = dataObj.getJSONObject("suspension_info");
			hLineupsObj = dataObj.getJSONObject("h_lineups_info");
			aLineupsObj = dataObj.getJSONObject("a_lineups_info");
		}
		//解析主队
		List<MatchLineUpPersonDTO> hlineupPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		List<MatchLineUpPersonDTO> hbenchPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		if(hLineupsObj != null) {
			JSONArray bArray = hLineupsObj.getJSONArray("lineups_bench");
			JSONArray lArray = hLineupsObj.getJSONArray("lineups");
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
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					person.setPositionX(positionX);
					person.setPositionY(positionY);
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
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					person.setPositionX(positionX);
					person.setPositionY(positionY);
					hlineupPersons.add(person);
				}
			}
		}
		//解析客队
		List<MatchLineUpPersonDTO> alineupPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		List<MatchLineUpPersonDTO> abenchPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		if(aLineupsObj != null) {
			JSONArray bArray = aLineupsObj.getJSONArray("lineups_bench");
			JSONArray lArray = aLineupsObj.getJSONArray("lineups");
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
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					person.setPositionX(positionX);
					person.setPositionY(positionY);
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
					MatchLineUpPersonDTO person = new MatchLineUpPersonDTO();
					person.setPersonId(personId);
					person.setPersonName(personName);
					person.setPosition(position);
					person.setPositionX(positionX);
					person.setPositionY(positionY);
					alineupPersons.add(person);
				}
			}
		}
		//解析伤
		List<MatchLineUpPersonDTO> hInjureiesPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		List<MatchLineUpPersonDTO> aInjureiesPersons = new ArrayList<MatchLineUpPersonDTO>(0);
		if(injuriesObj != null) {
			JSONArray hArray = injuriesObj.getJSONArray("h_injuries");
			JSONArray aArray = injuriesObj.getJSONArray("a_injuries");
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
			JSONArray hArray = suspensionObj.getJSONArray("h_suspensions");
			JSONArray aArray = suspensionObj.getJSONArray("a_suspensions");
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
		matchLineUpInfosDTO.setAbenchPersons(abenchPersons);
		matchLineUpInfosDTO.setAInjureiesPersons(aInjureiesPersons);
		matchLineUpInfosDTO.setAlineupPersons(alineupPersons);
		matchLineUpInfosDTO.setASuspensionPersons(aSuspensionPersons);
		matchLineUpInfosDTO.setHbenchPersons(hbenchPersons);
		matchLineUpInfosDTO.setHInjureiesPersons(hInjureiesPersons);
		matchLineUpInfosDTO.setHlineupPersons(hlineupPersons);
		matchLineUpInfosDTO.setHSuspensionPersons(hSuspensionPersons);
	}
}

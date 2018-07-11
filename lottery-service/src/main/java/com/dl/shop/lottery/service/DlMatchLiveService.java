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
import com.dl.lottery.dto.MatchLiveEventDTO;
import com.dl.lottery.dto.MatchLiveInfoDTO;
import com.dl.lottery.dto.MatchLiveStatisticsDTO;
import com.dl.lottery.dto.MatchLiveTeamDataDTO;
import com.dl.lottery.enums.MatchLiveDataEnums;
import com.dl.shop.lottery.dao2.DlMatchLiveMapper;
import com.dl.shop.lottery.model.DlMatchLive;

@Service
@Transactional(value="transactionManager2")
public class DlMatchLiveService extends AbstractService<DlMatchLive> {
    @Resource
    private DlMatchLiveMapper dlMatchLiveMapper;

    //獲取賽況信息
    public MatchLiveInfoDTO getMatchLiveInfo(Integer changciId) {
    	MatchLiveInfoDTO dto = new MatchLiveInfoDTO();
    	DlMatchLive dlMatchLive = dlMatchLiveMapper.getByChangciId(changciId);
    	if(dlMatchLive != null) {
    		String matchLiveInfo = dlMatchLive.getMatchLiveInfo();
    		dto = this.parseMatchLineups(matchLiveInfo);
    	}
    	return dto;
    }
    //解析賽況信息
    private MatchLiveInfoDTO parseMatchLineups(String matchLiveInfo) {
    	MatchLiveInfoDTO dto = new MatchLiveInfoDTO();
		JSONObject dataObj = null;
		JSONArray eventArray = null;
		JSONObject statisticsObj = null;
		if(StringUtils.isNotBlank(matchLiveInfo)) {
			try {
				dataObj = JSON.parseObject(matchLiveInfo);
			} catch (Exception e) {
			}
		}
		//解析data对象
		if(dataObj != null) {
			try {
				eventArray = dataObj.getJSONArray("event");
			} catch (Exception e1) {
			}
			try {
				statisticsObj = dataObj.getJSONObject("statistics");
			} catch (Exception e) {
			}
		}
		//解析事件
		List<MatchLiveEventDTO> eventList = new ArrayList<MatchLiveEventDTO>(0);
		if(eventArray != null) {
			eventList = new ArrayList<MatchLiveEventDTO>(eventArray.size());
			for(int i=0; i< eventArray.size(); i++) {
				JSONArray arr = eventArray.getJSONArray(i);
				if(arr.size() == 1) {
					JSONObject obj = arr.getJSONObject(0);
					MatchLiveEventDTO event = this.matchLiveEventObj(obj);
					eventList.add(event);
				} else if(arr.size() == 2){
					JSONObject obj = arr.getJSONObject(0);
					MatchLiveEventDTO event = this.matchLiveEventObj(obj);
					eventList.add(event);
					JSONObject obj1 = arr.getJSONObject(1);
					MatchLiveEventDTO event1 = this.matchLiveEventObj(obj1);
					eventList.add(event1);
				}
			}
		}
		dto.setEventList(eventList);
		//解析統計
		List<MatchLiveTeamDataDTO> matchLiveTeamDatas = new ArrayList<MatchLiveTeamDataDTO>(10);
		if(statisticsObj != null) {
			MatchLiveTeamDataDTO posession = this.matchLiveTeamData("posession", statisticsObj);
			posession.setData(MatchLiveDataEnums.POSESSION);
			matchLiveTeamDatas.add(posession);
			MatchLiveTeamDataDTO shotsOnTarget = this.matchLiveTeamData("shots_on_target", statisticsObj);
			shotsOnTarget.setData(MatchLiveDataEnums.SHOTS_ON_TARGET);
			matchLiveTeamDatas.add(shotsOnTarget);
			MatchLiveTeamDataDTO shotsOffTarget = this.matchLiveTeamData("shots_off_target", statisticsObj);
			shotsOffTarget.setData(MatchLiveDataEnums.SHOTS_OFF_TARGET);
			matchLiveTeamDatas.add(shotsOffTarget);
			MatchLiveTeamDataDTO shotsBlocked = this.matchLiveTeamData("shots_blocked", statisticsObj);
			shotsBlocked.setData(MatchLiveDataEnums.SHOTS_BLOCKED);
			matchLiveTeamDatas.add(shotsBlocked);
			MatchLiveTeamDataDTO corners = this.matchLiveTeamData("corners", statisticsObj);
			corners.setData(MatchLiveDataEnums.CORNERS);
			matchLiveTeamDatas.add(corners);
			MatchLiveTeamDataDTO freeKicks = this.matchLiveTeamData("free_kicks", statisticsObj);
			freeKicks.setData(MatchLiveDataEnums.FREE_KICKS);
			matchLiveTeamDatas.add(freeKicks);
			MatchLiveTeamDataDTO offsides = this.matchLiveTeamData("offsides", statisticsObj);
			offsides.setData(MatchLiveDataEnums.OFFSIDES);
			matchLiveTeamDatas.add(offsides);
			MatchLiveTeamDataDTO yellowCards = this.matchLiveTeamData("yellow cards", statisticsObj);
			yellowCards.setData(MatchLiveDataEnums.YELLOW_CARDS);
			matchLiveTeamDatas.add(yellowCards);
			MatchLiveTeamDataDTO fouls = this.matchLiveTeamData("fouls", statisticsObj);
			fouls.setData(MatchLiveDataEnums.FOULS);
			matchLiveTeamDatas.add(fouls);
			MatchLiveTeamDataDTO dangerousAttacks = this.matchLiveTeamData("dangerous_attacks", statisticsObj);
			dangerousAttacks.setData(MatchLiveDataEnums.DANGEROUS_ATTACKS);
			matchLiveTeamDatas.add(dangerousAttacks);
//			MatchLiveTeamDataDTO goalKicks = this.matchLiveTeamData("goal_kicks", statisticsObj);
//			MatchLiveTeamDataDTO attacks = this.matchLiveTeamData("attacks", statisticsObj);
//			MatchLiveTeamDataDTO goals = this.matchLiveTeamData("goals", statisticsObj);
//			MatchLiveTeamDataDTO possession = this.matchLiveTeamData("possession", statisticsObj);
//			MatchLiveTeamDataDTO substitutions = this.matchLiveTeamData("substitutions", statisticsObj);
//			MatchLiveTeamDataDTO throwIns = this.matchLiveTeamData("throw_ins", statisticsObj);
			/*matchLiveStatisticsDTO.setAttacks(attacks);
			matchLiveStatisticsDTO.setCorners(corners);
			matchLiveStatisticsDTO.setDangerousAttacks(dangerousAttacks);
			matchLiveStatisticsDTO.setFouls(fouls);
			matchLiveStatisticsDTO.setFreeKicks(freeKicks);
			matchLiveStatisticsDTO.setGoalKicks(goalKicks);
			matchLiveStatisticsDTO.setGoals(goals);
			matchLiveStatisticsDTO.setOffsides(offsides);
			matchLiveStatisticsDTO.setPosession(posession);
			matchLiveStatisticsDTO.setPossession(possession);
			matchLiveStatisticsDTO.setShotsBlocked(shotsBlocked);
			matchLiveStatisticsDTO.setShotsOffTarget(shotsOffTarget);
			matchLiveStatisticsDTO.setShotsOnTarget(shotsOnTarget);
			matchLiveStatisticsDTO.setSubstitutions(substitutions);
			matchLiveStatisticsDTO.setThrowIns(throwIns);
			matchLiveStatisticsDTO.setYellowCards(yellowCards);*/
		}
		dto.setMatchLiveStatisticsDTO(matchLiveTeamDatas);
		return dto;
    }
    //获取事件对象
	private MatchLiveEventDTO matchLiveEventObj(JSONObject obj) {
		String eventType = obj.getString("event_type");
		String eventCode = obj.getString("code");
		String eventName = obj.getString("name");
		String minute = obj.getString("minute");
		String personId = obj.getString("person_id");
		String person = obj.getString("person");
		String isHa = obj.getString("is_ha");
		MatchLiveEventDTO matchLiveEventDto = new MatchLiveEventDTO();
		matchLiveEventDto.setEventCode(eventCode);
		matchLiveEventDto.setEventName(eventName);
		matchLiveEventDto.setEventType(eventType);
		matchLiveEventDto.setIsHa(isHa);
		matchLiveEventDto.setMinute(minute);
		matchLiveEventDto.setPerson(person);
		matchLiveEventDto.setPersonId(personId);
		return matchLiveEventDto;
	}
    //获取球队统计数据
    private MatchLiveTeamDataDTO matchLiveTeamData(String key, JSONObject jsonObject) {
    	MatchLiveTeamDataDTO dto = new MatchLiveTeamDataDTO();
    	JSONObject dataObj = null;
		try {
			dataObj = jsonObject.getJSONObject(key);
		} catch (Exception e) {
		}
    	if(dataObj != null) {
    		String teamAData = dataObj.getString("team_a_data");
    		String teamHData = dataObj.getString("team_h_data");
    		dto.setTeamAData(teamAData);
    		dto.setTeamHData(teamHData);
    	}
    	return dto;
    }
}

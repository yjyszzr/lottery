package com.dl.shop.lottery.service;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.service.AbstractService;
import com.dl.lottery.dto.MatchLiveEventDTO;
import com.dl.lottery.dto.MatchLiveStatisticsDTO;
import com.dl.lottery.dto.MatchLiveTeamDataDTO;
import com.dl.shop.lottery.dao2.DlMatchLiveMapper;
import com.dl.shop.lottery.model.DlMatchLive;

@Service
@Transactional
public class DlMatchLiveService extends AbstractService<DlMatchLive> {
    @Resource
    private DlMatchLiveMapper dlMatchLiveMapper;

    //獲取賽況信息
    public void getMatchLiveInfo(Integer changciId) {
    	DlMatchLive dlMatchLive = dlMatchLiveMapper.getByChangciId(changciId);
    	if(dlMatchLive != null) {
    		String matchLiveInfo = dlMatchLive.getMatchLiveInfo();
    		this.parseMatchLineups(matchLiveInfo);
    	}
    }
    //解析賽況信息
    private void parseMatchLineups(String matchLiveInfo) {
		JSONObject dataObj = null;
		JSONArray eventArray = null;
		JSONObject statisticsObj = null;
		if(StringUtils.isNotBlank(matchLiveInfo)) {
			dataObj = JSON.parseObject(matchLiveInfo);
		}
		//解析data对象
		if(dataObj != null) {
			eventArray = dataObj.getJSONArray("event");
			statisticsObj = dataObj.getJSONObject("statistics");
		}
		//解析事件
		if(eventArray != null) {
			for(int i=0; i< eventArray.size(); i++) {
				JSONArray arr = eventArray.getJSONArray(i);
				if(arr.size() == 1) {
					JSONObject obj = arr.getJSONObject(1);
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
				} else if(arr.size() == 2){
					
				}
			}
		}
		//解析統計
		if(statisticsObj != null) {
			MatchLiveTeamDataDTO attacks = this.matchLiveTeamData("attacks", statisticsObj);
			MatchLiveTeamDataDTO corners = this.matchLiveTeamData("corners", statisticsObj);
			MatchLiveTeamDataDTO dangerousAttacks = this.matchLiveTeamData("dangerous_attacks", statisticsObj);
			MatchLiveTeamDataDTO fouls = this.matchLiveTeamData("fouls", statisticsObj);
			MatchLiveTeamDataDTO freeKicks = this.matchLiveTeamData("free_kicks", statisticsObj);
			MatchLiveTeamDataDTO goals = this.matchLiveTeamData("goals", statisticsObj);
			MatchLiveTeamDataDTO goalKicks = this.matchLiveTeamData("goal_kicks", statisticsObj);
			MatchLiveTeamDataDTO offsides = this.matchLiveTeamData("offsides", statisticsObj);
			MatchLiveTeamDataDTO posession = this.matchLiveTeamData("posession", statisticsObj);
			MatchLiveTeamDataDTO possession = this.matchLiveTeamData("possession", statisticsObj);
			MatchLiveTeamDataDTO shotsBlocked = this.matchLiveTeamData("shots_blocked", statisticsObj);
			MatchLiveTeamDataDTO shotsOffTarget = this.matchLiveTeamData("shots_off_target", statisticsObj);
			MatchLiveTeamDataDTO shotsOnTarget = this.matchLiveTeamData("shots_on_target", statisticsObj);
			MatchLiveTeamDataDTO substitutions = this.matchLiveTeamData("substitutions", statisticsObj);
			MatchLiveTeamDataDTO throwIns = this.matchLiveTeamData("throw_ins", statisticsObj);
			MatchLiveTeamDataDTO yellowCards = this.matchLiveTeamData("yellow cards", statisticsObj);
			MatchLiveStatisticsDTO matchLiveStatisticsDTO = new MatchLiveStatisticsDTO();
			matchLiveStatisticsDTO.setAttacks(attacks);
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
			matchLiveStatisticsDTO.setYellowCards(yellowCards);
		}
    }
    
    private MatchLiveTeamDataDTO matchLiveTeamData(String key, JSONObject jsonObject) {
    	MatchLiveTeamDataDTO dto = new MatchLiveTeamDataDTO();
    	JSONObject dataObj = jsonObject.getJSONObject(key);
    	if(dataObj != null) {
    		String teamAData = dataObj.getString("team_a_data");
    		String teamHData = dataObj.getString("team_h_data");
    		dto.setTeamAData(teamAData);
    		dto.setTeamHData(teamHData);
    	}
    	return dto;
    }
}

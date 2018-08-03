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
import com.dl.lottery.dto.MatchLiveTeamDataDTO;
import com.dl.lottery.dto.MatchMinuteAndScoreDTO;
import com.dl.lottery.enums.MatchLiveDataEnums;
import com.dl.lottery.enums.MatchLiveEventEnums;
import com.dl.lottery.enums.MatchStatusEnums;
import com.dl.shop.lottery.dao2.DlMatchLiveMapper;
import com.dl.shop.lottery.model.DlMatchLive;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(value="transactionManager2")
@Slf4j
public class DlMatchLiveService extends AbstractService<DlMatchLive> {
    @Resource
    private DlMatchLiveMapper dlMatchLiveMapper;
    
    //获取比赛的进行时长和比分
    public MatchMinuteAndScoreDTO getMatchInfoNow(Integer changciId) {
    	MatchMinuteAndScoreDTO dto = new MatchMinuteAndScoreDTO();
    	DlMatchLive dlMatchLive = dlMatchLiveMapper.getByChangciId(changciId);
    	if(dlMatchLive != null && !StringUtils.isEmpty(dlMatchLive.getMatchLiveInfo())) {
    		String matchLiveInfo = dlMatchLive.getMatchLiveInfo();
    		dto = this.parseJsonStr(matchLiveInfo);
    	}
    	
    	return dto;
    }
    
    public static MatchMinuteAndScoreDTO parseJsonStr(String matchLiveInfo) {
    	MatchMinuteAndScoreDTO dto = new MatchMinuteAndScoreDTO();
    	JSONObject dataObj = null;
    	if(StringUtils.isNotBlank(matchLiveInfo)) {
			try {
				dataObj = JSON.parseObject(matchLiveInfo);
			} catch (Exception e) {
				log.error(e.getMessage());
				return dto;
			}
		}

        String minute = dataObj.getString("minute");
        String fsH = dataObj.getString("fs_h");
        String fsA = dataObj.getString("fs_a");
        String htsH = dataObj.getString("hts_h");
        String htsA = dataObj.getString("hts_a");
        String minuteExtra = dataObj.getString("minute_extra");
        String matchStatus = dataObj.getString("match_status");
        if(StringUtils.isNotEmpty(minute) && StringUtils.isNotEmpty(minuteExtra) ) {
        	if(Integer.valueOf(minute) == 90) {
            	//Integer beyond90 =Integer.valueOf(minute) + Integer.valueOf(minuteExtra);
            	dto.setMinute("90+");
        	}else {
        		dto.setMinute(minute);
        	}
        }else {
        	dto.setMinute(minute);
        }
        dto.setMatchStatus(matchStatus);
        dto.setFirstHalf(createShowScore(htsH,htsA));
        dto.setWhole(createShowScore(fsH,fsA));
    	return dto;
    }
    
    /**
     * 对于已经开赛但是抓取比分显示是空的显示0:0,否则按实际情况展示
     * @return
     */
    public static String createShowScore(String hScore,String aScore) {
    	if(StringUtils.isEmpty(hScore) && StringUtils.isEmpty(aScore)) {
    		return "0:0";
    	}
    	return hScore+":"+aScore;
    }
    
    //獲取賽況信息
    public MatchLiveInfoDTO getMatchLiveInfo(Integer changciId) {
    	MatchLiveInfoDTO dto = new MatchLiveInfoDTO();
    	DlMatchLive dlMatchLive = dlMatchLiveMapper.getByChangciId(changciId);
    	if(dlMatchLive != null) {
    		String matchLiveInfo = dlMatchLive.getMatchLiveInfo();
    		dto = this.parseMatchLineups(matchLiveInfo);
    	}
    	dto.setMatchStatus(MatchStatusEnums.Fixture.getCode());//抓取不到直播的，比赛状态就显示未开赛
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
		
		//比分和比赛状态
		String matchStatus = dataObj.getString("match_status");
        String minute = dataObj.getString("minute");
        String fsH = dataObj.getString("fs_h");
        String fsA = dataObj.getString("fs_a");
        String htsH = dataObj.getString("hts_h");
        String htsA = dataObj.getString("hts_a");
        String minuteExtra = dataObj.getString("minute_extra");
        dto.setMatchStatus(MatchStatusEnums.getCodeByEnName(matchStatus));
        if(StringUtils.isNoneEmpty(minute) && StringUtils.isNoneEmpty(minuteExtra) ) {
        	if(Integer.valueOf(minute) == 90 ) {
            	dto.setMinute("90+");
        	}else {
        		dto.setMinute(minute);
        	}
        }else{
        	dto.setMinute(minute);
        }
        dto.setFsH(fsH);
        dto.setFsA(fsA);
        dto.setHtsH(htsH);
        dto.setHtsA(htsA);
		
		//解析事件
		List<MatchLiveEventDTO> eventList = new ArrayList<MatchLiveEventDTO>(0);
		if(eventArray != null) {
			eventList = new ArrayList<MatchLiveEventDTO>(eventArray.size());
			for(int i=0; i< eventArray.size(); i++) {
				JSONArray arr = eventArray.getJSONArray(i);
				if(arr.size() == 1) {
					JSONObject obj = arr.getJSONObject(0);
					MatchLiveEventDTO event = this.matchLiveEventObj(obj);
					if(event != null) {
						eventList.add(event);
					}
				} else if(arr.size() == 2){
					JSONObject obj = arr.getJSONObject(0);
					MatchLiveEventDTO event = this.matchLiveEventObj(obj);
					if(event != null) {
						eventList.add(event);
					}
					JSONObject obj1 = arr.getJSONObject(1);
					MatchLiveEventDTO event1 = this.matchLiveEventObj(obj1);
					if(event1 != null) {
						eventList.add(event1);
					}
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
		String eventCode = obj.getString("code");
		String eventName = MatchLiveEventEnums.getMessageByCode(eventCode);
		if(eventName == null)return null;
		String eventType = obj.getString("event_type");
//		String eventName = obj.getString("name");
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

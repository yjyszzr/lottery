package com.dl.lottery.enums;

import org.apache.commons.lang3.StringUtils;

public enum MatchLiveDataEnums {
	POSESSION("posession","控球率",1),
	SHOTS_ON_TARGET("shots_on_target","射正",0),
	SHOTS_OFF_TARGET("shots_off_target","射偏",0),
	SHOTS_BLOCKED("shots_blocked","被封堵",0),
	CORNERS("corners","角球",0),
	FREE_KICKS("free_kicks","任意球",0),
	OFFSIDES("offsides","越位",0),
	YELLOW_CARDS("yellow cards","黄牌",0),
	FOULS("fouls","犯规",0),
	DANGEROUS_ATTACKS("dangerous_attacks","有威胁攻势",0),
	UNKNOWN("Unknown","未知", 0);
	
	private String code;
	private String msg;
	private int type;
	
	MatchLiveDataEnums(String code, String msg, int type){
		this.code = code;
		this.msg = msg;
		this.type = type;
	}

	public static String getMessageByCode(String code) {
		if(StringUtils.isNotBlank(code)) {
			for(MatchLiveDataEnums ml: MatchLiveDataEnums.values()) {
				if(ml.getCode().toLowerCase().equals(code.toLowerCase())) {
					return ml.getMsg();
				}
			}
		}
		return MatchLiveDataEnums.UNKNOWN.getMsg();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}

package com.dl.lottery.enums;

import org.apache.commons.lang3.StringUtils;

public enum MatchLineUpEnums {
	ATTACKER("Attacker","前锋"),
	GOALKEEPER("Goalkeeper","守门员"),
	MIDFIELDER("Midfielder","中场"),
	DEFENDER("Defender","后卫"),
	UNKNOWN("Unknown","未知");
	
	private String code;
	private String msg;
	
	MatchLineUpEnums(String code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public static String getMessageByCode(String code) {
		if(StringUtils.isNotBlank(code)) {
			for(MatchLineUpEnums ml: MatchLineUpEnums.values()) {
				if(ml.getCode().toLowerCase().equals(code.toLowerCase())) {
					return ml.getMsg();
				}
			}
		}
		return MatchLineUpEnums.UNKNOWN.getMsg();
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
}

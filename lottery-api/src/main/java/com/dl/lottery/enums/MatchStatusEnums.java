package com.dl.lottery.enums;

import org.apache.commons.lang3.StringUtils;

public enum MatchStatusEnums {
     Fixture("0","未开赛"),
     Played("1","已完成"),
     Playing("6","进行中"),
     Postponed("4","推迟"),
     Suspended("5","暂停"),
     Cancelled("2","取消");
	
	private String code;
	private String msg;
	
	MatchStatusEnums(String code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public static String getMessageByCode(String code) {
		if(StringUtils.isNotBlank(code)) {
			for(MatchStatusEnums ml: MatchStatusEnums.values()) {
				if(ml.getCode().toLowerCase().equals(code.toLowerCase())) {
					return ml.getMsg();
				}
			}
		}
		return  null;
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

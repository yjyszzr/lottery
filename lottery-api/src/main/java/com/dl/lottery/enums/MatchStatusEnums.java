package com.dl.lottery.enums;

import org.apache.commons.lang3.StringUtils;

public enum MatchStatusEnums {
	
     Fixture("0","未开赛","Fixture"),
     Played("1","已完成","Played"),
     Playing("6","进行中","Playing"),
     Postponed("4","推迟","Postponed"),
     Suspended("5","暂停","Suspended"),
     Cancelled("2","取消","Cancelled");
	
	private String code;
	private String msg;
	private String enName;
	
	MatchStatusEnums(String code, String msg,String enName){
		this.code = code;
		this.msg = msg;
		this.enName = enName;
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
	
	public static String getCodeByEnName(String enName) {
		if(StringUtils.isNotBlank(enName)) {
			for(MatchStatusEnums ml: MatchStatusEnums.values()) {
				if(ml.getEnName().toLowerCase().equals(enName.toLowerCase())) {
					return ml.getCode();
				}
			}
		}
		return  null;
	}
	

	public String getCode() {
		return code;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
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

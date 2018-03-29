package com.dl.enums;

public enum MatchResultHafuEnum {
	
	HAFU_HH("33", "胜胜"),
	HAFU_HD("31", "胜平"),
	HAFU_HA("30", "胜负"),
	HAFU_DH("13", "平胜"),
	HAFU_DD("11", "平平"),
	HAFU_DA("10", "平负"),
	HAFU_AH("03", "负胜"),
	HAFU_AD("01", "负平"),
	HAFU_AA("00", "负负");
	
	private String code;
	private String msg;
	
	private MatchResultHafuEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static String getName(String index) {
		for(MatchResultHafuEnum lwd: MatchResultHafuEnum.values()) {
			if(lwd.getCode().equals(index)) {
				return lwd.getMsg();
			}
		}
		return null;
	}
	
	public static String getCode(String value) {
		for(MatchResultHafuEnum lwd: MatchResultHafuEnum.values()) {
			if(lwd.getMsg().equals(value)) {
				return lwd.getCode();
			}
		}
		return null;
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

package com.dl.shop.lottery.core;

public enum MatchChangeEnum {

	equals(0, "equal"),
	up(1, "up"),
	down(2, "down");
	private Integer code;
	private String msg;
	
	private MatchChangeEnum(Integer code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public static String getName(int index) {
		for(MatchChangeEnum lwd: MatchChangeEnum.values()) {
			if(lwd.getCode() == index) {
				return lwd.getMsg();
			}
		}
		return null;
	}
	
	public static Integer getCode(String value) {
		for(MatchChangeEnum lwd: MatchChangeEnum.values()) {
			if(lwd.getMsg().equals(value)) {
				return lwd.getCode();
			}
		}
		return null;
	}
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

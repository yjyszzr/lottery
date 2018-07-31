package com.dl.lottery.enums;

public enum PlayLabelEnums {
	Single(1,"单关"),
	Today_Focus(2,"今日开奖"),
	Add_Reward(3,"加奖"),
	Hot(4,"热门");
	
	private Integer code;
	private String msg;
	
	PlayLabelEnums(Integer code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public static String getMessageByCode(Integer code) {
		for(PlayLabelEnums ml: PlayLabelEnums.values()) {
			if(ml.getCode() == code) {
				return ml.getMsg();
			}
		}
		return "";
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

package com.dl.lottery.enums;

public enum CatEnums {
	Today_Focus("1","今日关注"),
	Buy_Forcast("2","竞彩预测"),
	Good_Analysis("3","牛人分析"),
	World_Cup("4","其他");
	
	private String code;
	private String msg;
	
	CatEnums(String code, String msg){
		this.code = code;
		this.msg = msg;
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

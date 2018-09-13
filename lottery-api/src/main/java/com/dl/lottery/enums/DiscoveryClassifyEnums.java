package com.dl.lottery.enums;

public enum DiscoveryClassifyEnums {
	OpenPrizes("1","开奖结果"),
	Professionals("2","专家广场"),
	LotteryStudy("3","彩票学堂"),
	Activities("4","活动中心"),
	Articles("5","资讯信息"),
	ShowOrders("6","晒单公园"),
	Leagues("7","联赛资料");
	
	private String code;
	private String msg;
	
	DiscoveryClassifyEnums(String code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public static String getMessageByCode(String code) {
		for(DiscoveryClassifyEnums ml: DiscoveryClassifyEnums.values()) {
			if(ml.getCode().equals(code)) {
				return ml.getMsg();
			}
		}
		return "";
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

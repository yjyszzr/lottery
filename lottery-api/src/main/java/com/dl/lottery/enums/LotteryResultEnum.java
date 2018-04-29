package com.dl.lottery.enums;

public enum LotteryResultEnum {

	BET_MONEY_LIMIT(303001,"单笔订单金额不得超过2万元");
	
	private Integer code;
	private String msg;
	
	LotteryResultEnum(Integer code, String msg){
		this.code = code;
		this.msg = msg;
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

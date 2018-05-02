package com.dl.lottery.enums;

public enum LotteryResultEnum {

	OPTION_ERROR(303000,"操作失败！"),
	BET_MONEY_LIMIT(303001,"单笔订单金额不得超过2万元"),
	BET_TIME_LIMIT(303002,"您有参赛场次投注时间已过！"),
	BET_PLAY_ENABLE(303003,"请选择有效的赛事玩法！"),
	BET_CELL_EMPTY(303004,"请选择有效的参赛场次！"),
	BET_PLAY_TYPE_ENABLE(303005,"请选择有效的串关！"),
	BET_CELL_HAS_NULL(303006,"您有参赛场次没有投注选项！"),
	BET_CELL_NO_SINGLE(303007,"请求场次不能选择单关！"),
	BET_CELL_DAN_ERR(303008,"参赛设胆场次有误，请核对！"),
	BET_MONEY_other(303100,"");
	
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

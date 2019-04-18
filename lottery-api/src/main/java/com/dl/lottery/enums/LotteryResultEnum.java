package com.dl.lottery.enums;

public enum LotteryResultEnum {

	OPTION_ERROR(303000,"操作失败！"),
	BET_MONEY_LIMIT(303001,"单注彩票金额不得超过2万元"),
	BET_TIME_LIMIT(303002,"您有参赛场次投注时间已过！"),
	BET_PLAY_ENABLE(303003,"请选择有效的赛事玩法！"),
	BET_CELL_EMPTY(303004,"请选择有效的参赛场次！"),
	BET_PLAY_TYPE_ENABLE(303005,"请选择有效的串关！"),
	BET_CELL_HAS_NULL(303006,"您有参赛场次没有投注选项！"),
	BET_CELL_NO_SINGLE(303007,"请求场次不能选择单关！"),
	BET_CELL_DAN_ERR(303008,"参赛设胆场次有误，请核对！"),
	ONLY_ONE_CONDITION(303009,"只看已购对阵和赛事筛选为互斥关系,只能选择一种"),
	BET_NUMBER_LIMIT(303010,"彩票总注数不得超过10000注"),
	BET_TIMES_LIMIT(303011,"彩票总倍数不得超过99999倍"),
	ARTICLE_NULL(303012,"资讯已过期！"),
	ARTICLE_ID_NULL(303013,"请选择有郊的文章！"),
	BET_MATCH_STOP(303014,"暂停销售！"),
	BET_MATCH_WC(303015,"世界杯期间投注量较大，为保证出票，最少投注100元！"),
	BET_MONEY_other(303100,""),
	DB_NO_DATA(303016,"无数据"),
	MATCH_BEYOND_EIGHT(303017,"最多选择8场比赛"),
	NO_MATCH(303018,"暂无比赛"),
	BET_PLAY_NOT_MONY(303019,"每次比赛只能选择一种玩法！"),
	BET_SYS_TIME_LIMIT(303020,"所选比赛已超时，请重新投注！"),
	BET_ORDER_MONEY_LIMIT(303021,"投注金额不能高于20万！");
	
	
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

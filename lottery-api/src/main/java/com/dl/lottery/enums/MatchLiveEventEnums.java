package com.dl.lottery.enums;

import org.apache.commons.lang3.StringUtils;

//进球 点球 点球未进 乌龙球  红牌 黄牌 亮黄变红 入球无效  换上 换下  助攻
public enum MatchLiveEventEnums {
	G("G","进球",0),
	PG("PG","点球",0),
	PSM("PSM","点球未进",0),
	PSG("PSG","点球进球",0),
	OG("OG","乌龙球",0),
	RC("RC","红牌",0),
	YC("YC","黄牌",0),
	Y2C("Y2C","二黄变红",0),
//	("","入球无效",0),
	SI("SI","换上",0),
	SO("SO","换下",0),
	AS("AS","助攻",0),
	UNKNOWN("Unknown","未知", 0);
	
	private String code;
	private String msg;
	private int type;
	
	MatchLiveEventEnums(String code, String msg, int type){
		this.code = code;
		this.msg = msg;
		this.type = type;
	}

	public static String getMessageByCode(String code) {
		if(StringUtils.isNotBlank(code)) {
			for(MatchLiveEventEnums ml: MatchLiveEventEnums.values()) {
				if(ml.getCode().toLowerCase().equals(code.toLowerCase())) {
					return ml.getMsg();
				}
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}

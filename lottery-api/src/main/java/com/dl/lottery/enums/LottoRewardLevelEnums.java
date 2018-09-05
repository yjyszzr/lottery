package com.dl.lottery.enums;

public enum LottoRewardLevelEnums {
	FIRST(1,"一等奖","追加一等奖"),
	SECOND(2,"二等奖","追加二等奖"),
	THIRD(3,"三等奖","追加三等奖"),
	FOURTH(4,"四等奖","追加四等奖"),
	FIVETH(5,"五等奖","追加五等奖"),
	SIXTH(6,"六等奖","追加六等奖");
	
	private Integer code;
	private String basicName;
	private String appendName;
	
	LottoRewardLevelEnums(Integer code, String basicName ,String appendName){
		this.code = code;
		this.basicName = basicName;
		this.appendName = appendName;
	}

	public static String getbasicNameByCode(Integer code) {
		for(LottoRewardLevelEnums ml: LottoRewardLevelEnums.values()) {
			if(ml.getCode() == code) {
				return ml.getBasicName();
			}
		}
		return "";
	}
	
	public static String getappendNameByCode(Integer code) {
		for(LottoRewardLevelEnums ml: LottoRewardLevelEnums.values()) {
			if(ml.getCode() == code) {
				return ml.getAppendName();
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

	public String getBasicName() {
		return basicName;
	}

	public void setBasicName(String basicName) {
		this.basicName = basicName;
	}

	public String getAppendName() {
		return appendName;
	}

	public void setAppendName(String appendName) {
		this.appendName = appendName;
	}

}

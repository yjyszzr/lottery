package com.dl.enums;

public enum MatchResultCrsEnum {
	
	CRS_10("10", "1:0"), 
	CRS_20("20", "2:0"),
	CRS_21("21", "2:1"),
	CRS_30("30", "3:0"),
	CRS_31("31", "3:1"),
	CRS_32("32", "3:2"),
	CRS_40("40", "4:0"),
	CRS_41("41", "4:1"),
	CRS_42("42", "4:2"),
	CRS_50("50", "5:0"),
	CRS_51("51", "5:1"),
	CRS_52("52", "5:2"),
	CRS_90("90", "胜其它"),
	
	CRS_00("00", "0:0"),
	CRS_11("11", "1:1"),
	CRS_22("22", "2:2"),
	CRS_33("33", "3:3"),
	CRS_99("99", "平其它"),

	CRS_01("01", "0:1"),
	CRS_02("02", "0:2"),
	CRS_12("12", "1:2"),
	CRS_03("03", "0:3"),
	CRS_13("13", "1:3"),
	CRS_04("04", "0:4"),
	CRS_14("14", "1:4"),
	CRS_24("24", "2:4"),
	CRS_05("05", "0:5"),
	CRS_15("15", "1:5"),
	CRS_25("25", "2:5"),
	CRS_09("09", "负其它");
	
	private String code;
	private String msg;
	
	private MatchResultCrsEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static String getName(String index) {
		for(MatchResultCrsEnum lwd: MatchResultCrsEnum.values()) {
			if(lwd.getCode().equals(index)) {
				return lwd.getMsg();
			}
		}
		return null;
	}
	
	public static String getCode(String value) {
		for(MatchResultCrsEnum lwd: MatchResultCrsEnum.values()) {
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

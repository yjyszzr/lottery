package com.dl.shop.lottery.core;

public enum LocalWeekDate {

	week_date_1(1,"周一"),
	week_date_2(2,"周二"),
	week_date_3(3,"周三"),
	week_date_4(4,"周四"),
	week_date_5(5,"周五"),
	week_date_6(6,"周六"),
	week_date_7(7, "周日");
	
	private Integer code;
	private String msg;
	
	private LocalWeekDate(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static String getName(int index) {
		for(LocalWeekDate lwd: LocalWeekDate.values()) {
			if(lwd.getCode() == index) {
				return lwd.getMsg();
			}
		}
		return null;
	}
	
	public static Integer getCode(String value) {
		for(LocalWeekDate lwd: LocalWeekDate.values()) {
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

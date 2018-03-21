package com.dl.enums;

public enum MatchPlayTypeEnum {
	PLAY_TYPE_HAD(1,"had"), // 胜平负
	PLAY_TYPE_HAFU(2,"hafu"), //半全场
	PLAY_TYPE_HHAD(3,"hhad"), //让球胜平负
	PLAY_TYPE_TTG(4,"ttg"); //总进球
	
	private Integer code;
    private String msg;

    private MatchPlayTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getcode() {
        return code;
    }

    public void setcode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

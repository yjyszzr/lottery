package com.dl.enums;

public enum MatchPlayTypeEnum {
	PLAY_TYPE_HHAD(1,"hhad"), //让球胜平负
	PLAY_TYPE_HAD(2,"had"), // 胜平负
	PLAY_TYPE_CRS(3,"crs"), //比分
	PLAY_TYPE_TTG(4,"ttg"), //总进球
	PLAY_TYPE_HAFU(5,"hafu"), //半全场
	PLAY_TYPE_MIX(6,"mix"), //混合过关
	PLAY_TYPE_TSO(7,"tso"); //2选1
	
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

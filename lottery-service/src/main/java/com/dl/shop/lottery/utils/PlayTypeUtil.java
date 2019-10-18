package com.dl.shop.lottery.utils;

import java.util.EnumSet;

import com.dl.base.enums.MatchPlayTypeEnum;

public class PlayTypeUtil {

	/**
	 * 获取赛事玩法类型msg
	 * @param code
	 * @return
	 */
	public static String getPlayTypeMsg(int code) {
		EnumSet<MatchPlayTypeEnum> matchPlayTypeSet = EnumSet.allOf(MatchPlayTypeEnum.class);
		for(MatchPlayTypeEnum matchPlayTypeEnum : matchPlayTypeSet) {
			if(matchPlayTypeEnum.getcode() == code) {
				return matchPlayTypeEnum.getMsg();
			}
		}
		return null;
	}
	
	/**
	 * 获取赛事玩法类型code
	 * @param msg
	 * @return
	 */
	public static Integer getPlayTypeCode(String msg) {
		EnumSet<MatchPlayTypeEnum> matchPlayTypeSet = EnumSet.allOf(MatchPlayTypeEnum.class);
		for(MatchPlayTypeEnum matchPlayTypeEnum : matchPlayTypeSet) {
			if(matchPlayTypeEnum.getMsg().equals(msg)) {
				return matchPlayTypeEnum.getcode();
			}
		}
		return null;
	}
	
}

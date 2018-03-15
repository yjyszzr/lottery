package com.dl.shop.lottery.model;

/**
 * 单场比赛即时比分数据
 *
 */
public class BiFenLiveBean {

	/** 比赛开始时间 */
	public String match_start_time;
	/** 当前时间 */
	public String now_time;
	/** 比赛状态 */
	public String match_status;
	/** 主队比分 */
	public String host_bf;
	/** 客队比分 */
	public String guest_bf;
	/** 主队半场比分 */
	public String host_sbf;
	/** 客队半场比分 */
	public String guest_sbf;
	/** 主队图片链接 */
	public String hostImgUrl;
	/** 客队图片链接 */
	public String guestImgUrl;
	
	public String hostName;
	
	public String guestName;
	
	/** 比赛进行的时间 */
	public String go_time;
	
}

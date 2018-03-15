package com.dl.shop.lottery.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 比分直播-足球对阵数据
 */

public class ZqListEntity {
	/**联赛列表*/
	public List<ZqLeague> league_list  = new ArrayList<ZqLeague>();;
	
	/**赛程列表*/
	public List<ZqMatchDate> match_list  = new ArrayList<ZqMatchDate>();
	
//	public Response response;
	
	/** 服务器当前时间 */
	public String now_time;
}

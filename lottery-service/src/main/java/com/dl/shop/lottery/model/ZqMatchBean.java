package com.dl.shop.lottery.model;
/**
 * 即时比分足球对阵数据
 */
public class ZqMatchBean {

	/** 联赛id */
	public String league_id;
	
	/** 场次id*/
	public String play_id;
	
	/** 编号*/
	public String chang_ci;
	
	/** 联赛名称 */
	public String league_name;
	
	/** 比赛时间 */
	public String match_time;
	
	/** 主队名称 */
	public String host_team;
	
	/** 客队名称 */
	public String guest_team;
	
	/** 主队黄牌 */
	public String host_yy;
	
	/** 主队红牌 */
	public String host_red;
	
	/** 客队黄牌 */
	public String guest_yy;
	
	/** 客队红牌 */
	public String guest_red;
	
	/** 客队半场比分 */
	public String guest_half;
	
	/** 客队全场比分 */
	public String guest_whole;
	
	/** 主队半场比分 */
	public String host_half;
	
	/** 主队全场比分 */
	public String host_whole;
	
	/** 比赛状态 */
	public String match_status;
	
	/** 上半场or下半场开始时间，具体值需跟进比赛状态来 */
	public String score_time;
	
//	/** 服务器当前时间 */
//	public String now_time;
	
	/** 正在比赛：比赛进行时间，其他状态，则是对应的中文名状态 */
	public String goTime;
	
}

package com.dl.shop.lottery.model;

/**
 * 世界杯冠军对阵数据
 */
public class WorldCupSchema {
	
	/** 场次id*/
	public String play_id;
	
	/** 期号id*/
	public String qihao_id;
	
	/** 主队名称 */
	public String h_team;
	
	/** 图标 */
	public String flagUrl;
	
	/** 概率 */
	public String win_gl;
	
	/** 过关的sp值 */
	public String gg_sp;
	
	/** 存储选择结果*/
	public boolean[] selectedResult = new boolean[3];
	
	
}

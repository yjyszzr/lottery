package com.dl.shop.lottery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 足球对阵列表
 */
public class ZqMatchDate implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 比赛时间 */
	public String match_time;

	/** 场次列表 */
	public List<ZqMatchBean> play_list = new ArrayList<ZqMatchBean>();

}

package com.dl.shop.lottery.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  足球对阵列表
 */

public class JczqMatch {
	
	/**比赛时间*/
	public String match_time;
	
	/**场次列表*/
	public List<JczqSchema> play_list = new ArrayList<JczqSchema>();
}

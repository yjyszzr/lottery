package com.dl.shop.lottery.model;

import com.dl.shop.lottery.core.ProjectConstant;

/**
 * 竞彩足球对阵数据
 */
public class JczqSchema {
	
	/** 联赛id */
	public String league_id;
	
	/** 场次id: Fdal2323232 */
	public String play_id;
	
	/** 期号id */
	public String qihao_id;
	
	/** 场次名称 */
	public String game_no;
	
	/** 联赛名称 */
	public String match_name;
	
	/** 复式截止时间 */
	public String compound_time;
	
	/** 主队名称 */
	public String h_team;
	
	/** 客队名称 */
	public String v_team;
	
	/** 过关的sp值 */
	public String gg_jssp;
	
	/** 日期 */
	public String game_date;
	
	/** 场次:周三001 */
	public String chang_ci;
	
	/** 胜平负sp值 */
	public String spf_rq = "0";
	public String spf_sale  = "1";
	public String spf_sp[] = {"0", "0","0"};
	/** 胜平负存储选择结果： 0-主 , 1-平 , 2-客 */
	public boolean[] spf_selectedResult = { false, false, false };
	public String spf_bet_win_bl;
	public String spf_bet_ping_bl;
	public String spf_bet_fu_bl;
	public String spf_dg = "0";//胜平负单关固赔
	
	/** 让球胜平负 */
	public String rqspf_rq = "0";
	public String rqspf_sale  = "1";;
	public String rqspf_sp[] = {"0", "0","0"};
	public boolean[] rqspf_selectedResult = { false, false, false };
	public String rqspf_bet_win_bl;
	public String rqspf_bet_ping_bl;
	public String rqspf_bet_fu_bl;
	public String rqspf_dg = "0";
	
	/** 半全场胜平负 */
	public String bqspf_rq = "0";
	public String bqspf_sale  = "1";
	public String bqsfp_sp[] = {"0", "0","0", "0","0", "0","0", "0","0"};
	public boolean[] bqspf_selectedResult = { false, false, false,false, false, false,false, false, false};
	public String bqspf_bet_win_bl;
	public String bqspf_bet_ping_bl;
	public String bqspf_bet_fu_bl;
	public String bqspf_dg = "0";
	
	/** 比分 */
	public String bf_rq = "0";
	public String bf_sale  = "1";
	public String []bf_sp = {"0", "0","0", "0","0", "0","0", "0","0", "0","0", "0","0", "0","0", "0","0", "0","0", "0","0", "0","0", "0","0", "0","0", "0","0", "0"};
	public boolean[] bf_selectedResult = { false, false,false, false, false,false, false, false,false, false, false,false, false, false,false, false, false,false, false, false,false, false, false,false, false, false,false, false, false, false, false };
	public String bf_bet_win_bl;
	public String bf_bet_ping_bl;
	public String bf_bet_fu_bl;
	public String bf_dg = "0";
	
	/** 总进球 */
	public String zjq_rq = "0";
	public String zjq_sale  = "1";
	public String []zjq_sp = {"0", "0","0", "0","0", "0","0"};
	public boolean[] zjq_selectedResult = { false, false, false,false, false, false, false, false};
	public String zjq_bet_win_bl;
	public String zjq_bet_ping_bl;
	public String zjq_bet_fu_bl;
	public String zjq_dg = "0";
	
	/**当前玩法 sp值 */
	public String host_sp = "0";
	public String ping_sp = "0";
	public String guest_sp = "0";
	/** 让球 */
	public String gg_rq;
	
	public String is_dg = "0";
	
	/** 存储选择结果*/
	public boolean[] selectedResult;
	/** 当前玩法对应的sp值 */
	public String currentSp[];
	
	/** 获取当前玩法相关数据 */
	public void setPlay(String play){
		if(play.equalsIgnoreCase(ProjectConstant.JZ_SPF)){
			host_sp = spf_sp[0];
			ping_sp = spf_sp[1];
			guest_sp = spf_sp[2];
			gg_rq = spf_rq;
			selectedResult = spf_selectedResult;
			is_dg = spf_dg;
		}else if(play.equalsIgnoreCase(ProjectConstant.JZ_RQSPF)){
			host_sp = rqspf_sp[0];
			ping_sp = rqspf_sp[1];
			guest_sp = rqspf_sp[2];
			gg_rq = rqspf_rq;
			selectedResult = rqspf_selectedResult;
			is_dg = rqspf_dg;
		}
	}
	
	/** 历史战绩 */
	public String history_win;
	public String history_pin;
	public String history_fu;
	public String history_info;
	
	/** 排名 */
	public String guest_sort;
	public String host_sort;
	
	/** 平均欧指 */
	public String host_win_ouzhi;
	public String host_ping_ouzhi;
	public String host_fu_ouzhi;
	
	public String dataId;
	
	/** 近期战绩 */
	public String history_host_record;
	public String history_guest_record;

	
	/** 设置当前玩法对应的数据 */
	public void setDataByPlay(String playType){
		if(playType.equalsIgnoreCase(ProjectConstant.JZ_SPF)){
			currentSp = spf_sp;
			
			
			gg_rq = spf_rq;
			is_dg = spf_dg;
			
			selectedResult = spf_selectedResult;
		}else if(playType.equalsIgnoreCase(ProjectConstant.JZ_RQSPF)){
			currentSp = rqspf_sp;
			
			
			gg_rq = rqspf_rq;
			is_dg = rqspf_dg;
			
			selectedResult = rqspf_selectedResult;
		}else if(playType.equalsIgnoreCase(ProjectConstant.JZ_BF)){
			currentSp = bf_sp;
			
			is_dg = bf_dg;
		}else if(playType.equalsIgnoreCase(ProjectConstant.JZ_BQSPF)){
			currentSp = bqsfp_sp;
			
			is_dg = bqspf_dg;
		}else if(playType.equalsIgnoreCase(ProjectConstant.JZ_ZJQ)){
			currentSp = zjq_sp;
			
			is_dg = zjq_dg;
		}
	}
	
}

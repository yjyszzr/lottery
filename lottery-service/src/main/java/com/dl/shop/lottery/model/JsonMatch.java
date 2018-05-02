package com.dl.shop.lottery.model;

import java.io.Serializable;


public class JsonMatch {
	
    /**
     * 联赛简称
     */
    private String league_addr;

    /**
     * 场次id
     */
    private Integer cangci_id;

    /**
     * 场次
     */
    private String changci;



    /**
     * 主队简称
     */
    private String home_team_abbr;


    private String visiting_team_abbr;


    private String match_time;


    private String first_hslf;
    
    /**
     * 整场得分
     */
    private String whole;
    
    /**
     * 比赛状态：0-未拉取 1-已拉取
     */
    private String code;
    
    private String rangqiu;
    
    private String win;
    
    private String flat;
    
    private String lose;
    
    

}
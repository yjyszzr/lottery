package com.dl.shop.lottery.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 竞足接口返回的所有数据
 */
public class JczqListEntity {
	
	/**联赛列表*/
	public HashMap<String, SporLeague> league_list  = new HashMap<String, SporLeague>();
	
	public List<SporLeague> league_list_hh  = new ArrayList<SporLeague>();
	
	/**联赛列表 --- 让球胜平负*/
	public List<SporLeague> league_list_rqspf  = new ArrayList<SporLeague>();
	
	/**联赛列表 -- 胜平负*/
	public List<SporLeague> league_list_spf  = new ArrayList<SporLeague>();
	
	/**联赛列表 -- 比分*/
	public List<SporLeague> league_list_bf  = new ArrayList<SporLeague>();
	
	/**联赛列表 -- 半全场*/
	public List<SporLeague> league_list_bqspf  = new ArrayList<SporLeague>();
	
	/**联赛列表 -- 总进球*/
	public List<SporLeague> league_list_zjq  = new ArrayList<SporLeague>();
	
	/**赛程列表*/
	public List<JczqMatch> match_list  = new ArrayList<JczqMatch>();
	
	/**赛程列表 -- 胜平负*/
	public List<JczqMatch> match_list_spf  = new ArrayList<JczqMatch>();
	
	/**赛程列表 -- 让球胜平负*/
	public List<JczqMatch> match_list_rqspf  = new ArrayList<JczqMatch>();
	
	/**赛程列表 -- 比分*/
	public List<JczqMatch> match_list_bf  = new ArrayList<JczqMatch>();
	
	/**赛程列表 -- 半全场*/
	public List<JczqMatch> match_list_bqspf  = new ArrayList<JczqMatch>();
	
	/**赛程列表 -- 总进球*/
	public List<JczqMatch> match_list_zjq  = new ArrayList<JczqMatch>();
	
//	public Response response;
	
    public final String ITEM = "item";
    public final String GAMEID = "gameId";
    public final String MATCHID = "matchId";
    public final String MATCHNO = "matchNo";
    public final String MATCHNAME = "matchName";
    public final String HOMETEAM = "homeTeam";
    public final String CONCEDE = "concede";
    public final String GUESTTEAM = "guestTeam";
    public final String ENDTIME = "endTime";
    public final String SPSPF = "spSpf";
    public final String SPRQSPF = "spRqspf";
    public final String SPBF = "spBf";
    public final String SPZJQ = "spZjq";
    public final String SPBQSPF = "spBqspf";
    public final String LEAGUENAME = "leagueName";
    public final String LEAGUEID = "leagueId";
    public final String Date = "date"; 
    public final String PLAYLIST = "playlist";
    
    public final String SPFDG = "spfDg";
    public final String ZJQDG = "zjqDg";
    public final String BFDG = "bfDg";
    public final String BQSPFDG = "bqspfDg";
    public final String RQSPFDG = "rqspfDg";
    public final String HostRecord = "hostRecord";
    public final String GuestRecord = "guestRecord";
    public final String CommonRecord = "commonRecord";
    public final String HostRanking = "hostRanking";
    public final String GuestRanking = "guestRanking";
    public final String OuZhi = "ouzhi";
    public final String DataId = "dataId";
	
}

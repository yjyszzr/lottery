package com.dl.shop.lottery.model;

import java.util.HashMap;
import java.util.Vector;

/**
 * 竞彩足球对阵数据
 */
public class JczqAgainstList {
    public final String TOTAL = "total";
    public final String SPMAPERS = "spmapers";
    public final String MAPER = "maper";
    public final String MAPERVALUE = "mvalue";
    public final String NAME = "name";
    public final String ITEM = "item";
    public final String MATCHNO = "matchNo";
    public final String MATCHNAME = "matchName";
    public final String HOMETEAM = "homeTeam";
    public final String CONCEDE = "concede";
    public final String GUESTTEAM = "guestTeam";
    public final String ENDTIME = "endTime";
    public final String PUBLISHTIME = "publishTime";
    public final String AVG = "avg";
    public final String WIN = "win";
    public final String DRAW = "draw";
    public final String LOSS = "loss";
    public final String SPLIST = "splist";
    public final String SP = "sp";
    public final String SPVALUE = "spValue";
    public final String VALUE = "value";
    public final String LEAGUENAME = "leagueName";
    
    private String total;//总的比赛场数
    private Vector<Maper> maperList = new Vector<Maper>();
    private Vector<MatchBean> matchList = new Vector<MatchBean>();//对阵数据
    private Vector<String> endTimeList = new Vector<String>();//包含几天的赛事
    private Vector<String> matchNameList = new Vector<String>();//联赛名称 --- 从对阵中截取
    private Vector<String> leagueNameList = new Vector<String>();//联赛名称 -- 直接获取
    
    /**
     * 总投注场次
     * @param iTotal
     */
    public void setTotal(String iTotal) {
        this.total = iTotal;
    }
    public String getTotal() {
        return this.total;
    }
    
    public void setMaperList(Maper iMaper) {
        this.maperList.add(iMaper);
    }
    public Vector<Maper> getMaperList() {
        return this.maperList;
    }
    
    /**
     * 对阵
     * @param iMatchBean
     */
    public void setMatchList(MatchBean iMatchBean) {
        this.matchList.add(iMatchBean);
    }
    public Vector<MatchBean> getMatchList() {
        return this.matchList;
    }
    
    /**
     * 比赛截止日期
     * @param iEndTime
     */
    public void setEndTimeList(String iEndTime){
        //若已经包含了，则不添加
        if(!endTimeList.contains(iEndTime)){            
            endTimeList.add(iEndTime);
        }
    }
    public Vector<String> getEndTimeList(){
        return endTimeList;
    }
    
    /**
     * 联赛名
     * @param iMatchName
     */
    public void setMatchNameList(String iMatchName){
        //若已经包含了，则不添加
        if(!matchNameList.contains(iMatchName)){            
            matchNameList.add(iMatchName);
        }
    }
    public Vector<String> getMatchNameList(){
        return matchNameList;
    }
    
    /**
     * 联赛名
     * @param iLeagueName
     */
    public void setLeagueNameList(String iLeagueName){
        if(!leagueNameList.contains(iLeagueName)){            
            leagueNameList.add(iLeagueName);
        }
    }
    public Vector<String> getLeagueNameList(){
        return leagueNameList;
    }
    
    public class MatchBean{
        private String matchNo;
        private String matchName;//联赛名称
        private String homeTeam;//主队
        private String concede;//让球数
        private String guestTeam;//客队
        private String endTime;//结束时间
        private String publishTime;//发布时间--投注
        private String win;
        private String draw;
        private String loss;
        private HashMap<String, String> spValueList = new HashMap<String, String>();
        
        public void setMatchNo(String iMatchNo) {
            this.matchNo = iMatchNo;
        }
        public String getMatchNo() {
            return this.matchNo;
        }
        
        public void setMatchName(String iMatchName) {
            this.matchName = iMatchName;
        }
        public String getMatchName() {
            return this.matchName;
        }
        
        public void setHomeTeam(String iHomeTeam) {
            this.homeTeam = iHomeTeam;
        }
        public String getHomeTeam() {
            return this.homeTeam;
        }
        
        public void setConcede(String iConcede) {
            this.concede = iConcede;
        }
        public String getConcede() {
            return this.concede;
        }
        
        public void setGuestTeam(String iGuestTeam) {
            this.guestTeam = iGuestTeam;
        }
        public String getGuestTeam() {
            return this.guestTeam;
        }
        
        public void setEndTime(String iEndTime) {
            this.endTime = iEndTime;
        }
        public String getEndTime() {
            return this.endTime;
        }
        
        public void setPublishTime(String iPublishTime) {
            this.publishTime = iPublishTime;
        }
        public String getPublishTime() {
            return this.publishTime;
        }
        
        public void setWin(String iWin) {
            this.win = iWin;
        }
        public String getWin() {
            return this.win;
        }
        
        public void setDraw(String iDraw) {
            this.draw = iDraw;
        }
        public String getDraw() {
            return this.draw;
        }
        
        public void setLoss(String iLoss) {
            this.loss = iLoss;
        }
        public String getLoss() {
            return this.loss;
        }
        
        //SP值
        public void setSpValue(SPValue iSpValue) {
            spValueList.put(iSpValue.getValue(), iSpValue.getSpValue());
        }
        public String getSpValue(String iKey) {
            return this.spValueList.get(iKey);
        }
    }
    
    public class Maper{
        private String name;
        private String value;
        
        public void setName(String iName) {
            this.name = iName;
        }
        public String getName() {
            return this.name;
        }
        
        public void setValue(String iValue) {
            this.value = iValue;
        }
        public String getValue() {
            return this.value;
        }
    }
    
    public class SPValue{
        private String spValue;
        private String value;
        
        public void setSpValue(String iSpValue) {
            this.spValue = iSpValue;
        }
        public String getSpValue() {
            return this.spValue;
        }
        
        public void setValue(String iValue) {
            this.value = iValue;
        }
        public String getValue() {
            return this.value;
        }
    }
    
}

package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class LotteryMatchDTO {
	@ApiModelProperty("赛事id")
	private Integer matchId;

	@ApiModelProperty("赛事编号")
	private String matchSn;

	@ApiModelProperty("联赛名称")
	private String leagueName;

	@ApiModelProperty("联赛简称")
	private String leagueAddr;

	// @ApiModelProperty("场次id")
	// private Integer changciId;

	@ApiModelProperty("场次")
	private String changci;

	@ApiModelProperty("场次id")
	private Integer changciId;

	@ApiModelProperty("主队名称")
	private String homeTeamName;

	@ApiModelProperty("主队简称")
	private String homeTeamAbbr;

	@ApiModelProperty("主队logo")
	private String homeTeamLogo;

	@ApiModelProperty("客队名称")
	private String visitingTeamName;

	@ApiModelProperty("客队简称")
	private String visitingTeamAbbr;

	@ApiModelProperty("客队logo")
	private String visitingTeamLogo;

	@ApiModelProperty("比赛时间")
	private String matchTime;

	@ApiModelProperty("比赛开始时间")
	private String matchTimeStart;

	@ApiModelProperty("显示时间")
	private Date showTime;

	@ApiModelProperty("上半场得分")
	private String firstHalf;

	@ApiModelProperty("整场得分")
	private String whole;

	@ApiModelProperty("比赛时长")
	private String minute;

	@ApiModelProperty("是否收藏：0-否 1-是")
	private String isCollect;

	@ApiModelProperty("比赛状态:0-未开赛,1-已完成,2-取消,4-推迟,5-暂停,6-进行中")
	private String matchFinish;

	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	public String getMatchSn() {
		return matchSn;
	}

	public void setMatchSn(String matchSn) {
		this.matchSn = matchSn;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public String getLeagueAddr() {
		return leagueAddr;
	}

	public void setLeagueAddr(String leagueAddr) {
		this.leagueAddr = leagueAddr;
	}

	public String getChangci() {
		return changci;
	}

	public void setChangci(String changci) {
		this.changci = changci;
	}

	public Integer getChangciId() {
		return changciId;
	}

	public void setChangciId(Integer changciId) {
		this.changciId = changciId;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public String getHomeTeamAbbr() {
		return homeTeamAbbr;
	}

	public void setHomeTeamAbbr(String homeTeamAbbr) {
		this.homeTeamAbbr = homeTeamAbbr;
	}

	public String getHomeTeamLogo() {
		return homeTeamLogo;
	}

	public void setHomeTeamLogo(String homeTeamLogo) {
		this.homeTeamLogo = homeTeamLogo;
	}

	public String getVisitingTeamName() {
		return visitingTeamName;
	}

	public void setVisitingTeamName(String visitingTeamName) {
		this.visitingTeamName = visitingTeamName;
	}

	public String getVisitingTeamAbbr() {
		return visitingTeamAbbr;
	}

	public void setVisitingTeamAbbr(String visitingTeamAbbr) {
		this.visitingTeamAbbr = visitingTeamAbbr;
	}

	public String getVisitingTeamLogo() {
		return visitingTeamLogo;
	}

	public void setVisitingTeamLogo(String visitingTeamLogo) {
		this.visitingTeamLogo = visitingTeamLogo;
	}

	public String getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}

	public String getMatchTimeStart() {
		return matchTimeStart;
	}

	public void setMatchTimeStart(String matchTimeStart) {
		this.matchTimeStart = matchTimeStart;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public String getFirstHalf() {
		return firstHalf;
	}

	public void setFirstHalf(String firstHalf) {
		this.firstHalf = firstHalf;
	}

	public String getWhole() {
		return whole;
	}

	public void setWhole(String whole) {
		this.whole = whole;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public String getMatchFinish() {
		return matchFinish;
	}

	public void setMatchFinish(String matchFinish) {
		this.matchFinish = matchFinish;
	}

}
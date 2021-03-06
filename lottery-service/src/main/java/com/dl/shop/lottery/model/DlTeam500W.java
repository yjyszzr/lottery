package com.dl.shop.lottery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_team_500w")
public class DlTeam500W {

	/**
	 * 球队编号
	 */
	@Id
	@Column(name = "team_id")
	private Integer teamId;
	/**
	 * 球队名称
	 */
	@Column(name = "team_name")
	private String teamName;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 联赛ID
	 */
	@Column(name = "league_id")
	private Integer leagueId;

	/**
	 * 球队图标
	 */
	@Column(name = "team_pic")
	private String teamPic;

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Integer leagueId) {
		this.leagueId = leagueId;
	}

	public String getTeamPic() {
		return teamPic;
	}

	public void setTeamPic(String teamPic) {
		this.teamPic = teamPic;
	}

}
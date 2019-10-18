package com.dl.shop.lottery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_team_future_500w")
public class DlTeamFuture500W {
	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 比赛时间
	 */
	@Column(name = "match_time")
	private String matchTime;

	/**
	 * 联赛简称
	 */
	@Column(name = "league_abbr")
	private String leagueAbbr;

	/**
	 * 主队简称
	 */
	@Column(name = "home_abbr")
	private String homeAbbr;

	/**
	 * 客队简称
	 */
	@Column(name = "visiting_abbr")
	private String visitingAbbr;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 球队id
	 */
	@Column(name = "team_id")
	private Integer teamId;

	/**
	 * 获取id
	 *
	 * @return id - id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置id
	 *
	 * @param id
	 *            id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取比赛时间
	 *
	 * @return match_time - 比赛时间
	 */
	public String getMatchTime() {
		return matchTime;
	}

	/**
	 * 设置比赛时间
	 *
	 * @param matchTime
	 *            比赛时间
	 */
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}

	/**
	 * 获取联赛简称
	 *
	 * @return league_abbr - 联赛简称
	 */
	public String getLeagueAbbr() {
		return leagueAbbr;
	}

	/**
	 * 设置联赛简称
	 *
	 * @param leagueAbbr
	 *            联赛简称
	 */
	public void setLeagueAbbr(String leagueAbbr) {
		this.leagueAbbr = leagueAbbr;
	}

	/**
	 * 获取主队简称
	 *
	 * @return home_abbr - 主队简称
	 */
	public String getHomeAbbr() {
		return homeAbbr;
	}

	/**
	 * 设置主队简称
	 *
	 * @param homeAbbr
	 *            主队简称
	 */
	public void setHomeAbbr(String homeAbbr) {
		this.homeAbbr = homeAbbr;
	}

	/**
	 * 获取客队简称
	 *
	 * @return visiting_abbr - 客队简称
	 */
	public String getVisitingAbbr() {
		return visitingAbbr;
	}

	/**
	 * 设置客队简称
	 *
	 * @param visitingAbbr
	 *            客队简称
	 */
	public void setVisitingAbbr(String visitingAbbr) {
		this.visitingAbbr = visitingAbbr;
	}

	/**
	 * 获取更新时间
	 *
	 * @return update_time - 更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateTime
	 *            更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取创建时间
	 *
	 * @return create_time - 创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取球队id
	 *
	 * @return team_id - 球队id
	 */
	public Integer getTeamId() {
		return teamId;
	}

	/**
	 * 设置球队id
	 *
	 * @param teamId
	 *            球队id
	 */
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
}
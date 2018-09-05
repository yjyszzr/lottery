package com.dl.shop.lottery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_season_500w")
public class DlSeason500w {
	/**
	 * 500W的联赛赛季id
	 */
	@Id
	@Column(name = "season_id")
	private Integer seasonId;

	/**
	 * 联赛id
	 */
	@Column(name = "league_id")
	private Integer leagueId;

	/**
	 * 联赛赛季
	 */
	@Column(name = "match_season")
	private String matchSeason;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 获取500W的联赛赛季id
	 *
	 * @return season_id - 500W的联赛赛季id
	 */
	public Integer getSeasonId() {
		return seasonId;
	}

	/**
	 * 设置500W的联赛赛季id
	 *
	 * @param seasonId
	 *            500W的联赛赛季id
	 */
	public void setSeasonId(Integer seasonId) {
		this.seasonId = seasonId;
	}

	/**
	 * 获取联赛id
	 *
	 * @return league_id - 联赛id
	 */
	public Integer getLeagueId() {
		return leagueId;
	}

	/**
	 * 设置联赛id
	 *
	 * @param leagueId
	 *            联赛id
	 */
	public void setLeagueId(Integer leagueId) {
		this.leagueId = leagueId;
	}

	/**
	 * 获取联赛赛季
	 *
	 * @return match_season - 联赛赛季
	 */
	public String getMatchSeason() {
		return matchSeason;
	}

	/**
	 * 设置联赛赛季
	 *
	 * @param matchSeason
	 *            联赛赛季
	 */
	public void setMatchSeason(String matchSeason) {
		this.matchSeason = matchSeason;
	}

	/**
	 * @return create_time
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return update_time
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
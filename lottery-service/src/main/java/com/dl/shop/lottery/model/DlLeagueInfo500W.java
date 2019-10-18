package com.dl.shop.lottery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "dl_league_500w")
public class DlLeagueInfo500W {

	/**
	 * 联赛编号Id
	 */
	@Column(name = "league_id")
	private Integer leagueId;

	/**
	 * 联赛名称
	 */
	@Column(name = "league_name")
	private String leagueName;

	/**
	 * 联赛简称
	 */
	@Column(name = "league_abbr")
	private String leagueAbbr;

	@Column(name = "contry_id")
	private Integer contryId;

	@Column(name = "league_type")
	private Integer leagueType;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "league_pic")
	private String leaguePic;

	@Column(name = "is_league")
	private Integer isLeague;

	@Column(name = "is_hot")
	private Integer isHot;

	@Column(name = "league_rule")
	private String leagueRule;

	@Column(name = "l_id")
	private Integer lId;

	public String getLeagueAbbr() {
		return leagueAbbr;
	}

	public void setLeagueAbbr(String leagueAbbr) {
		this.leagueAbbr = leagueAbbr;
	}

	public Integer getlId() {
		return lId;
	}

	public void setlId(Integer lId) {
		this.lId = lId;
	}

	/**
	 * 获取联赛编号
	 *
	 * @return league_id - 联赛编号
	 */
	public Integer getLeagueId() {
		return leagueId;
	}

	/**
	 * 设置联赛编号
	 *
	 * @param leagueId
	 *            联赛编号
	 */
	public void setLeagueId(Integer leagueId) {
		this.leagueId = leagueId;
	}

	/**
	 * 获取联赛名称
	 *
	 * @return league_name - 联赛名称
	 */
	public String getLeagueName() {
		return leagueName;
	}

	/**
	 * 设置联赛名称
	 *
	 * @param leagueName
	 *            联赛名称
	 */
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public Integer getContryId() {
		return contryId;
	}

	public void setContryId(Integer contryId) {
		this.contryId = contryId;
	}

	public Integer getLeagueType() {
		return leagueType;
	}

	public void setLeagueType(Integer leagueType) {
		this.leagueType = leagueType;
	}

	public String getLeaguePic() {
		return leaguePic;
	}

	public void setLeaguePic(String leaguePic) {
		this.leaguePic = leaguePic;
	}

	public Integer getIsHot() {
		return isHot;
	}

	public void setIsHot(Integer isHot) {
		this.isHot = isHot;
	}

	public String getLeagueRule() {
		return leagueRule;
	}

	public void setLeagueRule(String leagueRule) {
		this.leagueRule = leagueRule;
	}

	public Integer getIsLeague() {
		return isLeague;
	}

	public void setIsLeague(Integer isLeague) {
		this.isLeague = isLeague;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
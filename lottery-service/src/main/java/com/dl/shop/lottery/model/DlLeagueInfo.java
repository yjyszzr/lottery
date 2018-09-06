package com.dl.shop.lottery.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_league_info")
public class DlLeagueInfo {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 联赛编号
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
	private String leagueAddr;

	@Column(name = "contry_id")
	private Integer contryId;

	@Column(name = "league_type")
	private Integer leagueType;

	@Column(name = "league_from")
	private Integer leagueFrom;

	@Column(name = "league_pic")
	private String leaguePic;

	@Column(name = "is_league")
	private Integer isLeague;

	@Column(name = "is_hot")
	private Integer isHot;

	@Column(name = "league_rule")
	private String leagueRule;

	/**
	 * 获取ID
	 *
	 *
	 * @return id - ID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置ID
	 *
	 * @param id
	 *            ID
	 */
	public void setId(Integer id) {
		this.id = id;
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

	/**
	 * 获取联赛简称
	 *
	 * @return league_addr - 联赛简称
	 */
	public String getLeagueAddr() {
		return leagueAddr;
	}

	/**
	 * 设置联赛简称
	 *
	 * @param leagueAddr
	 *            联赛简称
	 */
	public void setLeagueAddr(String leagueAddr) {
		this.leagueAddr = leagueAddr;
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

	public Integer getLeagueFrom() {
		return leagueFrom;
	}

	public void setLeagueFrom(Integer leagueFrom) {
		this.leagueFrom = leagueFrom;
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

}
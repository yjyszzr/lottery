package com.dl.shop.lottery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_season_group_data_500w")
public class DlSeasonGroupData500W {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 分组id
	 */
	@Column(name = "match_group_id")
	private Integer matchGroupId;

	/**
	 * 比赛时间
	 */
	@Column(name = "match_time")
	private String matchTime;

	@Column(name = "home_team_id")
	private Integer homeTeamId;

	/**
	 * 主队
	 */
	@Column(name = "home_team")
	private String homeTeam;

	@Column(name = "visitor_team_id")
	private Integer visitorTeamId;

	/**
	 * 客队
	 */
	@Column(name = "visitor_team")
	private String visitorTeam;

	/**
	 * 比分
	 */
	@Column(name = "match_score")
	private String matchScore;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 分组名称
	 */
	@Column(name = "group_name")
	private String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取分组id
	 *
	 * @return match_group_id - 分组id
	 */
	public Integer getMatchGroupId() {
		return matchGroupId;
	}

	/**
	 * 设置分组id
	 *
	 * @param matchGroupId
	 *            分组id
	 */
	public void setMatchGroupId(Integer matchGroupId) {
		this.matchGroupId = matchGroupId;
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
	 * @return home_team_id
	 */
	public Integer getHomeTeamId() {
		return homeTeamId;
	}

	/**
	 * @param homeTeamId
	 */
	public void setHomeTeamId(Integer homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	/**
	 * 获取主队
	 *
	 * @return home_team - 主队
	 */
	public String getHomeTeam() {
		return homeTeam;
	}

	/**
	 * 设置主队
	 *
	 * @param homeTeam
	 *            主队
	 */
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	/**
	 * @return visitor_team_id
	 */
	public Integer getVisitorTeamId() {
		return visitorTeamId;
	}

	/**
	 * @param visitorTeamId
	 */
	public void setVisitorTeamId(Integer visitorTeamId) {
		this.visitorTeamId = visitorTeamId;
	}

	/**
	 * 获取客队
	 *
	 * @return visitor_team - 客队
	 */
	public String getVisitorTeam() {
		return visitorTeam;
	}

	/**
	 * 设置客队
	 *
	 * @param visitorTeam
	 *            客队
	 */
	public void setVisitorTeam(String visitorTeam) {
		this.visitorTeam = visitorTeam;
	}

	/**
	 * 获取比分
	 *
	 * @return match_score - 比分
	 */
	public String getMatchScore() {
		return matchScore;
	}

	/**
	 * 设置比分
	 *
	 * @param matchScore
	 *            比分
	 */
	public void setMatchScore(String matchScore) {
		this.matchScore = matchScore;
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
}
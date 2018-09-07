package com.dl.shop.lottery.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_contry_500w")
public class DlLeagueContry500W {
	/**
	 * ID
	 */
	@Id
	@Column(name="contry_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer contryId;

	/**
	 * 所属组id
	 */
	@Column(name = "group_id")
	private Integer groupId;

	/**
	 * 0:竞彩网, 1:500万
	 */
	@Column(name = "league_from")
	private Integer leagueFrom;

	/**
	 * 联赛国家名称
	 */
	@Column(name = "contry_name")
	private String contryName;
	/**
	 * 联赛国家名称
	 */
	@Column(name = "contry_pic")
	private String contryPic;

	

	public Integer getContryId() {
		return contryId;
	}

	public void setContryId(Integer contryId) {
		this.contryId = contryId;
	}

	/**
	 * 获取所属组id
	 *
	 * @return group_id - 所属组id
	 */
	public Integer getGroupId() {
		return groupId;
	}

	/**
	 * 设置所属组id
	 *
	 * @param groupId
	 *            所属组id
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	/**
	 * 获取联赛国家名称
	 *
	 * @return contry_name - 联赛国家名称
	 */
	public String getContryName() {
		return contryName;
	}

	/**
	 * 设置联赛国家名称
	 *
	 * @param contryName
	 *            联赛国家名称
	 */
	public void setContryName(String contryName) {
		this.contryName = contryName;
	}

	public String getContryPic() {
		return contryPic;
	}

	public void setContryPic(String contryPic) {
		this.contryPic = contryPic;
	}

	public Integer getLeagueFrom() {
		return leagueFrom;
	}

	public void setLeagueFrom(Integer leagueFrom) {
		this.leagueFrom = leagueFrom;
	}

}
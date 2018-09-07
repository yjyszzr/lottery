package com.dl.shop.lottery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_score_500w")
public class DlScore500W {
	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 500w联赛id
	 */
	@Column(name = "l_id")
	private Integer lId;

	/**
	 * (all home away联赛)( A B C...杯赛)
	 */
	private String ty;

	/**
	 * 球队名称
	 */
	@Column(name = "team_name")
	private String teamName;

	/**
	 * 赛场次数
	 */
	@Column(name = "match_num")
	private Integer matchNum;

	/**
	 * 胜场次数
	 */
	@Column(name = "match_h")
	private Integer matchH;

	/**
	 * 平场次数
	 */
	@Column(name = "match_d")
	private Integer matchD;

	/**
	 * 负场次数
	 */
	@Column(name = "match_a")
	private Integer matchA;

	/**
	 * 进球数
	 */
	@Column(name = "ball_in")
	private Integer ballIn;

	/**
	 * 失球数
	 */
	@Column(name = "ball_lose")
	private Integer ballLose;

	/**
	 * 积分
	 */
	private Integer score;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updatetime;

	/**
	 * 排名
	 */
	private Integer rank;

	/**
	 * 球队id
	 */
	@Column(name = "team_id")
	private Integer teamId;

	/**
	 * 赛季id
	 */
	@Column(name = "season_id")
	private Integer seasonId;

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
	 * 获取500w联赛id
	 *
	 * @return l_id - 500w联赛id
	 */
	public Integer getlId() {
		return lId;
	}

	/**
	 * 设置500w联赛id
	 *
	 * @param lId
	 *            500w联赛id
	 */
	public void setlId(Integer lId) {
		this.lId = lId;
	}

	/**
	 * 获取(all home away联赛)( A B C...杯赛)
	 *
	 * @return type - (all home away联赛)( A B C...杯赛)
	 */
	public String getTy() {
		return ty;
	}

	/**
	 * 设置(all home away联赛)( A B C...杯赛)
	 *
	 * @param type
	 *            (all home away联赛)( A B C...杯赛)
	 */
	public void setTy(String ty) {
		this.ty = ty;
	}

	/**
	 * 获取球队名称
	 *
	 * @return team_name - 球队名称
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * 设置球队名称
	 *
	 * @param teamName
	 *            球队名称
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 * 获取赛场次数
	 *
	 * @return match_num - 赛场次数
	 */
	public Integer getMatchNum() {
		return matchNum;
	}

	/**
	 * 设置赛场次数
	 *
	 * @param matchNum
	 *            赛场次数
	 */
	public void setMatchNum(Integer matchNum) {
		this.matchNum = matchNum;
	}

	/**
	 * 获取胜场次数
	 *
	 * @return match_h - 胜场次数
	 */
	public Integer getMatchH() {
		return matchH;
	}

	/**
	 * 设置胜场次数
	 *
	 * @param matchH
	 *            胜场次数
	 */
	public void setMatchH(Integer matchH) {
		this.matchH = matchH;
	}

	/**
	 * 获取平场次数
	 *
	 * @return match_d - 平场次数
	 */
	public Integer getMatchD() {
		return matchD;
	}

	/**
	 * 设置平场次数
	 *
	 * @param matchD
	 *            平场次数
	 */
	public void setMatchD(Integer matchD) {
		this.matchD = matchD;
	}

	/**
	 * 获取负场次数
	 *
	 * @return match_a - 负场次数
	 */
	public Integer getMatchA() {
		return matchA;
	}

	/**
	 * 设置负场次数
	 *
	 * @param matchA
	 *            负场次数
	 */
	public void setMatchA(Integer matchA) {
		this.matchA = matchA;
	}

	/**
	 * 获取进球数
	 *
	 * @return ball_in - 进球数
	 */
	public Integer getBallIn() {
		return ballIn;
	}

	/**
	 * 设置进球数
	 *
	 * @param ballIn
	 *            进球数
	 */
	public void setBallIn(Integer ballIn) {
		this.ballIn = ballIn;
	}

	/**
	 * 获取失球数
	 *
	 * @return ball_lose - 失球数
	 */
	public Integer getBallLose() {
		return ballLose;
	}

	/**
	 * 设置失球数
	 *
	 * @param ballLose
	 *            失球数
	 */
	public void setBallLose(Integer ballLose) {
		this.ballLose = ballLose;
	}

	/**
	 * 获取积分
	 *
	 * @return score - 积分
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * 设置积分
	 *
	 * @param score
	 *            积分
	 */
	public void setScore(Integer score) {
		this.score = score;
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
	 * @return updatetime - 更新时间
	 */
	public Date getUpdatetime() {
		return updatetime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updatetime
	 *            更新时间
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	/**
	 * 获取排名
	 *
	 * @return order - 排名
	 */
	public Integer getRank() {
		return rank;
	}

	/**
	 * 设置排名
	 *
	 * @param order
	 *            排名
	 */
	public void setRank(Integer rank) {
		this.rank = rank;
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

	/**
	 * 获取赛季id
	 *
	 * @return season_id - 赛季id
	 */
	public Integer getSeasonId() {
		return seasonId;
	}

	/**
	 * 设置赛季id
	 *
	 * @param seasonId
	 *            赛季id
	 */
	public void setSeasonId(Integer seasonId) {
		this.seasonId = seasonId;
	}
}
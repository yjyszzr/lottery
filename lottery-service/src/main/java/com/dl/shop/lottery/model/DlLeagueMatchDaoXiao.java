package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_match_daoxiao")
public class DlLeagueMatchDaoXiao {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 赛事场次id
     */
    @Column(name = "changci_id")
    private Integer changciId;

    /**
     * id
     */
    @Column(name = "daoxiao_id")
    private Integer daoxiaoId;

    /**
     * 公司名称
     */
    @Column(name = "com_name")
    private String comName;


    /**
     * 初始奖金大
     */
    @Column(name = "init_win")
    private String initWin;

    /**
     * 初始奖金盘
     */
    @Column(name = "init_draw")
    private String initDraw;

    /**
     * 初始奖金小
     */
    @Column(name = "init_lose")
    private String initLose;

    /**
     * 即时奖金大
     */
    @Column(name = "real_win")
    private String realWin;

    /**
     * 即时奖金盘
     */
    @Column(name = "real_draw")
    private String realDraw;

    /**
     * 即时奖金小
     */
    @Column(name = "real_lose")
    private String realLose;

    /**
     * 初始变化 时间
     */
    @Column(name = "init_time")
    private String initTime;

    /**
     * 即时变化时间
     */
    @Column(name = "real_time")
    private String realTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Integer updateTime;

    /**
     * 拉取平台:0竞彩1 500万
     */
    @Column(name = "league_from")
    private Integer leagueFrom;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getChangciId() {
		return changciId;
	}

	public void setChangciId(Integer changciId) {
		this.changciId = changciId;
	}

	public Integer getDaoxiaoId() {
		return daoxiaoId;
	}

	public void setDaoxiaoId(Integer daoxiaoId) {
		this.daoxiaoId = daoxiaoId;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getInitWin() {
		return initWin;
	}

	public void setInitWin(String initWin) {
		this.initWin = initWin;
	}

	public String getInitDraw() {
		return initDraw;
	}

	public void setInitDraw(String initDraw) {
		this.initDraw = initDraw;
	}

	public String getInitLose() {
		return initLose;
	}

	public void setInitLose(String initLose) {
		this.initLose = initLose;
	}

	public String getRealWin() {
		return realWin;
	}

	public void setRealWin(String realWin) {
		this.realWin = realWin;
	}

	public String getRealDraw() {
		return realDraw;
	}

	public void setRealDraw(String realDraw) {
		this.realDraw = realDraw;
	}

	public String getRealLose() {
		return realLose;
	}

	public void setRealLose(String realLose) {
		this.realLose = realLose;
	}

	public String getInitTime() {
		return initTime;
	}

	public void setInitTime(String initTime) {
		this.initTime = initTime;
	}

	public String getRealTime() {
		return realTime;
	}

	public void setRealTime(String realTime) {
		this.realTime = realTime;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getLeagueFrom() {
		return leagueFrom;
	}

	public void setLeagueFrom(Integer leagueFrom) {
		this.leagueFrom = leagueFrom;
	}

}
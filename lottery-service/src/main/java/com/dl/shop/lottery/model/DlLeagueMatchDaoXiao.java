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
     * 排名
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 初始奖金大
     */
    @Column(name = "init_win")
    private Double initWin;

    /**
     * 初始奖金盘
     */
    @Column(name = "init_draw")
    private Double initDraw;

    /**
     * 初始奖金小
     */
    @Column(name = "init_lose")
    private Double initLose;

    /**
     * 即时奖金大
     */
    @Column(name = "real_win")
    private Double realWin;

    /**
     * 即时奖金盘
     */
    @Column(name = "real_draw")
    private Double realDraw;

    /**
     * 即时奖金小
     */
    @Column(name = "real_lose")
    private Double realLose;

    /**
     * 胜变化趋势:0equal,1up,2down
     */
    @Column(name = "win_change")
    private Integer winChange;

    /**
     * 平变化趋势:0equal,1up,2down
     */
    @Column(name = "draw_change")
    private Integer drawChange;

    /**
     * 负变化趋势:0equal,1up,2down
     */
    @Column(name = "lose_change")
    private Integer loseChange;

    /**
     * 初始变化 时间
     */
    @Column(name = "init_time")
    private Integer initTime;

    /**
     * 即时变化时间
     */
    @Column(name = "real_time")
    private Integer realTime;

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

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取赛事场次id
     *
     * @return changci_id - 赛事场次id
     */
    public Integer getChangciId() {
        return changciId;
    }

    /**
     * 设置赛事场次id
     *
     * @param changciId 赛事场次id
     */
    public void setChangciId(Integer changciId) {
        this.changciId = changciId;
    }

    /**
     * 获取id
     *
     * @return daoxiao_id - id
     */
    public Integer getDaoxiaoId() {
        return daoxiaoId;
    }

    /**
     * 设置id
     *
     * @param daoxiaoId id
     */
    public void setDaoxiaoId(Integer daoxiaoId) {
        this.daoxiaoId = daoxiaoId;
    }

    /**
     * 获取公司名称
     *
     * @return com_name - 公司名称
     */
    public String getComName() {
        return comName;
    }

    /**
     * 设置公司名称
     *
     * @param comName 公司名称
     */
    public void setComName(String comName) {
        this.comName = comName;
    }

    /**
     * 获取排名
     *
     * @return order_num - 排名
     */
    public Integer getOrderNum() {
        return orderNum;
    }

    /**
     * 设置排名
     *
     * @param orderNum 排名
     */
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取初始奖金大
     *
     * @return init_win - 初始奖金大
     */
    public Double getInitWin() {
        return initWin;
    }

    /**
     * 设置初始奖金大
     *
     * @param initWin 初始奖金大
     */
    public void setInitWin(Double initWin) {
        this.initWin = initWin;
    }

    /**
     * 获取初始奖金盘
     *
     * @return init_draw - 初始奖金盘
     */
    public Double getInitDraw() {
        return initDraw;
    }

    /**
     * 设置初始奖金盘
     *
     * @param initDraw 初始奖金盘
     */
    public void setInitDraw(Double initDraw) {
        this.initDraw = initDraw;
    }

    /**
     * 获取初始奖金小
     *
     * @return init_lose - 初始奖金小
     */
    public Double getInitLose() {
        return initLose;
    }

    /**
     * 设置初始奖金小
     *
     * @param initLose 初始奖金小
     */
    public void setInitLose(Double initLose) {
        this.initLose = initLose;
    }

    /**
     * 获取即时奖金大
     *
     * @return real_win - 即时奖金大
     */
    public Double getRealWin() {
        return realWin;
    }

    /**
     * 设置即时奖金大
     *
     * @param realWin 即时奖金大
     */
    public void setRealWin(Double realWin) {
        this.realWin = realWin;
    }

    /**
     * 获取即时奖金盘
     *
     * @return real_draw - 即时奖金盘
     */
    public Double getRealDraw() {
        return realDraw;
    }

    /**
     * 设置即时奖金盘
     *
     * @param realDraw 即时奖金盘
     */
    public void setRealDraw(Double realDraw) {
        this.realDraw = realDraw;
    }

    /**
     * 获取即时奖金小
     *
     * @return real_lose - 即时奖金小
     */
    public Double getRealLose() {
        return realLose;
    }

    /**
     * 设置即时奖金小
     *
     * @param realLose 即时奖金小
     */
    public void setRealLose(Double realLose) {
        this.realLose = realLose;
    }

    /**
     * 获取胜变化趋势:0equal,1up,2down
     *
     * @return win_change - 胜变化趋势:0equal,1up,2down
     */
    public Integer getWinChange() {
        return winChange;
    }

    /**
     * 设置胜变化趋势:0equal,1up,2down
     *
     * @param winChange 胜变化趋势:0equal,1up,2down
     */
    public void setWinChange(Integer winChange) {
        this.winChange = winChange;
    }

    /**
     * 获取平变化趋势:0equal,1up,2down
     *
     * @return draw_change - 平变化趋势:0equal,1up,2down
     */
    public Integer getDrawChange() {
        return drawChange;
    }

    /**
     * 设置平变化趋势:0equal,1up,2down
     *
     * @param drawChange 平变化趋势:0equal,1up,2down
     */
    public void setDrawChange(Integer drawChange) {
        this.drawChange = drawChange;
    }

    /**
     * 获取负变化趋势:0equal,1up,2down
     *
     * @return lose_change - 负变化趋势:0equal,1up,2down
     */
    public Integer getLoseChange() {
        return loseChange;
    }

    /**
     * 设置负变化趋势:0equal,1up,2down
     *
     * @param loseChange 负变化趋势:0equal,1up,2down
     */
    public void setLoseChange(Integer loseChange) {
        this.loseChange = loseChange;
    }

    /**
     * 获取初始变化 时间
     *
     * @return init_time - 初始变化 时间
     */
    public Integer getInitTime() {
        return initTime;
    }

    /**
     * 设置初始变化 时间
     *
     * @param initTime 初始变化 时间
     */
    public void setInitTime(Integer initTime) {
        this.initTime = initTime;
    }

    /**
     * 获取即时变化时间
     *
     * @return real_time - 即时变化时间
     */
    public Integer getRealTime() {
        return realTime;
    }

    /**
     * 设置即时变化时间
     *
     * @param realTime 即时变化时间
     */
    public void setRealTime(Integer realTime) {
        this.realTime = realTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Integer getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Integer getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取拉取平台:0竞彩1 500万
     *
     * @return league_from - 拉取平台:0竞彩1 500万
     */
    public Integer getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置拉取平台:0竞彩1 500万
     *
     * @param leagueFrom 拉取平台:0竞彩1 500万
     */
    public void setLeagueFrom(Integer leagueFrom) {
        this.leagueFrom = leagueFrom;
    }
}
package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_match_europe")
public class DlLeagueMatchEurope {
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
     * 欧赔对应竞彩网id
     */
    @Column(name = "europe_id")
    private Integer europeId;

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
     * 初始奖金胜
     */
    @Column(name = "init_win")
    private Double initWin;

    /**
     * 初始奖金平
     */
    @Column(name = "init_draw")
    private Double initDraw;

    /**
     * 初始奖金负
     */
    @Column(name = "init_lose")
    private Double initLose;

    /**
     * 即时奖金胜
     */
    @Column(name = "real_win")
    private Double realWin;

    /**
     * 即时奖金平
     */
    @Column(name = "real_draw")
    private Double realDraw;

    /**
     * 即时奖金负
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
     * 最新更新时间，以分为单位，如：大于一小时展示用
     */
    @Column(name = "time_minus")
    private Integer timeMinus;

    /**
     * 最新概率胜
     */
    @Column(name = "win_ratio")
    private String winRatio;

    /**
     * 最新概率平
     */
    @Column(name = "draw_ratio")
    private String drawRatio;

    /**
     * 凯利指数负
     */
    @Column(name = "lose_ratio")
    private String loseRatio;

    private String per;

    /**
     * 凯利指数胜
     */
    @Column(name = "win_index")
    private Double winIndex;

    /**
     * 凯利指数平
     */
    @Column(name = "draw_index")
    private Double drawIndex;

    /**
     * 凯利指数负
     */
    @Column(name = "lose_index")
    private Double loseIndex;

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
     * 拉取平台:0竞彩
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
     * 获取欧赔对应竞彩网id
     *
     * @return europe_id - 欧赔对应竞彩网id
     */
    public Integer getEuropeId() {
        return europeId;
    }

    /**
     * 设置欧赔对应竞彩网id
     *
     * @param europeId 欧赔对应竞彩网id
     */
    public void setEuropeId(Integer europeId) {
        this.europeId = europeId;
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
     * 获取初始奖金胜
     *
     * @return init_win - 初始奖金胜
     */
    public Double getInitWin() {
        return initWin;
    }

    /**
     * 设置初始奖金胜
     *
     * @param initWin 初始奖金胜
     */
    public void setInitWin(Double initWin) {
        this.initWin = initWin;
    }

    /**
     * 获取初始奖金平
     *
     * @return init_draw - 初始奖金平
     */
    public Double getInitDraw() {
        return initDraw;
    }

    /**
     * 设置初始奖金平
     *
     * @param initDraw 初始奖金平
     */
    public void setInitDraw(Double initDraw) {
        this.initDraw = initDraw;
    }

    /**
     * 获取初始奖金负
     *
     * @return init_lose - 初始奖金负
     */
    public Double getInitLose() {
        return initLose;
    }

    /**
     * 设置初始奖金负
     *
     * @param initLose 初始奖金负
     */
    public void setInitLose(Double initLose) {
        this.initLose = initLose;
    }

    /**
     * 获取即时奖金胜
     *
     * @return real_win - 即时奖金胜
     */
    public Double getRealWin() {
        return realWin;
    }

    /**
     * 设置即时奖金胜
     *
     * @param realWin 即时奖金胜
     */
    public void setRealWin(Double realWin) {
        this.realWin = realWin;
    }

    /**
     * 获取即时奖金平
     *
     * @return real_draw - 即时奖金平
     */
    public Double getRealDraw() {
        return realDraw;
    }

    /**
     * 设置即时奖金平
     *
     * @param realDraw 即时奖金平
     */
    public void setRealDraw(Double realDraw) {
        this.realDraw = realDraw;
    }

    /**
     * 获取即时奖金负
     *
     * @return real_lose - 即时奖金负
     */
    public Double getRealLose() {
        return realLose;
    }

    /**
     * 设置即时奖金负
     *
     * @param realLose 即时奖金负
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
     * 获取最新更新时间，以分为单位，如：大于一小时展示用
     *
     * @return time_minus - 最新更新时间，以分为单位，如：大于一小时展示用
     */
    public Integer getTimeMinus() {
        return timeMinus;
    }

    /**
     * 设置最新更新时间，以分为单位，如：大于一小时展示用
     *
     * @param timeMinus 最新更新时间，以分为单位，如：大于一小时展示用
     */
    public void setTimeMinus(Integer timeMinus) {
        this.timeMinus = timeMinus;
    }

    /**
     * 获取最新概率胜
     *
     * @return win_ratio - 最新概率胜
     */
    public String getWinRatio() {
        return winRatio;
    }

    /**
     * 设置最新概率胜
     *
     * @param winRatio 最新概率胜
     */
    public void setWinRatio(String winRatio) {
        this.winRatio = winRatio;
    }

    /**
     * 获取最新概率平
     *
     * @return draw_ratio - 最新概率平
     */
    public String getDrawRatio() {
        return drawRatio;
    }

    /**
     * 设置最新概率平
     *
     * @param drawRatio 最新概率平
     */
    public void setDrawRatio(String drawRatio) {
        this.drawRatio = drawRatio;
    }

    /**
     * 获取凯利指数负
     *
     * @return lose_ratio - 凯利指数负
     */
    public String getLoseRatio() {
        return loseRatio;
    }

    /**
     * 设置凯利指数负
     *
     * @param loseRatio 凯利指数负
     */
    public void setLoseRatio(String loseRatio) {
        this.loseRatio = loseRatio;
    }

    /**
     * @return per
     */
    public String getPer() {
        return per;
    }

    /**
     * @param per
     */
    public void setPer(String per) {
        this.per = per;
    }

    /**
     * 获取凯利指数胜
     *
     * @return win_index - 凯利指数胜
     */
    public Double getWinIndex() {
        return winIndex;
    }

    /**
     * 设置凯利指数胜
     *
     * @param winIndex 凯利指数胜
     */
    public void setWinIndex(Double winIndex) {
        this.winIndex = winIndex;
    }

    /**
     * 获取凯利指数平
     *
     * @return draw_index - 凯利指数平
     */
    public Double getDrawIndex() {
        return drawIndex;
    }

    /**
     * 设置凯利指数平
     *
     * @param drawIndex 凯利指数平
     */
    public void setDrawIndex(Double drawIndex) {
        this.drawIndex = drawIndex;
    }

    /**
     * 获取凯利指数负
     *
     * @return lose_index - 凯利指数负
     */
    public Double getLoseIndex() {
        return loseIndex;
    }

    /**
     * 设置凯利指数负
     *
     * @param loseIndex 凯利指数负
     */
    public void setLoseIndex(Double loseIndex) {
        this.loseIndex = loseIndex;
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
     * 获取拉取平台:0竞彩
     *
     * @return league_from - 拉取平台:0竞彩
     */
    public Integer getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置拉取平台:0竞彩
     *
     * @param leagueFrom 拉取平台:0竞彩
     */
    public void setLeagueFrom(Integer leagueFrom) {
        this.leagueFrom = leagueFrom;
    }
}
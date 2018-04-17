package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_match_asia")
public class DlLeagueMatchAsia {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 赛事编号
     */
    @Column(name = "changci_id")
    private Integer changciId;

    /**
     * 亚盘对应竞彩网id
     */
    @Column(name = "asia_id")
    private Integer asiaId;

    /**
     * 公司名称
     */
    @Column(name = "com_name")
    private String comName;

    /**
     * 初始水位
     */
    @Column(name = "init_odds1")
    private Double initOdds1;

    /**
     * 初始盘口
     */
    @Column(name = "init_rule")
    private String initRule;

    /**
     * 初始水位赔率
     */
    @Column(name = "init_odds2")
    private Double initOdds2;

    /**
     * 即时水位
     */
    @Column(name = "real_odds1")
    private Double realOdds1;

    /**
     * 即时盘口
     */
    @Column(name = "real_rule")
    private String realRule;

    /**
     * 即时水位赔率
     */
    @Column(name = "real_odds2")
    private Double realOdds2;

    /**
     * 变化趋势:0equal,1up,2down
     */
    @Column(name = "odds1_change")
    private Integer odds1Change;

    /**
     * 变化趋势:0equal,1up,2down
     */
    @Column(name = "odds2_change")
    private Integer odds2Change;

    /**
     * 最新更新时间，以分为单位，如：大于一小时展示用
     */
    @Column(name = "time_minus")
    private Integer timeMinus;

    /**
     * 最新概率主
     */
    @Column(name = "ratio_h")
    private Double ratioH;

    /**
     * 最新概率客
     */
    @Column(name = "ratio_a")
    private Double ratioA;

    /**
     * 凯利指数主
     */
    @Column(name = "index_h")
    private Double indexH;

    /**
     * 凯利指数客
     */
    @Column(name = "index_a")
    private Double indexA;

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
     * 拉取平台
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

   
    public Integer getChangciId() {
		return changciId;
	}

	public void setChangciId(Integer changciId) {
		this.changciId = changciId;
	}

	/**
     * 获取亚盘对应竞彩网id
     *
     * @return asia_id - 亚盘对应竞彩网id
     */
    public Integer getAsiaId() {
        return asiaId;
    }

    /**
     * 设置亚盘对应竞彩网id
     *
     * @param asiaId 亚盘对应竞彩网id
     */
    public void setAsiaId(Integer asiaId) {
        this.asiaId = asiaId;
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
     * 获取初始水位
     *
     * @return init_odds1 - 初始水位
     */
    public Double getInitOdds1() {
        return initOdds1;
    }

    /**
     * 设置初始水位
     *
     * @param initOdds1 初始水位
     */
    public void setInitOdds1(Double initOdds1) {
        this.initOdds1 = initOdds1;
    }

    /**
     * 获取初始盘口
     *
     * @return init_rule - 初始盘口
     */
    public String getInitRule() {
        return initRule;
    }

    /**
     * 设置初始盘口
     *
     * @param initRule 初始盘口
     */
    public void setInitRule(String initRule) {
        this.initRule = initRule;
    }

    /**
     * 获取初始水位赔率
     *
     * @return init_odds2 - 初始水位赔率
     */
    public Double getInitOdds2() {
        return initOdds2;
    }

    /**
     * 设置初始水位赔率
     *
     * @param initOdds2 初始水位赔率
     */
    public void setInitOdds2(Double initOdds2) {
        this.initOdds2 = initOdds2;
    }

    /**
     * 获取即时水位
     *
     * @return real_odds1 - 即时水位
     */
    public Double getRealOdds1() {
        return realOdds1;
    }

    /**
     * 设置即时水位
     *
     * @param realOdds1 即时水位
     */
    public void setRealOdds1(Double realOdds1) {
        this.realOdds1 = realOdds1;
    }

    /**
     * 获取即时盘口
     *
     * @return real_rule - 即时盘口
     */
    public String getRealRule() {
        return realRule;
    }

    /**
     * 设置即时盘口
     *
     * @param realRule 即时盘口
     */
    public void setRealRule(String realRule) {
        this.realRule = realRule;
    }

    /**
     * 获取即时水位赔率
     *
     * @return real_odds2 - 即时水位赔率
     */
    public Double getRealOdds2() {
        return realOdds2;
    }

    /**
     * 设置即时水位赔率
     *
     * @param realOdds2 即时水位赔率
     */
    public void setRealOdds2(Double realOdds2) {
        this.realOdds2 = realOdds2;
    }

    /**
     * 获取变化趋势:0equal,1up,2down
     *
     * @return odds1_change - 变化趋势:0equal,1up,2down
     */
    public Integer getOdds1Change() {
        return odds1Change;
    }

    /**
     * 设置变化趋势:0equal,1up,2down
     *
     * @param odds1Change 变化趋势:0equal,1up,2down
     */
    public void setOdds1Change(Integer odds1Change) {
        this.odds1Change = odds1Change;
    }

    /**
     * 获取变化趋势:0equal,1up,2down
     *
     * @return odds2_change - 变化趋势:0equal,1up,2down
     */
    public Integer getOdds2Change() {
        return odds2Change;
    }

    /**
     * 设置变化趋势:0equal,1up,2down
     *
     * @param odds2Change 变化趋势:0equal,1up,2down
     */
    public void setOdds2Change(Integer odds2Change) {
        this.odds2Change = odds2Change;
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
     * 获取最新概率主
     *
     * @return ratio_h1 - 最新概率主
     */
    public Double getRatioH() {
        return ratioH;
    }

    /**
     * 设置最新概率主
     *
     * @param ratioH1 最新概率主
     */
    public void setRatioH(Double ratioH) {
        this.ratioH = ratioH;
    }

    /**
     * 获取最新概率客
     *
     * @return ratio_a1 - 最新概率客
     */
    public Double getRatioA() {
        return ratioA;
    }

    /**
     * 设置最新概率客
     *
     * @param ratioA1 最新概率客
     */
    public void setRatioA(Double ratioA) {
        this.ratioA = ratioA;
    }

    /**
     * 获取凯利指数主
     *
     * @return index_ha - 凯利指数主
     */
    public Double getIndexH() {
        return indexH;
    }

    /**
     * 设置凯利指数主
     *
     * @param indexHa 凯利指数主
     */
    public void setIndexH(Double indexH) {
        this.indexH = indexH;
    }

    /**
     * 获取凯利指数客
     *
     * @return index_a1 - 凯利指数客
     */
    public Double getIndexA() {
        return indexA;
    }

    /**
     * 设置凯利指数客
     *
     * @param indexA1 凯利指数客
     */
    public void setIndexA(Double indexA) {
        this.indexA = indexA;
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
     * 获取拉取平台
     *
     * @return league_from - 拉取平台
     */
    public Integer getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置拉取平台
     *
     * @param leagueFrom 拉取平台
     */
    public void setLeagueFrom(Integer leagueFrom) {
        this.leagueFrom = leagueFrom;
    }
}
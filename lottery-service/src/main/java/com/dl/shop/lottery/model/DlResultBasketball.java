package com.dl.shop.lottery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_result_basketball")
public class DlResultBasketball {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 场次id
     */
    @Column(name = "changci_id")
    private Integer changciId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 抓取来源 0 竞彩网 1 500w
     */
    @Column(name = "league_from")
    private Integer leagueFrom;

    /**
     * 赛果数据
     */
    @Column(name = "data_json")
    private String dataJson;

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
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取场次id
     *
     * @return changci_id - 场次id
     */
    public Integer getChangciId() {
        return changciId;
    }

    /**
     * 设置场次id
     *
     * @param changciId 场次id
     */
    public void setChangciId(Integer changciId) {
        this.changciId = changciId;
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
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取抓取来源 0 竞彩网 1 500w
     *
     * @return league_from - 抓取来源 0 竞彩网 1 500w
     */
    public Integer getLeagueFrom() {
        return leagueFrom;
    }

    /**
     * 设置抓取来源 0 竞彩网 1 500w
     *
     * @param leagueFrom 抓取来源 0 竞彩网 1 500w
     */
    public void setLeagueFrom(Integer leagueFrom) {
        this.leagueFrom = leagueFrom;
    }

    /**
     * 获取赛果数据
     *
     * @return data_json - 赛果数据
     */
    public String getDataJson() {
        return dataJson;
    }

    /**
     * 设置赛果数据
     *
     * @param dataJson 赛果数据
     */
    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }
}
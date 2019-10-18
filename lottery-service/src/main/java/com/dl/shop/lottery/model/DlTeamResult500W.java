package com.dl.shop.lottery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_team_result_500w")
public class DlTeamResult500W {
    /**
     * 球队id
     */
    @Id
    @Column(name = "team_id")
    private Integer teamId;

    /**
     * 成立时间
     */
    @Column(name = "team_time")
    private String teamTime;

    /**
     * 球场容量
     */
    @Column(name = "team_capacity")
    private String teamCapacity;

    /**
     * 国家
     */
    private String contry;

    /**
     * 球场
     */
    private String court;

    /**
     * 城市
     */
    private String city;

    /**
     * 球队身价
     */
    @Column(name = "team_value")
    private String teamValue;

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
     * @param teamId 球队id
     */
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    /**
     * 获取成立时间
     *
     * @return team_time - 成立时间
     */
    public String getTeamTime() {
        return teamTime;
    }

    /**
     * 设置成立时间
     *
     * @param teamTime 成立时间
     */
    public void setTeamTime(String teamTime) {
        this.teamTime = teamTime;
    }

    /**
     * 获取球场容量
     *
     * @return team_capacity - 球场容量
     */
    public String getTeamCapacity() {
        return teamCapacity;
    }

    /**
     * 设置球场容量
     *
     * @param teamCapacity 球场容量
     */
    public void setTeamCapacity(String teamCapacity) {
        this.teamCapacity = teamCapacity;
    }

    /**
     * 获取国家
     *
     * @return contry - 国家
     */
    public String getContry() {
        return contry;
    }

    /**
     * 设置国家
     *
     * @param contry 国家
     */
    public void setContry(String contry) {
        this.contry = contry;
    }

    /**
     * 获取球场
     *
     * @return court - 球场
     */
    public String getCourt() {
        return court;
    }

    /**
     * 设置球场
     *
     * @param court 球场
     */
    public void setCourt(String court) {
        this.court = court;
    }

    /**
     * 获取城市
     *
     * @return city - 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取球队身价
     *
     * @return team_value - 球队身价
     */
    public String getTeamValue() {
        return teamValue;
    }

    /**
     * 设置球队身价
     *
     * @param teamValue 球队身价
     */
    public void setTeamValue(String teamValue) {
        this.teamValue = teamValue;
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
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
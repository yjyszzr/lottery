package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_contry")
public class DlLeagueContry {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 所属组id
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 联赛国家名称
     */
    @Column(name = "contry_name")
    private String contryName;

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
     * @param groupId 所属组id
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
     * @param contryName 联赛国家名称
     */
    public void setContryName(String contryName) {
        this.contryName = contryName;
    }
}
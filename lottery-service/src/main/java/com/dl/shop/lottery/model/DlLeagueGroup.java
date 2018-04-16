package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_league_group")
public class DlLeagueGroup {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 组编号
     */
    @Column(name = "group_sn")
    private Integer groupSn;

    /**
     * 组名称
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 组所属于分类:0足联，1篮联
     */
    @Column(name = "league_type")
    private Byte leagueType;

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
     * 获取组编号
     *
     * @return group_sn - 组编号
     */
    public Integer getGroupSn() {
        return groupSn;
    }

    /**
     * 设置组编号
     *
     * @param groupSn 组编号
     */
    public void setGroupSn(Integer groupSn) {
        this.groupSn = groupSn;
    }

    /**
     * 获取组名称
     *
     * @return group_name - 组名称
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置组名称
     *
     * @param groupName 组名称
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 获取组所属于分类:0足联，1篮联
     *
     * @return league_type - 组所属于分类:0足联，1篮联
     */
    public Byte getLeagueType() {
        return leagueType;
    }

    /**
     * 设置组所属于分类:0足联，1篮联
     *
     * @param leagueType 组所属于分类:0足联，1篮联
     */
    public void setLeagueType(Byte leagueType) {
        this.leagueType = leagueType;
    }
}
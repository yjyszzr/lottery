package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_discovery_hall_classify")
public class DlDiscoveryHallClassify {
    /**
     * 大厅资讯分类id
     */
    @Id
    @Column(name = "classify_id")
    private Integer classifyId;

    /**
     * 名称
     */
    @Column(name = "class_name")
    private String className;

    /**
     * img
     */
    @Column(name = "class_img")
    private String classImg;

    /**
     * 副标题
     */
    @Column(name = "sub_title")
    private String subTitle;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态 0-售卖 1-停售 2-未开售
     */
    private Integer status;

    /**
     * 是否显示 0-不显示 1-显示
     */
    @Column(name = "is_show")
    private Integer isShow;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 跳转链接地址
     */
    @Column(name = "redirect_url")
    private String redirectUrl;

    /**
     * 状态说明
     */
    @Column(name = "status_reason")
    private String statusReason;

    /**
     * 1-咨询版 2- 交易版
     */
    @Column(name = "is_transaction")
    private Integer isTransaction;


    /**
     * type 发现的业务类型
     */
    @Column(name = "type")
    private Integer type;


    /**
     * 获取大厅资讯分类id
     *
     * @return classify_id - 大厅资讯分类id
     */
    public Integer getClassifyId() {
        return classifyId;
    }

    /**
     * 设置大厅资讯分类id
     *
     * @param classifyId 大厅资讯分类id
     */
    public void setClassifyId(Integer classifyId) {
        this.classifyId = classifyId;
    }

    /**
     * 获取名称
     *
     * @return class_name - 名称
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置名称
     *
     * @param className 名称
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 获取img
     *
     * @return class_img - img
     */
    public String getClassImg() {
        return classImg;
    }

    /**
     * 设置img
     *
     * @param classImg img
     */
    public void setClassImg(String classImg) {
        this.classImg = classImg;
    }

    /**
     * 获取副标题
     *
     * @return sub_title - 副标题
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * 设置副标题
     *
     * @param subTitle 副标题
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }


    public Integer getIsTransaction() {
        return isTransaction;
    }

    public void setIsTransaction(Integer isTransaction) {
        this.isTransaction = isTransaction;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取状态 0-售卖 1-停售 2-未开售
     *
     * @return status - 状态 0-售卖 1-停售 2-未开售
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态 0-售卖 1-停售 2-未开售
     *
     * @param status 状态 0-售卖 1-停售 2-未开售
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取是否显示 0-不显示 1-显示
     *
     * @return is_show - 是否显示 0-不显示 1-显示
     */
    public Integer getIsShow() {
        return isShow;
    }

    /**
     * 设置是否显示 0-不显示 1-显示
     *
     * @param isShow 是否显示 0-不显示 1-显示
     */
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
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
     * 获取跳转链接地址
     *
     * @return redirect_url - 跳转链接地址
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * 设置跳转链接地址
     *
     * @param redirectUrl 跳转链接地址
     */
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    /**
     * 获取状态说明
     *
     * @return status_reason - 状态说明
     */
    public String getStatusReason() {
        return statusReason;
    }

    /**
     * 设置状态说明
     *
     * @param statusReason 状态说明
     */
    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }
}
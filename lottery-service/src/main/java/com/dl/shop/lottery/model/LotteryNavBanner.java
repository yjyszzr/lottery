package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_nav_banner")
public class LotteryNavBanner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * banner名称
     */
    @Column(name = "banner_name")
    private String bannerName;

    /**
     * banner大图
     */
    @Column(name = "banner_image")
    private String bannerImage;

    /**
     * 图片链接
     */
    @Column(name = "banner_link")
    private String bannerLink;
    
    /**
     * 链接参数列表
     */
    @Column(name = "banner_param")
    private String bannerParam;

    /**
     * 排序
     */
    @Column(name = "banner_sort")
    private Integer bannerSort;

    /**
     * 是否显示 0-不显示 1-显示
     */
    @Column(name = "is_show")
    private Integer isShow;

    @Column(name = "show_position")
    private Integer showPosition;



    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;
    
    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Integer startTime;
    
    
    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Integer endTime;
    
    
    /**
     * 是否交易版
     */
    @Column(name = "is_transaction")
    private String isTransaction; 
    /**
     * 平台名称
     */
    @Column(name = "app_code_name")
    private String appCodeName;
    
    public String getAppCodeName() {
		return appCodeName;
	}

	public void setAppCodeName(String appCodeName) {
		this.appCodeName = appCodeName;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	public String getIsTransaction() {
		return isTransaction;
	}

	public void setIsTransaction(String isTransaction) {
		this.isTransaction = isTransaction;
	}

    public Integer getShowPosition() {
        return showPosition;
    }

    public void setShowPosition(Integer showPosition) {
        this.showPosition = showPosition;
    }

	/**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取banner名称
     *
     * @return banner_name - banner名称
     */
    public String getBannerName() {
        return bannerName;
    }

    /**
     * 设置banner名称
     *
     * @param bannerName banner名称
     */
    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    /**
     * 获取banner大图
     *
     * @return banner_image - banner大图
     */
    public String getBannerImage() {
        return bannerImage;
    }

    /**
     * 设置banner大图
     *
     * @param bannerImage banner大图
     */
    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    /**
     * 获取图片链接
     *
     * @return banner_link - 图片链接
     */
    public String getBannerLink() {
        return bannerLink;
    }

    /**
     * 设置图片链接
     *
     * @param bannerLink 图片链接
     */
    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }
    
	public String getBannerParam() {
		return bannerParam;
	}

	public void setBannerParam(String bannerParam) {
		this.bannerParam = bannerParam;
	}

	/**
     * 获取排序
     *
     * @return banner_sort - 排序
     */
    public Integer getBannerSort() {
        return bannerSort;
    }

    /**
     * 设置排序
     *
     * @param bannerSort 排序
     */
    public void setBannerSort(Integer bannerSort) {
        this.bannerSort = bannerSort;
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
}
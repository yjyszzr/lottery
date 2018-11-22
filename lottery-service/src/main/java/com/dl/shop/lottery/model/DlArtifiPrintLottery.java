package com.dl.shop.lottery.model;

import javax.persistence.*;

@Table(name = "dl_artifi_print_lottery")
public class DlArtifiPrintLottery {
	
	public static final byte ORDER_STATUS_INIT = 0;
	public static final byte ORDER_STATUS_PRINTSUCC = 1;//出票成功
	public static final byte ORDER_STATUS_PRINTFAILURE = 2;//出票失败
	
	public static final byte OPERATION_STATUS_INIT = 0;	//未分配
	public static final byte OPERATION_STATUS_ALLOCATED = 1;//已分配
	
    /**
     * id
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 订单号
     */
    @Column(name = "order_sn")
    private String orderSn;

    /**
     * 状态0待确认1出票成功2出票失败,默认为待确认状态
     */
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 轮询状态:0未轮询,1已轮询,默认未轮询
     */
    @Column(name = "operation_status")
    private Byte operationStatus;

    /**
     * 添加时间
     */
    @Column(name = "add_time")
    private Integer addTime;

    /**
     * 操作人ID
     */
    @Column(name = "admin_id")
    private Integer adminId;

    /**
     * 操作人名称
     */
    @Column(name = "admin_name")
    private String adminName;

    /**
     * 操作时间
     */
    @Column(name = "operation_time")
    private Integer operationTime;

    
    @Column(name = "statistics_print")
    private Integer statisticsPrint;
    
    
	public Integer getStatisticsPrint() {
		return statisticsPrint;
	}

	public void setStatisticsPrint(Integer statisticsPrint) {
		this.statisticsPrint = statisticsPrint;
	}

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
     * 获取订单号
     *
     * @return order_sn - 订单号
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置订单号
     *
     * @param orderSn 订单号
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

	/**
     * 获取状态0待确认1出票成功2出票失败,默认为待确认状态
     *
     * @return order_status - 状态0待确认1出票成功2出票失败,默认为待确认状态
     */
    public Byte getOrderStatus() {
        return orderStatus;
    }

    /**
     * 设置状态0待确认1出票成功2出票失败,默认为待确认状态
     *
     * @param orderStatus 状态0待确认1出票成功2出票失败,默认为待确认状态
     */
    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 获取轮询状态:0未轮询,1已轮询,默认未轮询
     *
     * @return operation_status - 轮询状态:0未轮询,1已轮询,默认未轮询
     */
    public Byte getOperationStatus() {
        return operationStatus;
    }

    /**
     * 设置轮询状态:0未轮询,1已轮询,默认未轮询
     *
     * @param operationStatus 轮询状态:0未轮询,1已轮询,默认未轮询
     */
    public void setOperationStatus(Byte operationStatus) {
        this.operationStatus = operationStatus;
    }

    /**
     * 获取添加时间
     *
     * @return add_time - 添加时间
     */
    public Integer getAddTime() {
        return addTime;
    }

    /**
     * 设置添加时间
     *
     * @param addTime 添加时间
     */
    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    /**
     * 获取操作人ID
     *
     * @return admin_id - 操作人ID
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * 设置操作人ID
     *
     * @param adminId 操作人ID
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * 获取操作人名称
     *
     * @return admin_name - 操作人名称
     */
    public String getAdminName() {
        return adminName;
    }

    /**
     * 设置操作人名称
     *
     * @param adminName 操作人名称
     */
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    /**
     * 获取操作时间
     *
     * @return operation_time - 操作时间
     */
    public Integer getOperationTime() {
        return operationTime;
    }

    /**
     * 设置操作时间
     *
     * @param operationTime 操作时间
     */
    public void setOperationTime(Integer operationTime) {
        this.operationTime = operationTime;
    }
}
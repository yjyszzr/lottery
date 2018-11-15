package com.dl.shop.lottery.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "dl_log_operation")
public class DlOpLog {

	@Id
	@Column(name = "log_id")
	private Integer logId;
	
	/**
	 * 日志类型
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 订单编号
	 */
	@Column(name = "order_sn")
	private String orderSn;

	/**
	 * 操作人
	 */
	@Column(name = "phone")
	private String phone;

	/**
	 * 操作类型 收银台(1收钱，2派奖)  出票系统(1已出票，2出票失败)
	 */
	@Column(name = "op_type")
	private Integer opType;
	
	/**
	 * 日志添加时间
	 */
	@Column(name = "add_time")
	private Integer addTime;
	
	/**
	 * 图片地址
	 */
	@Column(name = "pic")
	private String pic;

	/**
	 * 失败原因
	 */
	@Column(name = "fail_msg")
	private String failMsg;
}

package com.dl.shop.base.dao.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class DDyArtifiPrintEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public long id;
	public String orderSn;
	public int status;//0初始状态 1成功出票 2出票失败
	public int lotteryClassifyId;	//1足彩   2大乐透
}

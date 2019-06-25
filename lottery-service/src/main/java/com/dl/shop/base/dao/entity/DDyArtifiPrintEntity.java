package com.dl.shop.base.dao.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DDyArtifiPrintEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "id")
	public long id;
	
	@ApiModelProperty(value = "订单sn")
	public String orderSn;
	
	@ApiModelProperty(value = "出票状态 0初始状态 1成功出票 2出票失败")
	public int status;//0初始状态 1成功出票 2出票失败
	
	@ApiModelProperty(value = "1足彩  2大乐透")
	public int lotteryClassifyId;	//1足彩   2大乐透
	
	@ApiModelProperty(value = "logo")
	public String logo;

	@ApiModelProperty(value = "混合投注多种玩法标识:1-多种玩法 0-非多种玩法")
	public String mixPlayBz;
}

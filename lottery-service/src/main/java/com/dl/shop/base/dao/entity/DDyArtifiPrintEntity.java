package com.dl.shop.base.dao.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class DDyArtifiPrintEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public long id;
	public String orderSn;
}

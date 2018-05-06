package com.dl.shop.lottery.model;

import lombok.Data;

@Data
public class BetResultInfo {

	private Integer ticketNum = 0;
	private Integer betNum = 0;
	private Double minBonus = 0.0;
	private Double maxBonus = 0.0;
}

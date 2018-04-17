package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DLZQOrderLotteryBetInfoDTO implements Serializable{

	@ApiModelProperty("出票编号")
	private String ticketId;
	@ApiModelProperty("投注彩票列表,投注方案展示")
	private List<DLBetMatchCellDTO> betCells;
	@ApiModelProperty("出票状态， 0-待出票 1-已出票 2-出票失败 3-出票中")
	private Integer status;
	
	public DLZQOrderLotteryBetInfoDTO(){}
	
	public DLZQOrderLotteryBetInfoDTO(String ticketId, List<DLBetMatchCellDTO> betCells){
		this.ticketId = ticketId;
		this.betCells = betCells;
	}
}

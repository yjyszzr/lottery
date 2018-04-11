package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DLZQBetInfoDTO implements Serializable{

	@ApiModelProperty("最大奖金")
	private Double maxBonus;
	@ApiModelProperty("最小奖金")
	private Double minBonus;
	@ApiModelProperty("投注彩票列表,投注方案展示")
	private List<DLBetMatchCellDTO> betCells;
	@ApiModelProperty("倍数")
	private int times;
	@ApiModelProperty("投注数目")
	private int betNum;
	@ApiModelProperty("彩票金额")
	private Double money;
	@ApiModelProperty("投注方式：31")
	private String betType;
	@ApiModelProperty("玩法")
	private String playType;
	/*@ApiModelProperty("彩票")
	private String stakes;*/
	/*@ApiModelProperty("期次")
	private String issue;*/
	@ApiModelProperty("实际的投注场次信息")
	private List<DIZQUserBetCellInfoDTO>  userBetCellInfos;
	@ApiModelProperty("预出票信息列表")
	private List<LotteryPrintDTO> lotteryPrints;
}
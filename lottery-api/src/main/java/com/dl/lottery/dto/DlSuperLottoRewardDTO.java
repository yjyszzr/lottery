package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class DlSuperLottoRewardDTO {

	@ApiModelProperty(value = "Id")
	private Integer id;
	/**
	 * 期次
	 */
	@ApiModelProperty(value = "期次")
	private Integer termNum;
	/**
	 * 奖金级别
	 */
	@ApiModelProperty(value = "奖金级别")
	private Integer rewardLevel;

	/**
	 * 基本中奖注数
	 */
	@ApiModelProperty(value = "基本中奖注数")
	private Integer rewardNum1;

	/**
	 * 基本单注奖金
	 */
	@ApiModelProperty(value = "基本单注奖金")
	private Integer rewardPrice1;

	/**
	 * 追加中奖注数
	 */
	@ApiModelProperty(value = "追加中奖注数")
	private Integer rewardNum2;

	/**
	 * 追加单注奖金
	 */
	@ApiModelProperty(value = "追加单注奖金")
	private Integer rewardPrice2;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Integer createTime;

	/**
	 * 数据源
	 */
	@ApiModelProperty(value = "数据源")
	private Integer platFrom;
}

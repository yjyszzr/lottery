package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveWcBetInfoParam {

	@ApiModelProperty("投注id，用','分隔")
	private String betIds;
	@ApiModelProperty("类型：0冠军，1冠亚军")
	private Integer isGj;
	@ApiModelProperty("倍数")
	private Integer times;
	@ApiModelProperty("红包")
	private Integer bonusId;
	@ApiModelProperty(value="彩票种类")
	private int lotteryClassifyId;
	@ApiModelProperty(value="彩票玩法类别")
	private int lotteryPlayClassifyId;
}

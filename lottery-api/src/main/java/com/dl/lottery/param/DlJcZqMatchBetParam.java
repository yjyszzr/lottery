package com.dl.lottery.param;

import java.io.Serializable;
import java.util.List;

import com.dl.lottery.dto.MatchBetPlayDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlJcZqMatchBetParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="投注倍数")
	private Integer times;
	@ApiModelProperty(value="投注方式,如：31代表3串1")
	private String betType;
	@ApiModelProperty(value="该投票玩法")
	private String playType;
	@ApiModelProperty(value="彩票种类")
	private int lotteryClassifyId;
	@ApiModelProperty(value="彩票玩法类别")
	private int lotteryPlayClassifyId;
	@ApiModelProperty(value="投注赛事详细")
	private List<MatchBetPlayDTO> matchBetPlays;
	@ApiModelProperty(value="用户红包id,如果没有不填写，可以为空")
	private Integer bonusId;
	@ApiModelProperty(value="店铺id")
	private String storeId;
}

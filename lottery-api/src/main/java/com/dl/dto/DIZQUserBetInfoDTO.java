package com.dl.dto;

import java.io.Serializable;
import java.util.List;

import com.dl.param.DlJcZqSaveBetInfoParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DIZQUserBetInfoDTO implements Serializable{

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
	@ApiModelProperty("彩票")
	private String stakes;
	@ApiModelProperty("期次")
	private String issue;
	@ApiModelProperty(value="余额抵扣")
	private Double surplus;
	@ApiModelProperty(value="优惠卷抵扣")
	private Double bonusAmount;
	@ApiModelProperty(value="当前优惠卷")
	private String bonusId;
	@ApiModelProperty(value="需第三方支付")
	private Double thirdPartyPaid;
	/*@ApiModelProperty("实际的投注组合信息（包括加胆后的拆分，该值前端不使用，转给后端下单使用）")
	private List<List<MatchBetCellDTO>>  matchBetList;*/
	
	public DIZQUserBetInfoDTO(DlJcZqSaveBetInfoParam param){
		this.times = param.getTimes();
		this.betNum = param.getBetNum();
		this.money = param.getMoney();
		this.betType = param.getBetType();
		this.playType = param.getPlayType();
		this.stakes = param.getStakes();
		this.issue = param.getIssue();
//		this.matchBetList = param.getMatchBetList();
	}
}

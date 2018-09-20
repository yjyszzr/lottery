package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import com.dl.lottery.param.DlJcLqMatchBetParam;
import com.dl.lottery.param.DlJcZqMatchBetParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户下注信息，暂时存放缓存")
@Data
public class DILQUserBetInfoDTO implements Serializable{

	@ApiModelProperty("倍数")
	private int times;
	@ApiModelProperty("投注方式：31")
	private String betType;
	@ApiModelProperty("玩法")
	private String playType;
	@ApiModelProperty(value="彩票种类")
	private int lotteryClassifyId;
	@ApiModelProperty(value="彩票玩法类别")
	private int lotteryPlayClassifyId;
	@ApiModelProperty("实际的投注场次信息,订单详情")
	private List<DILQUserBetCellInfoDTO>  userBetCellInfos;
	@ApiModelProperty("投注数目")
	private int betNum;
	@ApiModelProperty("彩票金额")
	private Double money;
	@ApiModelProperty("预测奖金")
	private String forecastMoney;
	@ApiModelProperty(value="余额抵扣")
	private Double surplus;
	@ApiModelProperty(value="优惠卷抵扣")
	private Double bonusAmount;
	@ApiModelProperty(value="当前优惠卷")
	private String bonusId;
	@ApiModelProperty(value="需第三方支付")
	private Double thirdPartyPaid;
	@ApiModelProperty(value="1:android,2:ios")
	private String requestFrom;
	@ApiModelProperty(value="用户id")
	private Integer userId;
	@ApiModelProperty("期次")
	private String issue;
	@ApiModelProperty("投注票数")
	private Integer ticketNum;
	
	
	public DILQUserBetInfoDTO(DlJcLqMatchBetParam param){
		this.times = param.getTimes();
		this.betType = param.getBetType();
		this.playType = param.getPlayType();
		this.lotteryClassifyId = param.getLotteryClassifyId();
		this.lotteryPlayClassifyId = param.getLotteryPlayClassifyId();
	}
	public DILQUserBetInfoDTO() {}
}

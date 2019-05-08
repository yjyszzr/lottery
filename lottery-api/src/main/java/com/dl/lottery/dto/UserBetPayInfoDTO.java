package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserBetPayInfoDTO {

    @ApiModelProperty(value="用户id")
    private Integer userId;
    @ApiModelProperty(value="彩票种类")
    private int lotteryClassifyId;
    @ApiModelProperty(value="彩票玩法类别")
    private int lotteryPlayClassifyId;
    @ApiModelProperty("期次")
    private String issue;
    @ApiModelProperty("倍数")
    private int times;
    @ApiModelProperty("投注数目")
    private int betNum;
    @ApiModelProperty("彩票金额")
    private Double orderMoney;
    @ApiModelProperty("预测奖金")
    private String forecastMoney;
    @ApiModelProperty("投注票数")
    private Integer ticketNum;
    @ApiModelProperty("投注方式：31")
    private String betType;
    @ApiModelProperty("玩法")
    private String playType;
    @ApiModelProperty("混合投注具体玩法")
    private String playTypeDetail;


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

    @ApiModelProperty(value="用户投注详情")
    private List<UserBetDetailInfoDTO> betDetailInfos;

}

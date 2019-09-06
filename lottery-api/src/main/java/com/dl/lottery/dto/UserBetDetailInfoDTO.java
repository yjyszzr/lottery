package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBetDetailInfoDTO {

    @ApiModelProperty(value="赛事id")
    private Integer matchId;
    @ApiModelProperty(value = "场次:周三001", required = true)
    public String changci;
    @ApiModelProperty(value="是否设胆，0：否，1是")
    private int isDan;
    @ApiModelProperty(value="彩票种类")
    private int lotteryClassifyId;
    @ApiModelProperty(value="彩票玩法类别")
    private int lotteryPlayClassifyId;
    @ApiModelProperty(value="投注场次队名")
    private String matchTeam;
    @ApiModelProperty(value = "比赛时间")
    public int matchTime;
    @ApiModelProperty(value = "赛事编码")
    public String playCode;
    @ApiModelProperty("让球数")
    private String fixedodds;
    @ApiModelProperty("预设总分")
    private String forecastScore;

    @ApiModelProperty(value="投注选项详情")
    private String ticketData;
    @ApiModelProperty(value = "投注方式：大乐透:0单式，1复式，2胆拖")
    private String betType;
}

package com.dl.shop.lottery.model;

import lombok.Data;
@Data
public class LotteryMatchPlayQdd {
    private Integer matchPlayId;
    private Integer changciId;
    private String playContent;
    private Integer playType;
    private Integer status;
    private Integer isHot;
    private Integer isDel;
    private Integer createTime;
    private Integer updateTime;
    private String matchLiveInfo;
}
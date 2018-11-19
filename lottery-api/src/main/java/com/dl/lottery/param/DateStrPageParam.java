package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DateStrPageParam extends PageParam {
	
    @ApiModelProperty(value = "默认传空字符串,表示查询当天的,当前日期字符串,月日为单数的时候不要带0：格式2018-3-5")
    private String dateStr;
}

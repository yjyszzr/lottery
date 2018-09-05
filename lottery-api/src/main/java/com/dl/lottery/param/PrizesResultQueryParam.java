package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PrizesResultQueryParam {
	
    @ApiModelProperty(value = "彩种：1-足彩 2-大乐透  3-篮彩")
    private String lotteryClassify;
 
    @ApiModelProperty(value = "日期：选择了日期就传，没有日期选择传空字符串")
    private String dateStr;
	
}

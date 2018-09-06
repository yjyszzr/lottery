package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SZCQueryParam {
	
    @ApiModelProperty(value = "彩种：2-大乐透  4-快3  5-双色球  7-广东11选5")
    private String lotteryClassify;
    
	@ApiModelProperty(value = "期号")
	private Integer termNum;
 
    @ApiModelProperty(value = "日期：知道日期则就传，不知道传空字符串")
    private String dateStr;
}    

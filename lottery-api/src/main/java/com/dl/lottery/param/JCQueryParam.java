package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JCQueryParam {
	
    @ApiModelProperty(value = "彩种：1-足彩 3-篮彩")
    private String lotteryClassify;
 
    @ApiModelProperty(value = "日期：知道日期则就传，不知道传空字符串")
    private String dateStr;
}    

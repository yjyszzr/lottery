package com.dl.lottery.param;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JCQueryParam {
	
    @ApiModelProperty(value = "彩种：1-足彩 3-篮彩 6-北京单场")
    @NotNull(message = "彩种不能为空")
    private String lotteryClassify;
 
    @ApiModelProperty(value = "日期：选择了日期就传，没选择传空字符串")
    private String dateStr;
}    

package com.dl.lottery.param;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SZCQueryParam {
	
    @ApiModelProperty(value = "彩种：2-大乐透  4-快3  5-双色球  7-广东11选5")
    @NotNull(message = "彩种不能为空")
    private String lotteryClassify;
    
	@ApiModelProperty(value = "期号")
	@NotNull(message = "期号不能为空")
	private Integer termNum;
 
}    

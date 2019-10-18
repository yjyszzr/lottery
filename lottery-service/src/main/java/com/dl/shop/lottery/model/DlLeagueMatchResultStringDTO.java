package com.dl.shop.lottery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlLeagueMatchResultStringDTO {
	
    
    /**
     * 比赛结果，要与投注的内容保持一致，如:02|201804135004|3，用来与投注结果比较
     */
	@ApiModelProperty("比赛结果字符串")
    private String matchResultString;

}

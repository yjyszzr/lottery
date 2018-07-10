package com.dl.lottery.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 比分筛选类型参数
 *
 * @author zhangzirong
 */
@Data
public class QueryMatchParamByType {
	
    @ApiModelProperty(value = "当前日期字符串,月日为单数的时候不要带0：格式2018-3-5")
    @NotBlank(message = "当前日期不能为空")
    private String dateStr;
    
//    @ApiModelProperty(value = "是否查看已购赛事")
//    private String isAlreadyBuyMatch;
    
    @ApiModelProperty(value = "联赛id集合以,分隔,没有传空字符串")
    private String leagueIds;
    
//    @ApiModelProperty(value = "比赛结束：1-已结束,空字符串-全部 ")
//    private String matchFinish;
    
    @ApiModelProperty(value = "比赛结束：1-已结束,0-未结束,2-我的比赛 ")
    private String type;
    
}


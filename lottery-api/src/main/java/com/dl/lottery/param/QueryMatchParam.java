package com.dl.lottery.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 日期字符串
 *
 * @author zhangzirong
 */
@Data
public class QueryMatchParam {
	
    @ApiModelProperty(value = "当前日期字符串：格式2018-3-5")
    @NotBlank(message = "当前日期不能为空")
    private String dateStr;
    
    @ApiModelProperty(value = "是否查看已购赛事")
    private String isAlreadyBuyMatch;
    
    @ApiModelProperty(value = "联赛id集合:查询条件中没有的时候传空数组")
    @NotBlank(message = "联赛id集合不能为空")
    private String[] leagueIds;
    
    @ApiModelProperty(value = "比赛完成：1-未完成,未选择这个条件的时候传空字符串 ")
    private String matchFinish;
    
}

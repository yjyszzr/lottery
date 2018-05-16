package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HallParam {
	
    @ApiModelProperty(value = "1-资讯版 2-交易版")
    private String isTransaction;
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum = 1;
    @ApiModelProperty(value = "每页展示数")
    private Integer pageSize = 15;
}

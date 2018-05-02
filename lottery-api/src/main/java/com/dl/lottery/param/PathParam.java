package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PathParam {
    @ApiModelProperty(value = "历史赛事的绝对文件路径")
    private String realPath;
}

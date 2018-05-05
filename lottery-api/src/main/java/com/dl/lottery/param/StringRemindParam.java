package com.dl.lottery.param;

import com.dl.member.param.StrParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提醒参数
 *
 * @author zhangzirong
 */
@Data
public class StringRemindParam {

    @ApiModelProperty(value = "历史赛事文件命名为historyMatch.json，放在D盘根目录下，即可执行")
    private String str;
}

package com.dl.lottery.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DateStrParam {
	
    @ApiModelProperty(value = "当前日期字符串,月日为单数的时候不要带0：格式2018-3-5")
    @NotBlank(message = "当前日期不能为空")
    private String dateStr;

}

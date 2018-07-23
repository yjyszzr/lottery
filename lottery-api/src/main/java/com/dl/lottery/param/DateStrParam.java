package com.dl.lottery.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DateStrParam {
	
    @ApiModelProperty(value = "当前日期字符串,月日为单数的时候不要带0：格式2018-3-5")
    private String dateStr;

}

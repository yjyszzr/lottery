package com.dl.lottery.dto;

import com.dl.lottery.enums.MatchLiveDataEnums;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchLiveTeamDataDTO {

	@ApiModelProperty("主队数据")
	private String teamHData;
	@ApiModelProperty("客队数据")
	private String teamAData;
	@ApiModelProperty("数据类型:0数字1百分比")
	private int dataType=0;
	@ApiModelProperty("数据名称")
	private String dataName;
	@ApiModelProperty("数据编码")
	private String dataCode;
	
	public void setData(MatchLiveDataEnums dataEnum) {
		if(dataEnum != null) {
			this.dataType = dataEnum.getType();
			this.dataCode = dataEnum.getCode();
			this.dataName = dataEnum.getMsg();
		}
	}
}

package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperationRecordDTO {
	@ApiModelProperty(value = "成功出票数")
	private String sucNum;
	
	@ApiModelProperty(value = "失败出票数")
	private String failNum;
	
	@ApiModelProperty(value = "成功出票金额")
	private String sucMoney;
	
	@ApiModelProperty(value = "出票记录集合")
	private List<DlOpLogDTO> opList;

}

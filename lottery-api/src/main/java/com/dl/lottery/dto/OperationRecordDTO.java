package com.dl.lottery.dto;


import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperationRecordDTO {
	@ApiModelProperty(value = "成功出票数")
	private String sucNum = "0";
	
	@ApiModelProperty(value = "失败出票数")
	private String failNum = "0";
	
	@ApiModelProperty(value = "成功出票金额")
	private String sucMoney = "0";
	
	@ApiModelProperty(value = "出票记录集合")
	private PageInfo<DlOpLogDTO> printRecord = new PageInfo<DlOpLogDTO>();

	public void setOpList(PageInfo<DlOpLogDTO> printRecordList) {
		this.printRecord = printRecordList;
	}

}

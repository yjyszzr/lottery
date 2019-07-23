package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="玩法类型下的选项信息")
@Data
public class DlJcLqMatchCellDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "选项编码(投注号码)", required = true)
	public String cellCode;
	
	@ApiModelProperty(value = "选项名称", required = true)
	public String cellName;
	
	@ApiModelProperty(value = "选项赔率", required = true)
	public String cellOdds;
	
	public Double cellOddsD;
	
	public Double getCellOddsD() {
		return Double.parseDouble(cellOdds);
	}


	@ApiModelProperty(value = "子选项,比分类型用到该属性", required = true)
	public List<DlJcLqMatchCellDTO> cellSons;
	
	public DlJcLqMatchCellDTO(String cellCode, String cellName, String cellOdds){
		this.cellCode = cellCode;
		this.cellName = cellName;
		this.cellOdds = cellOdds;
	}
	
	public DlJcLqMatchCellDTO() {}
}

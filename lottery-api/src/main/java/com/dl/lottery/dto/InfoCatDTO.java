package com.dl.lottery.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InfoCatDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "分类：")
	public String cat;
	
	@ApiModelProperty(value = "分类名称")
	public String catName;
	
	
	
}

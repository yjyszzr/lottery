package com.dl.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlPlayClassifyParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "彩票分类id", required = true)
	private String lotteryClassifyId;
}

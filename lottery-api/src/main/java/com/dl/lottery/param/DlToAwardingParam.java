package com.dl.lottery.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlToAwardingParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "期次（如：20180403001）", required = true)
    private String issue;
}

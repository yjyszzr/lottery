package com.dl.lottery.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlRewardParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "场次id", required = true)
    private String changCiId;
}

package com.dl.lottery.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SupperLottoParam implements Serializable{

	@ApiModelProperty(value="期次号")
	Integer termNum;
	
}

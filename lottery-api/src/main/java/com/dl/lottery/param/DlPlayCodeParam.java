package com.dl.lottery.param;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlPlayCodeParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "Id")
	private List<String> playCodes;
}

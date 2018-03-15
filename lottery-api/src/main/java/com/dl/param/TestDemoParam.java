package com.dl.param;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("测试接口传参")
@Data
public class TestDemoParam implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "参数")
	@ApiModelProperty(value = "参数", required = true)
    private Integer param;
	
}

package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CatArticleParam {
	
	@ApiModelProperty(value="页码")
	private Integer page;
	@ApiModelProperty(value="每页显示数量")
	private Integer size;	
	@ApiModelProperty(value="资讯分类")
	private String extendCat;
	
}

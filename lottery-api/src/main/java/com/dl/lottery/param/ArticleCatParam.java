package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleCatParam {
	
	@ApiModelProperty(value="页码")
	Integer page;
	@ApiModelProperty(value="每页显示数量，默认10")
	Integer size;
	
	@ApiModelProperty(value="当前文章id")
	private String currentArticleId;

}

package com.dl.lottery.param;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleIdsParam {
	
	@ApiModelProperty(value="文章id集合")
	@NotEmpty
	private List<Integer> articleIds;

}

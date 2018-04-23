package com.dl.lottery.dto;

import java.io.Serializable;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlHallMixDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "大厅首页数据对象")
	public DlHallDTO dlHallDTO;
	
	@ApiModelProperty(value = "咨询列表对象")
	public PageInfo<DLArticleDTO> dlArticlePage;
}

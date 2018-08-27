package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.github.pagehelper.PageInfo;

@Data
public class DlDiscoveryPageDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "分类标题")
	List<DlDiscoveryHallClassifyDTO> discoveryHallClassifyList;

	@ApiModelProperty(value = "资讯列表")
	public PageInfo<DLArticleDTO> dlArticlePage;
}

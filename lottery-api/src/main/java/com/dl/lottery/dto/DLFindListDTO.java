package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import com.dl.lottery.dto.DlHallDTO.DlNavBannerDTO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DLFindListDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "咨讯分类标题")
	public List<InfoCatDTO> infoCatList;	
	
	@ApiModelProperty(value = "咨讯轮播图列表")
	public List<DlNavBannerDTO> navBanners;
	
	@ApiModelProperty(value = "重要新闻列表")
	public List<DLArticleDTO> bigNewsList;
	
	@ApiModelProperty(value = "资讯列表")
	public PageInfo<DLArticleDTO> dlArticlePage;
}

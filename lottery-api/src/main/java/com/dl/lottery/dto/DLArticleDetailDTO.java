package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DLArticleDetailDTO implements Serializable {

	@ApiModelProperty(value = "")
	private Integer articleId;

	@ApiModelProperty(value = "文章标题")
	private String title;
	@ApiModelProperty(value = "关键字")
	private String keywords;
	@ApiModelProperty(value = "作者")
	private String author;
	@ApiModelProperty(value = "文章缩略图，用‘，’分隔")
	private String articleThumb;
	@ApiModelProperty(value = "富文本内容")
	private String content;
	@ApiModelProperty(value = "发布时间")
	private String addTime;
	@ApiModelProperty(value = "阅读量")
	private Integer clickNumber;
	@ApiModelProperty(value = "转向链接:分享的时使用")
	private String link;
	@ApiModelProperty(value = "文章摘要")
	private String summary;
	@ApiModelProperty(value = "分类")
	private String extendCat;
	@ApiModelProperty(value = "比赛id")
	private Integer matchId;
	@ApiModelProperty(value = "主队- 1  客队 - 2")
	private String relatedTeam;
	@ApiModelProperty(value = "列表展示形式 1- 单张图  2-两张图  3-三张图")
	private Integer listStyle;
	@ApiModelProperty(value = "标签")
	private List<String> labelsArr;

	@ApiModelProperty(value = "0-已收藏  1-未收藏")
	private String isCollect;

	@ApiModelProperty(value = "相关文章：默认展示3条")
	private List<DLArticleDTO> articles;

}

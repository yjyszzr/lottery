package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import com.dl.member.dto.UserBonusDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DLArticleDTO implements Serializable{

	@ApiModelProperty(value="")
	private Integer articleId;

	@ApiModelProperty(value="文章标题")
	private String title;
	@ApiModelProperty(value="关键字")
	private String keywords;
	@ApiModelProperty(value="文章缩略图，用‘，’分隔")
	private List<String> articleThumb;
	@ApiModelProperty(value="发布时间")
	private String addTime;
	@ApiModelProperty(value="阅读量")
	private Integer clickNumber;
	@ApiModelProperty(value="转向链接")
	private String link;
	@ApiModelProperty(value="文章摘要")
	private String summary;
	@ApiModelProperty(value="分类")
	private String extendCat;
	@ApiModelProperty(value="比赛id")
	private Integer matchId;
	@ApiModelProperty(value="主队- 1  客队 - 2")
	private String relatedTeam;
	@ApiModelProperty(value="列表展示形式 1- 单张图  2-两张图  3-三张图")
	private Integer listStyle;


}

package com.dl.lottery.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlPlayClassifyDetailDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "彩票分类id")
	public String lotteryId;
	
	@ApiModelProperty(value = "玩法名称")
	public String playClassifyName;
	
	@ApiModelProperty(value = "玩法图片")
	public String playClassifyImg;
	
	@ApiModelProperty(value = "投注类型")
	public String playType;
	
	@ApiModelProperty(value = "玩法id")
	public String playClassifyId;
	
	@ApiModelProperty(value = "玩法标签id")
	public String playClassifyLabelId;
	
	@ApiModelProperty(value = "玩法标签名称")
	public String playClassifyLabelName;
	
	@ApiModelProperty(value = "副标题")
	public String subTitle;
	
	@ApiModelProperty(value = "跳转链接地址")
	public String redirectUrl;
}

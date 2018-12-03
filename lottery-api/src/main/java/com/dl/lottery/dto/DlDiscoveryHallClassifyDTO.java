package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlDiscoveryHallClassifyDTO {

	@ApiModelProperty("发现页分类id")
	private String classifyId;

	@ApiModelProperty("发现页业务类型:1-开奖结果  2-专家推荐 3-彩票学堂 4-活动中心 7-联赛资料 9-比分直播 10-线下店铺 11-查看更多")
	private String type;
	
	@ApiModelProperty("名称")
	private String className;

	@ApiModelProperty("logo")
	private String classImg;

	@ApiModelProperty("是否上线 0 待上线,1上线")
	private Integer status;

	@ApiModelProperty("状态说明")
	private String statusReason;

	@ApiModelProperty("跳转链接")
	private String redirectUrl;

	public DlDiscoveryHallClassifyDTO(){

	}

	public DlDiscoveryHallClassifyDTO(String classifyId, String type, String className, String classImg, Integer status, String statusReason, String redirectUrl) {
		this.classifyId = classifyId;
		this.type = type;
		this.className = className;
		this.classImg = classImg;
		this.status = status;
		this.statusReason = statusReason;
		this.redirectUrl = redirectUrl;
	}


}
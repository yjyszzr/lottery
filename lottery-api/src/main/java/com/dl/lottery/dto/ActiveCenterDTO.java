package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class ActiveCenterDTO {
	@ApiModelProperty(value = "进行中的活动")
	List<DlBannerForActive> onlineList;

	@ApiModelProperty(value = "已结束的活动")
	List<DlBannerForActive> offlineList;

}

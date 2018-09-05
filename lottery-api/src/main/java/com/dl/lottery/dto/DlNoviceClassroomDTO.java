package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
public class DlNoviceClassroomDTO {
	@ApiModelProperty(value = "小白课堂")
	List<DlBannerForActive> noviceClassroomList;

}

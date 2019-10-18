package com.dl.lottery.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlPlayerInfosDTO {

	@ApiModelProperty(value = "球员类型Code:0守门员，1后卫，2中场，3前锋")
	private Integer playerTypeCode;
	@ApiModelProperty(value = "球员类型:0守门员，1后卫，2中场，3前锋")
	private String playerType;
	@ApiModelProperty(value = "球员列表")
	private List<DlPlayerInfoDTO> playerList;
	
	public DlPlayerInfosDTO(){}
	
	public DlPlayerInfosDTO(Integer playerTypeCode, String playerType){
		this.playerTypeCode = playerTypeCode;
		this.playerType = playerType;
	}
	
}

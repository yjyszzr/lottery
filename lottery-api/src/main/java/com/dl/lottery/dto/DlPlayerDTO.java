package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlPlayerDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "球员列表")
	private List<DlPlayerInfosDTO> playerInfosList;

	@Data
	public static class DlPlayerInfosDTO {
		@ApiModelProperty(value = "球员类型Code:0守门员，1后卫，2中场，3前锋")
		public Integer playerTypeCode;
		@ApiModelProperty(value = "球员类型:0守门员，1后卫，2中场，3前锋")
		public String playerType;
		@ApiModelProperty(value = "球员列表")
		public List<DlPlayerInfoDTO> playerList;
	}

	@Data
	public static class DlPlayerInfoDTO {
		@ApiModelProperty(value = "球员名称")
		public String playerName;
	}
}

package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlPlayerDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "球员类型Code")
	public String playerTypeCode;
	@ApiModelProperty(value = "球员类型")
	public String playerType;
	@ApiModelProperty(value = "球员列表")
	public List<DlPlayerInfoDTO> playerList;

	@Data
	public static class DlPlayerInfoDTO {
		@ApiModelProperty(value = "球员名称")
		public String PlayerName;
	}
}

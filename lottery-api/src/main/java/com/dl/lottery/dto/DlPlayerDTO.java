package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlPlayerDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "守门员列表")
	private DlPlayerInfosDTO goalKeepers = new DlPlayerInfosDTO(0, "守门员");
	
	@ApiModelProperty(value = "后卫列表")
	private DlPlayerInfosDTO backPlayers = new DlPlayerInfosDTO(1, "后卫");
	
	@ApiModelProperty(value = "中场列表")
	private DlPlayerInfosDTO midPlayers = new DlPlayerInfosDTO(2, "中场");
	
	@ApiModelProperty(value = "前锋列表")
	private DlPlayerInfosDTO forwards = new DlPlayerInfosDTO(3, "前锋");

}

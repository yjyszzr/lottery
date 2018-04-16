package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlPlayClassifyDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "玩法抬头")
	public DlPlayTitleDTO dlPlayTitleDTO;

	@ApiModelProperty(value = "玩法分类明细")
	public List<DlPlayClassifyDetailDTO> dlPlayClassifyDetailDTOs;
	
	@Data
	public static class DlPlayTitleDTO {
		
		@ApiModelProperty(value = "玩法名称")
		public String playName;
	}
}

package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlDiscoveryPageDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "分类List")
	List<DlDiscoveryHallClassifyDTO> discoveryHallClassifyList;
	@ApiModelProperty(value = "联赛list")
	List<DLHotLeagueDTO> hotLeagueList;
	@ApiModelProperty(value = "射手榜list")
	List<DlTopScorerDTO> topScorerDTOList;

}

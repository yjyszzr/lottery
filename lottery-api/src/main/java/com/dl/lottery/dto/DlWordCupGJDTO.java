package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlWordCupGJDTO {
	
	@ApiModelProperty(value="唯一编号")
	private Integer gjId;
	@ApiModelProperty(value="国家id")
	private Integer countryId;
	@ApiModelProperty(value="国家名称")
	private String contryName;
	@ApiModelProperty(value="国家图标")
	private String contryPic;
	@ApiModelProperty(value="奖金")
	private String betOdds;
	@ApiModelProperty(value="编号")
	private Integer sortId;
	@ApiModelProperty(value="玩法，投注用")
	private String game;
	@ApiModelProperty(value="期次号，投注用")
    private String issue;
	@ApiModelProperty(value="运营状态，0开售1停售")
	private Integer isSell;
	@ApiModelProperty(value="竞彩网状态，0开售1停售")
	private Integer betStatus;
	
}

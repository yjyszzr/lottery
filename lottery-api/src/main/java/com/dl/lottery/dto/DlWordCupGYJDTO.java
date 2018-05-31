package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlWordCupGYJDTO {
	
	@ApiModelProperty(value="唯一编号")
	private Integer gyjId;
	@ApiModelProperty(value="国家id")
	private Integer homeCountryId;
	@ApiModelProperty(value="国家名称")
	private String homeContryName;
	@ApiModelProperty(value="国家图标")
	private String homeContryPic;
	@ApiModelProperty(value="国家id")
	private Integer visitorCountryId;
	@ApiModelProperty(value="国家名称")
	private String visitorContryName;
	@ApiModelProperty(value="国家图标")
	private String visitorContryPic;
	@ApiModelProperty(value="奖金")
	private String betOdds;
	@ApiModelProperty(value="编号")
	private Integer sortId;
	@ApiModelProperty(value="玩法，投注用")
	private String game;
	@ApiModelProperty(value="期次号，投注用")
    private String issue;
	
}

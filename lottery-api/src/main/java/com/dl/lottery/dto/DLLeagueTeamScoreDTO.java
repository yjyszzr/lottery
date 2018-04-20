package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import com.dl.member.dto.UserBonusDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DLLeagueTeamScoreDTO implements Serializable{

	@ApiModelProperty(value="")
	 private Integer teamId;

	@ApiModelProperty(value="球队名称")
    private String teamName;

	@ApiModelProperty(value="比赛场次数")
    private Integer matchNum;

	@ApiModelProperty(value="胜场次数")
    private Integer matchH;

	@ApiModelProperty(value="平场次数")
    private Integer matchD;

	@ApiModelProperty(value="负场次数")
    private Integer matchL;

	@ApiModelProperty(value="进球数")
    private Integer ballIn;

	@ApiModelProperty(value="失球数")
    private Integer ballLose;

	@ApiModelProperty(value="净球数")
    private Integer ballClean;

	@ApiModelProperty(value="均得")
    private Double preH;

	@ApiModelProperty(value="均失")
    private Double preL;

	@ApiModelProperty(value="胜率")
    private String ratioH;

	@ApiModelProperty(value="平率")
    private String ratioD;

	@ApiModelProperty(value="负率")
    private String ratioL;

	@ApiModelProperty(value="积分")
    private Integer score;

	@ApiModelProperty(value="0总1主2客")
    private Integer flag;
	
	@ApiModelProperty(value="排名")
	private Integer rank;

}

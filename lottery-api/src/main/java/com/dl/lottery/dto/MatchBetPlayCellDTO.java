package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatchBetPlayCellDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="赛事id")
	private Integer matchId;
	@ApiModelProperty(value = "场次:周三001", required = true)
	public String changci;
	@ApiModelProperty(value="是否设胆，0：否，1是")
	private int isDan;
	@ApiModelProperty(value="彩票种类")
	private int lotteryClassifyId;
	@ApiModelProperty(value="彩票玩法类别")
	private int lotteryPlayClassifyId;
	@ApiModelProperty(value="投注场次队名,如：中国VS日本")
	private String matchTeam;
	@ApiModelProperty(value = "比赛时间")
	public int matchTime;
	@ApiModelProperty(value="投注赛事编码")
	private String playCode;
	@ApiModelProperty(value="该场次玩法")
	private String playType;
	@ApiModelProperty(value="投注选项")
	private List<DlJcZqMatchCellDTO> betCells;
	
	public MatchBetPlayCellDTO() {}
	
	public MatchBetPlayCellDTO(MatchBetPlayDTO betPlayDto) {
		this.matchId = betPlayDto.getMatchId();
		this.changci = betPlayDto.getChangci();
		this.isDan = betPlayDto.getIsDan();
		this.lotteryClassifyId = betPlayDto.getLotteryClassifyId();
		this.lotteryPlayClassifyId = betPlayDto.getLotteryPlayClassifyId();
		this.matchTeam = betPlayDto.getMatchTeam();
		this.matchTime = betPlayDto.getMatchTime();
		this.playCode = betPlayDto.getPlayCode();
	}
}

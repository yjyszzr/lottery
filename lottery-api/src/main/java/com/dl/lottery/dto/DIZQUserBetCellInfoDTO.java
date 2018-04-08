package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户下注场次信息，暂时存放缓存")
@Data
public class DIZQUserBetCellInfoDTO implements Serializable{

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
	@ApiModelProperty(value="投注选项详情")
	private String ticketData;
	@ApiModelProperty(value="投注场次队名")
	private String matchTeam;
	@ApiModelProperty(value = "比赛时间")
	public int matchTime;
	@ApiModelProperty(value = "赛事编码")
	public String playCode;
	
	public DIZQUserBetCellInfoDTO(MatchBetCellDTO matchCell){
		this.matchId = matchCell.getMatchId();
		this.changci = matchCell.getChangci();
		this.isDan = matchCell.getIsDan();
		this.lotteryClassifyId = matchCell.getLotteryClassifyId();
		this.lotteryPlayClassifyId = matchCell.getLotteryPlayClassifyId();
		this.matchTeam = matchCell.getMatchTeam();
		this.matchTime = matchCell.getMatchTime();
		this.playCode = matchCell.getPlayCode();
		String ticketData = matchCell.getPlayType() + "|" + matchCell.getPlayCode() + "|";
		this.ticketData = ticketData + matchCell.getBetCells().stream().map(cell->cell.getCellCode()+"@"+cell.getCellOdds())
		.collect(Collectors.joining(";"));
	}
}

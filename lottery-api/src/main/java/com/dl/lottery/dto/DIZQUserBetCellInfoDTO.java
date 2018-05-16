package com.dl.lottery.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

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
	@ApiModelProperty("让球数")
	private String fixedodds;
	
	public DIZQUserBetCellInfoDTO() {}
	
	public DIZQUserBetCellInfoDTO(MatchBetPlayDTO matchCell){
		this.matchId = matchCell.getMatchId();
		this.changci = matchCell.getChangci();
		this.isDan = matchCell.getIsDan();
		this.lotteryClassifyId = matchCell.getLotteryClassifyId();
		this.lotteryPlayClassifyId = matchCell.getLotteryPlayClassifyId();
		this.matchTeam = matchCell.getMatchTeam();
		this.matchTime = matchCell.getMatchTime();
		this.playCode = matchCell.getPlayCode();
		List<MatchBetCellDTO> matchBetCells = matchCell.getMatchBetCells();
		this.ticketData = matchBetCells.stream().map(betCell->{
			String ticketData = "0" + betCell.getPlayType() + "|" + playCode + "|";
			return ticketData + betCell.getBetCells().stream().map(cell->cell.getCellCode()+"@"+cell.getCellOdds())
					.collect(Collectors.joining(","));
		}).collect(Collectors.joining(";"));
	}
}

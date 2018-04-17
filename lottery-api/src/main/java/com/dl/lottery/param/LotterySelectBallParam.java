package com.dl.lottery.param;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("获取用户选择数据")
@Data
public class LotterySelectBallParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户选择数据", required = true)
    private List<SelectData> selectBallParam;
	
	@Data
	public static class SelectData {
		
		@ApiModelProperty(value = "赛事id", required = true)
		private String matchId;
		
		@ApiModelProperty(value = "比赛结果", required = true)
		private Vector<String> matchResult;
		
	}
}

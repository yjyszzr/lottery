package com.dl.lottery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetMatchLiveInfoParam {

	@ApiModelProperty(value = "赛事id")
	private Integer matchId;
}

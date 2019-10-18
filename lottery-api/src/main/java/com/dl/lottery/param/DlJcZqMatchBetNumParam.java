package com.dl.lottery.param;

import java.io.Serializable;
import java.util.List;

import com.dl.lottery.dto.DlJcZqDateMatchDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlJcZqMatchBetNumParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "投注号码：0:3,1|1:1,3|0:1,每个场次有|来区分，同场次的不同选项有‘,’区分，每个场次部分‘：’前代表是否设胆（0：不设胆，1设胆），‘:’后面是场次选项", required = true)
    private String stakes;
	@ApiModelProperty(value="玩法类型,如：31代表3串1")
	private String playType;
	
	private List<DlJcZqDateMatchDTO> playList;
}

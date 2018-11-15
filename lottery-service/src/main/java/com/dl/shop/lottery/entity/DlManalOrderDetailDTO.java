package com.dl.shop.lottery.entity;

import com.dl.member.dto.MediaTokenDTO;
import com.dl.order.dto.ManualOrderDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlManalOrderDetailDTO {
	@ApiModelProperty(value="订单信息token")
	private ManualOrderDTO detail;
	
	@ApiModelProperty(value="多媒体token")
	private MediaTokenDTO mediaToken;
}

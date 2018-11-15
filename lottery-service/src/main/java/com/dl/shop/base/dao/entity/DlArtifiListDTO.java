package com.dl.shop.base.dao.entity;

import java.io.Serializable;
import java.util.List;
import com.dl.member.dto.MediaTokenDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlArtifiListDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="列表")
	private List<DDyArtifiPrintEntity> list;
	
	@ApiModelProperty(value="多媒体token")
	private MediaTokenDTO mediaToken;
}

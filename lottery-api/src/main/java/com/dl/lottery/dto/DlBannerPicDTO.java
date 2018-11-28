package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlBannerPicDTO {

    @ApiModelProperty(value = "banner名称")
    public String bannerName;

    @ApiModelProperty(value = "banner大图")
    public String bannerImage;

    @ApiModelProperty(value = "图片跳转链接")
    public String bannerLink;

    @ApiModelProperty(value = "banner图生效起始时间")
    public Integer startTime;

    @ApiModelProperty(value = "banner图生效截止时间")
    public Integer endTime;

    public DlBannerPicDTO(String bannerName,String bannerImage,String bannerLink,Integer startTime,Integer endTime){
        this.bannerName = bannerName;
        this.bannerImage = bannerImage;
        this.bannerLink = bannerLink;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}

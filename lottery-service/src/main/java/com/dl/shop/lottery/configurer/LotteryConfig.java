package com.dl.shop.lottery.configurer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


import lombok.Data;

@Data
@Configuration
public class LotteryConfig {
	
	//文章分享的url
	@Value("${dl.share.info.url}")
	private String shareInfoUrl;

}

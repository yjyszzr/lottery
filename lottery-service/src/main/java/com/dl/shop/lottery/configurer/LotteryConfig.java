package com.dl.shop.lottery.configurer;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class LotteryConfig {

	// 文章分享的url
	@Value("${dl.share.info.url}")
	private String shareInfoUrl;

	// banner展示
	@Value("${dl.banner.show.url}")
	private String bannerShowUrl;

	// banner链接赛事URL
	@Value("${dl.banner.linkMatch.url}")
	private String banneLinkMatchUrl;

	// banner链接资讯URL
	@Value("${dl.banner.linkArticle.url}")
	private String banneLinkArticleUrl;

}

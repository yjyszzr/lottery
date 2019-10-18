package com.dl.shop.lottery.configurer;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class DataBaseCfg {

	// 数据库链接驱动
	@Value("${spring.datasource1.druid.driver-class-name}")
	private String driver;

	@Value("${spring.datasource1.druid.url}")
	private String url;

	@Value("${spring.datasource1.druid.username}")
	private String userName;

	@Value("${spring.datasource1.druid.password}")
	private String userPass;
}

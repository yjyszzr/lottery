package com.dl.shop.base.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
public class DataBaseCfg {

	//数据库链接驱动
	@Value("${spring.datasource1.druid.driver-class-name}")
	private String driver;

	@Value("${spring.datasource1.druid.url}")
	private String url;
	
	
	@Value("${spring.datasource1.druid.username}")
	private String userName;
	

	@Value("${spring.datasource1.druid.password}")
	private String userPass;
}

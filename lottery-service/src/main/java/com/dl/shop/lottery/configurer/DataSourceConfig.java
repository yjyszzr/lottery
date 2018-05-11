package com.dl.shop.lottery.configurer;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DataSourceConfig {

	@Bean(name = "dataSource1")
	@Primary
	// application.properteis中对应属性的前缀
	@ConfigurationProperties(prefix = "spring.datasource1.druid")
	public DataSource dataSource1() {
		return DataSourceBuilder.create().type(DruidDataSource.class).build();
	}

	@Bean(name = "dataSource2")
	// application.properteis中对应属性的前缀
	@ConfigurationProperties(prefix = "spring.datasource2.druid")
	public DataSource dataSource2() {
		return DataSourceBuilder.create().type(DruidDataSource.class).build();
	}
	
	@Value("${mybatis.mapper-locations}")
	private String mybatisMapperLocations;
	@Value("${mybatis.type-aliases-package}")
	private String mybatisTypeAliasesPackage;
	
	public String getMybatisMapperLocations(){
		return mybatisMapperLocations;
	}
	
	public String getMybatisTypeAliasesPackage(){
		return mybatisTypeAliasesPackage;
	}
}

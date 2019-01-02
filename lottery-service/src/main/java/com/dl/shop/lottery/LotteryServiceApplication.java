package com.dl.shop.lottery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.dl.base.configurer.FeignConfiguration;
import com.dl.base.configurer.RestTemplateConfig;
import com.dl.base.configurer.WebMvcConfigurer;
import com.dl.shop.lottery.configurer.Swagger2;
import com.dl.shop.lottery.core.ProjectConstant;

@SpringBootApplication
@Import({ RestTemplateConfig.class, Swagger2.class, WebMvcConfigurer.class, FeignConfiguration.class })
@MapperScan(basePackages = { ProjectConstant.MAPPER_PACKAGE, "com.dl.shop.lottery.dao2" })
@EnableTransactionManagement
@EnableEurekaClient
@EnableFeignClients({ "com.dl.member.api","com.dl.store.api", "com.dl.order.api", "com.dl.shop.payment.api", "com.dl.lotto.api", "com.dl.shop.auth.api","com.dl.store.api"})
public class LotteryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LotteryServiceApplication.class, args);
	}

}

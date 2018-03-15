package com.dl.shop;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.dl.shop.lottery.LotteryServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LotteryServiceApplication.class)
public class BaseTest {

	public Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private ApplicationContext ctx;
	
}

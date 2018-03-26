package com.dl.shop.lottery.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LotteryPrintService {

	@Value("${print.ticket.url}")
	private String printTicketUrl;
	
	
}

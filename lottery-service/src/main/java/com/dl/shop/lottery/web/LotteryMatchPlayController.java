package com.dl.shop.lottery.web;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.shop.lottery.service.LotteryMatchPlayService;

/**
* Created by CodeGenerator on 2018/03/21.
*/
@RestController
@RequestMapping("/lottery/match/play")
public class LotteryMatchPlayController {
    @Resource
    private LotteryMatchPlayService lotteryMatchPlayService;

}

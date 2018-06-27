package com.dl.lottery.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.PrintLotteryRefundDTO;
import com.dl.lottery.param.PrintLotteryStatusByOrderSnParam;
import com.dl.lottery.param.PrintLotterysRefundsByOrderSnParam;
import com.dl.lottery.param.SaveLotteryPrintInfoParam;

@FeignClient(value="lottery-service")
public interface ILotteryPrintService {

	@RequestMapping(path="/lottery/print/save", method=RequestMethod.POST)
	public BaseResult<String> saveLotteryPrintInfo(SaveLotteryPrintInfoParam param);
	
	@RequestMapping(path="/lottery/print/printLotteryStatusByOrderSn", method=RequestMethod.POST)
    public BaseResult<Integer> printLotteryStatusByOrderSn( PrintLotteryStatusByOrderSnParam param) ;
    @RequestMapping(path="/lottery/print/printLotterysRefundsByOrderSn", method=RequestMethod.POST)
    public BaseResult<PrintLotteryRefundDTO> printLotterysRefundsByOrderSn( PrintLotterysRefundsByOrderSnParam param) ;
}
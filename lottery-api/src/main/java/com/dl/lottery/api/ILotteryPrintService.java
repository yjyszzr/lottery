package com.dl.lottery.api;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.lottery.dto.PrintLotteryRefundDTO;
import com.dl.lottery.dto.PrintStakeResultDTO;
import com.dl.lottery.param.NotifyParam;
import com.dl.lottery.param.PrintLotteryStatusByOrderSnParam;
import com.dl.lottery.param.PrintLotterysRefundsByOrderSnParam;
import com.dl.lottery.param.SaveLotteryPrintInfoParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="lottery-service")
public interface ILotteryPrintService {

	@RequestMapping(path="/lottery/print/save", method=RequestMethod.POST)
	public BaseResult<String> saveLotteryPrintInfo(SaveLotteryPrintInfoParam param);
	
	@RequestMapping(path="/lottery/print/printLotteryStatusByOrderSn", method=RequestMethod.POST)
    public BaseResult<Integer> printLotteryStatusByOrderSn( PrintLotteryStatusByOrderSnParam param) ;
	
	@RequestMapping(path="/lottery/print/printLotteryRefunds", method=RequestMethod.POST)
    public BaseResult<PrintLotteryRefundDTO> printLotterysRefundsByOrderSn(@RequestBody PrintLotterysRefundsByOrderSnParam param) ;
	
    @PostMapping("/lottery/print/printLottery")
    public BaseResult<String> printLottery(@RequestBody EmptyParam emptyParam);
	
    @PostMapping("/lottery/print/updatePrintLotteryCompareStatus")
    public BaseResult<String> updatePrintLotteryCompareStatus(@RequestBody EmptyParam emptyParam);

    @RequestMapping(path="/lottery/print/notifyPrintResultToMerchant", method=RequestMethod.POST)
    public BaseResult<PrintStakeResultDTO> notifyPrintResultToMerchant(@RequestBody NotifyParam param);

}
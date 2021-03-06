package com.dl.shop.lottery.web;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.JSONHelper;
import com.dl.lottery.dto.*;
import com.dl.lottery.param.*;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.OrderInfoAndDetailDTO;
import com.dl.order.param.OrderSnParam;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.service.LotteryMatchService;
import com.dl.shop.lottery.service.LotteryPrintService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lottery/print")
@Slf4j
public class LotteryPrintController {

	@Resource
	private LotteryPrintService lotteryPrintService;
	
	@Resource
	private LotteryMatchService lotteryMatchService;

	@Resource
	private IOrderService orderService;

	@ApiOperation(value = "商户查询出票情况接口", notes = "商户查询出票情况接口")
	@PostMapping("/queryPrintResultToMerchant")
	public BaseResult<PrintStakeResultDTO> queryPrintResultToMerchant(@Valid @RequestBody QueryPrintStakeParam param) {
		return lotteryPrintService.queryPrintResutToMerchant(param);
	}

	@ApiOperation(value = "通知商户查询出票情况", notes = "通知商户查询出票情况")
	@PostMapping("/notifyPrintResultToMerchant")
	public BaseResult<String> notifyPrintResultToMerchant(@Valid @RequestBody NotifyParam param) {
		String rst = lotteryPrintService.notifyPrintResultToMerchant(param.getNotifyUrl(),param.getMerchantOrderSn());
		return ResultGenerator.genSuccessResult("投注结果通知成功",rst);
	}

	@ApiOperation(value = "投注接口", notes = "投注接口")
    @PostMapping("/toStake")
    public BaseResult<DlToStakeDTO> toStake(@Valid @RequestBody DlToStakeParam param) {
		DlToStakeDTO dlToStakeDTO = lotteryPrintService.toStake(param);
    	return ResultGenerator.genSuccessResult("投注成功", dlToStakeDTO);
    }
	
	@ApiOperation(value = "投注结果通知", notes = "投注结果通知")
    @PostMapping("/callbackStake")
    public BaseResult<String> callbackStake(@RequestBody DlCallbackStakeParam param) {
		lotteryPrintService.callbackStake(param);
    	return ResultGenerator.genSuccessResult("投注结果通知成功");
    }
	
	@ApiOperation(value = "投注结果通知", notes = "投注结果通知")
	@PostMapping("/callbackStakeSenDe")
	public BaseResult<String> callbackStakeSenDe(DlCallbackStakeSenDeParam param) {
		log.info("森德出票回调内容={}",JSONHelper.bean2json(param));
		if(param.getMessageType().equals("ticketResult")) {
			lotteryPrintService.callbackStakeSenDe(param);
		}
		return ResultGenerator.genSuccessResult("投注结果通知成功");
	}
	
	@ApiOperation(value = "投注结果通知", notes = "投注结果通知")
    @PostMapping("/callbackStakeWeiCaiShiDai")
    public Map<String,String> callbackStakeWeiCaiShiDai(DlCallbackStakeWeiCaiShiDaiParam param) {
		return lotteryPrintService.callbackStakeWeiCaiShiDai(param);
    }
	
	@ApiOperation(value = "投注结果查询", notes = "投注结果查询")
    @PostMapping("/queryStake")
    public BaseResult<DlQueryStakeDTO> queryStake(@Valid @RequestBody DlQueryStakeParam param) {
		DlQueryStakeDTO dlQueryStakeDTO = lotteryPrintService.queryStake(param);
    	return ResultGenerator.genSuccessResult("投注结果查询成功", dlQueryStakeDTO);
    }
	
	@ApiOperation(value = "期次查询", notes = "期次查询")
    @PostMapping("/queryIssue")
    public BaseResult<DlQueryIssueDTO> queryIssue(@Valid @RequestBody DlQueryIssueParam param) {
		DlQueryIssueDTO dlQueryIssueDTO = lotteryPrintService.queryIssue(param);
    	return ResultGenerator.genSuccessResult("期次查询成功", dlQueryIssueDTO);
    }
	
	@ApiOperation(value = "账户余额查询", notes = "账户余额查询")
    @PostMapping("/queryAccount")
    public BaseResult<DlQueryAccountDTO> queryAccount(@Valid @RequestBody DlQueryAccountParam param) {
		DlQueryAccountDTO dlQueryAccountDTO = lotteryPrintService.queryAccount(param);
    	return ResultGenerator.genSuccessResult("账户余额查询成功", dlQueryAccountDTO);
    }
	
	@ApiOperation(value = "期次投注对账文件查询", notes = "期次投注对账文件查询")
    @PostMapping("/queryStakeFile")
    public BaseResult<DlQueryStakeFileDTO> queryStakeFile(@Valid @RequestBody DlQueryStakeFileParam param) {
		DlQueryStakeFileDTO dlQueryStakeFileDTO = lotteryPrintService.queryStakeFile(param);
    	return ResultGenerator.genSuccessResult("期次投注对账文件查询成功", dlQueryStakeFileDTO);
    }
	
	@ApiOperation(value = "期次中奖文件查询", notes = "期次中奖文件查询")
    @PostMapping("/queryPrizeFile")
    public BaseResult<DlQueryPrizeFileDTO> queryPrizeFile(@Valid @RequestBody DlQueryPrizeFileParam param) {
		DlQueryPrizeFileDTO dlQueryPrizeFileDTO = lotteryPrintService.queryPrizeFile(param);
    	return ResultGenerator.genSuccessResult("期次中奖文件查询成功", dlQueryPrizeFileDTO);
    }
	
	@ApiOperation(value = "生成预出票信息", notes = "生成预出票信息")
    @PostMapping("/save")
    public BaseResult<String> saveLotteryPrintInfo(@Valid @RequestBody SaveLotteryPrintInfoParam param) {
		if(StringUtils.isBlank(param.getOrderSn())) {
			return ResultGenerator.genFailResult();
		}
		OrderSnParam orderSnParam = new OrderSnParam();
		orderSnParam.setOrderSn(param.getOrderSn());
		BaseResult<OrderInfoAndDetailDTO> orderWithDetailByOrderSn = orderService.getOrderWithDetailByOrderSn(orderSnParam);
		if(orderWithDetailByOrderSn.getCode() != 0) {
			return ResultGenerator.genFailResult();
		}
		List<LotteryPrint> printLotterysByOrderSn = lotteryPrintService.printLotterysByOrderSn(param.getOrderSn());
		if(CollectionUtils.isNotEmpty(printLotterysByOrderSn)) {
			return ResultGenerator.genSuccessResult("已创建");
		}
		OrderInfoAndDetailDTO data = orderWithDetailByOrderSn.getData();
		Integer lotteryClassifyId = data.getOrderInfoDTO().getLotteryClassifyId();
		Integer lotteryPlayClassifyId = data.getOrderInfoDTO().getLotteryPlayClassifyId();
		if(1== lotteryClassifyId && 8 == lotteryPlayClassifyId) {
			return lotteryPrintService.saveLotteryPrintInfo(data, param.getOrderSn());
		}
		List<LotteryPrintDTO> lotteryPrints = lotteryMatchService.getPrintLotteryListByOrderInfo(data, param.getOrderSn());
		if(CollectionUtils.isEmpty(lotteryPrints)) {
			return ResultGenerator.genFailResult();
		}
		Double printLotteryRoutAmount = lotteryMatchService.printLotteryRoutAmount();
        int printLotteryCom = 1 ;//河南出票公司
        log.info("save printLotteryCom orderSn={},ticketAmount={},canBetMoney={}",param.getOrderSn(),data.getOrderInfoDTO().getTicketAmount(),printLotteryRoutAmount);
        if(data.getOrderInfoDTO().getTicketAmount().subtract(new BigDecimal(printLotteryRoutAmount)).compareTo(BigDecimal.ZERO)<0){
            log.info("orderSn={},设置出票公司为西安出票公司",param.getOrderSn());
            printLotteryCom = 2;//西安出票公司
        }
        return lotteryPrintService.saveLotteryPrintInfo(lotteryPrints, param.getOrderSn(),printLotteryCom);
    }
	
	@ApiOperation(value = "查询订单对应的出票状态", notes = "查询订单对应的出票状态:1：待出票，2出票失败，3待开奖")
    @PostMapping("/printLotteryStatusByOrderSn")
    public BaseResult<Integer> printLotteryStatusByOrderSn(@Valid @RequestBody PrintLotteryStatusByOrderSnParam param) {
		Integer status = lotteryPrintService.printLotteryStatusByOrderSn(param.getOrderSn());
		return ResultGenerator.genSuccessResult("success", status);
	}
	@ApiOperation(value = "获取订单出票失败退款总金额", notes = "获取订单出票失败总金额:noOrder订单出票任务不存在，noFinish:尚未完全出票,noRefund:已全部出票不需退款,fullRefund:全部出票失败退款,xx.xx:该订单部分出票失败的总金额")
    @PostMapping("/printLotteryRefunds")
    public BaseResult<PrintLotteryRefundDTO> printLotterysRefundsByOrderSn(@Valid @RequestBody PrintLotterysRefundsByOrderSnParam param) {
		PrintLotteryRefundDTO printLotteryRefundDTO = lotteryPrintService.printLotterysRefundsByOrderSn(param.getOrderSn());
		return ResultGenerator.genSuccessResult("success", printLotteryRefundDTO);
	}
	
	@ApiOperation(value = "出票任务 ,调用第三方接口出票定时任务", notes = "出票任务 ,调用第三方接口出票定时任务")
    @PostMapping("/printLottery")
    public BaseResult<String> printLottery(@RequestBody EmptyParam emptyParam) {
		lotteryPrintService.printLottery();
		return ResultGenerator.genSuccessResult("sucess");
	}
	
	@ApiOperation(value = "更新彩票信息,给定时任务调用", notes = "更新彩票信息,给定时任务调用")
    @PostMapping("/updatePrintLotteryCompareStatus")
    public BaseResult<String> updatePrintLotteryCompareStatus(@RequestBody EmptyParam emptyParam) {
		lotteryPrintService.updatePrintLotteryCompareStatus();
		return ResultGenerator.genSuccessResult("sucess");
	}
	
}

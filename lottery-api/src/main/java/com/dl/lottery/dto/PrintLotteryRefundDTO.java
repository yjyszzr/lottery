package com.dl.lottery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class PrintLotteryRefundDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    
    public static String refundNoOrder="noOrder";//noOrder订单出票任务不存在
    public static String refundNoFinish="noFinish";//noFinish:尚未完全出票
    public static String refundNoRefund="noRefund";//noRefund:已全部出票不需退款,
    public static String refundFullRefund="fullRefund";//fullRefund:全部出票失败退款
    public static String refundPartRefund="partRefund";//partRefund:该订单部分出票失败的总金额
    
    @ApiModelProperty(value = "退款金额")
    public BigDecimal refundAmount;
    @ApiModelProperty(value = "退款标志 :noOrder:订单出票任务不存在,noFinish:尚未完全出票,noRefund:已全部出票不需退款,fullRefund:全部出票失败退款partRefund:该订单部分出票失败的总金额")
    public String refundSign;
    public Integer orderStatus;
    public Integer printLotteryStatus;
    public PrintLotteryRefundDTO(){
        
    }
    public static PrintLotteryRefundDTO instanceByPrintLotteryRefund(String refundSign,Integer orderStatus,Integer printLotteryStatus){
        PrintLotteryRefundDTO printLotteryRefundDTO = new PrintLotteryRefundDTO();
        printLotteryRefundDTO.setRefundSign(refundSign);
        printLotteryRefundDTO.setOrderStatus(orderStatus);
        printLotteryRefundDTO.setPrintLotteryStatus(printLotteryStatus);
        return printLotteryRefundDTO;
    }
}